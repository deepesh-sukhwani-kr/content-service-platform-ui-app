import {Component, OnInit} from '@angular/core';
import {Papa} from "ngx-papaparse";
import {CsvAsset} from "./model/csv-asset";
import {Message} from "primeng/components/common/api";
import {ImageObject} from "../csp-add/model/ImageObject";
import {AssetType} from "../csp-add/constants/assetType";
import {AssetIdentifier} from "../csp-add/model/AssetIdentifier";
import {ImageAddRequest} from "../csp-add/model/imageAddRequest";
import {CspAddService} from "../csp-add/csp-add.service";
import {AddImageResponse} from "../csp-add/model/addImageResponse";
import {AssetDetailResponse} from "../csp-add/model/assetDetailResponse";
import {CsvUploadConstants} from "./csv-upload-constants";
import {AuthService, User} from "kroger-ng-oauth2";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {UtilService} from "../util/util.service";

@Component({
  selector: 'app-csv-upload',
  templateUrl: './csv-upload.component.html',
  styleUrls: ['./csv-upload.component.less'],
  providers: [CspAddService, UtilService]
})
export class CsvUploadComponent implements OnInit {

  ifSubmitted: boolean = false;
  csv: File;
  csvAssets: CsvAsset[];
  msgs: Message[] = [];
  hasError: boolean = false;
  ifImagesNotUploaded: boolean = true;
  isFinalized: boolean = false;
  progressVisible: boolean = false;
  isCompleted: boolean = false

  constructor(private papa: Papa, private addService: CspAddService,
              private authService: AuthService, private utilService: UtilService,
              private router: Router) {
  }

  ngOnInit() {
  }

  onUpload(event, csvUpload) {
    this.hasError = false;
    for (let file of event.files) {
      this.csv = file;
      this.msgs = [];
      if (file.name.endsWith(".csv") || file.name.endsWith(".CSV"))
        this.parse();
      else
        this.showError('Invalid File Extension:',
          "Provided file extension is invalid, the only supported extension is '.csv'");
    }
    csvUpload.clear();
  }

  onImageUpload(event, imageUpload) {
    let files: File[] = event.files;
    this.msgs = [];
    for (let file of files) {
      this.readFile(file);
    }
    imageUpload.clear();
  }

  submit() {
    if(this.ifSingleImageUploaded()) {
      this.isFinalized = true;
      this.ifImagesNotUploaded = true;
      this.progressVisible = true;
      this.msgs = [];
      this.msgs.push({
        severity: 'warn', summary: 'Warning: ',
        detail: 'Upload is in progress. Please DO NOT hit back, refresh or any other button in this window. ' +
        'Progress will be lost!'
      });
      if (this.ifImagesNotUploaded) {
        this.msgs.push({
          severity: 'warn', summary: 'Warning: ',
          detail: 'Files not provided for the rows highlighted in Red. ' +
          'Hence, those rows will not be processed by Content Services Platform!'
        });
        this.csvAssets.forEach(csvAsset => {
          if (!csvAsset.fileProvidedStatus)
            document.getElementById(csvAsset.fileName + '_tr').style.backgroundColor = '#f8d3d9'
        })
      }
      this.csvAssets.forEach(csvAsset => {
        if (csvAsset.fileProvidedStatus) {
          csvAsset.uploadStatus = 'In Progress';
          document.getElementById(csvAsset.fileName).style.color = '#F8E535';
          this.utilService.getEndpoint('add').then(endpoint => {
            this.addService.addImages(this.buildAddImageRequest(csvAsset), endpoint)
              .subscribe(successResponse => this.handleSuccess(successResponse, csvAsset),
                failureResponse => this.handleFailure(failureResponse, csvAsset));
          });
        } else {
          csvAsset.processed = true;
        }
      })
    }else {
      this.showError('Error', 'At least one file should be uploaded before clicking submit!');
    }
  }

  download() {
    var csvData = this.convertToCSV();
    var a = document.createElement("a");
    a.setAttribute('style', 'display:none;');
    document.body.appendChild(a);
    var blob = new Blob([csvData], {type: 'text/csv'});
    var url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = 'CsvUploadStatusReport_' + (new Date()).toISOString() + '.csv';
    a.click();
  }

  reset() {
    this.ifSubmitted = false;
    this.csv = undefined;
    this.csvAssets = [];
    this.msgs = [];
    this.hasError = false;
    this.ifImagesNotUploaded = true;
    this.isFinalized = false;
    this.progressVisible = false;
    this.isCompleted = false
    document.getElementById("imageUpload").style.visibility = "hidden";
    document.getElementById("csvTable").style.visibility = "hidden";
  }

  private ifSingleImageUploaded(): boolean{
    let flag: boolean = false;
    this.csvAssets.forEach(csvAsset => {
      if(csvAsset.fileProvidedStatus)
        flag = true;
    });
    return flag;
  }

  private handleSuccess(response: Object, csvAsset: CsvAsset) {
    let res = <AddImageResponse>response;
    if (res != null && res.assetDetails!= null && res.assetDetails[0]) {
      if (this.isSequenceSuccess(res.assetDetails[0])) {
        csvAsset.imageId = res.assetDetails[0].imageId;
        csvAsset.url = res.assetDetails[0].imageUrl;
        csvAsset.uploadStatus = 'Successful: ' + res.assetDetails[0].statusCode + ' - ' + res.assetDetails[0].statusMessage
        document.getElementById(csvAsset.fileName).style.color = '#00e727';
      } else {
        csvAsset.uploadStatus = 'Failed: ' + res.assetDetails[0].statusCode + ' - ' + res.assetDetails[0].errorDetails
        document.getElementById(csvAsset.fileName).style.color = '#f82c25';
      }
    }else if(res.errorResponse != null){
      csvAsset.uploadStatus = 'Failed: Error at CSP UI Server';
      document.getElementById(csvAsset.fileName).style.color = '#f82c25';
    }
    else {
      csvAsset.uploadStatus = 'Failed: Please try again'
      document.getElementById(csvAsset.fileName).style.color = '#f82c25';
    }

    csvAsset.processed = true;
    this.completeProcessing();
  }

  private handleFailure(response: Object, csvAsset: CsvAsset) {

    if((<HttpErrorResponse>response).url)
      this.router.navigate(['/login']);

    csvAsset.uploadStatus = 'Failed: Connection Error';
    csvAsset.processed = true;
    document.getElementById(csvAsset.fileName).style.color = '#f82c25';
    console.log(response);
    this.completeProcessing();
    console.log((<User>this.authService.getUser()).username);
    console.log((<User>this.authService.getUser()).accountNonExpired);
    console.log((<User>this.authService.getUser()).accountNonLocked);
    console.log((<User>this.authService.getUser()).anonymous);
    console.log((<User>this.authService.getUser()).coopDivisionNumber);
    console.log((<User>this.authService.getUser()).credentialsNonExpired);
    console.log((<User>this.authService.getUser()).divisionCode);
    console.log((<User>this.authService.getUser()).divisionNumber);
    console.log((<User>this.authService.getUser()).emailAddress);
    console.log((<User>this.authService.getUser()).enabled);
    console.log((<User>this.authService.getUser()).groups);
    console.log((<User>this.authService.getUser()).hash);
    console.log((<User>this.authService.getUser()).hqDivisionNumber);
    console.log((<User>this.authService.getUser()).initials);




  }

  private isSequenceSuccess(sequence: AssetDetailResponse) {
    if (!sequence)
      return false;
    return sequence.statusMessage === 'SUCCESS' || sequence.statusMessage === 'SUCCESSWITHDUPLICATES' ||
      sequence.statusMessage === 'FAILUREWITHDUPLICATES';
  }


  private parse() {
    this.papa.parse(this.csv, {
      complete: result => {
        this.validateCsv(result.data);
        if (!this.hasError) {
          this.populateAssets(result.data);
          this.validateAssets();
          if (!this.hasError) {
            this.ifSubmitted = true;
            document.getElementById("imageUpload").style.visibility = "visible";
            document.getElementById("csvTable").style.visibility = "visible";
          }
        }
      },
      error: result => {
        this.showError('CSV Parsing Error',
          'Unable to parse the provided file. Please check the file before uploading');
      }
    })
  }

  private populateAssets(data: any) {
    this.csvAssets = [];
    for (let i = 0; i < data.length; i++) {
      if (i != 0 && data[i].length === 14) {
        this.csvAssets.push(this.populateAsset(data[i]));
      }
    }
  }

  private populateAsset(asset: any): CsvAsset {
    let csvAsset: CsvAsset = new CsvAsset();
    csvAsset.gtin = asset[0];
    csvAsset.viewAngle = asset[1];
    if (asset[2])
      csvAsset.size = this.getSize(asset[2]);
    csvAsset.background = asset[3];
    csvAsset.lastModifiedDate = asset[4];
    csvAsset.description = asset[5];
    csvAsset.upc10 = asset[6];
    csvAsset.upc12 = asset[7];
    csvAsset.upc13 = asset[8];
    csvAsset.source = asset[9];
    csvAsset.imageType = asset[10];
    csvAsset.fileName = asset[11];
    csvAsset.colorProfile = asset[12];
    csvAsset.imageOrientationType = asset[13];
    csvAsset.fileProvided = "../../assets/cros.jfif";
    csvAsset.fileProvidedStatus = false;
    csvAsset.uploadStatus = 'Not Started';
    csvAsset.processed = false;
    return csvAsset;
  }

  private getSize(size: string): string {
    if (size) {
      if (size.match('[0-9]+x[0-9]+') && size.match('[0-9]+x[0-9]+').length > 0) {
        return size.match('[0-9]+x[0-9]+')[0];
      }
      if (size.match('[0-9]+X[0-9]+') && size.match('[0-9]+X[0-9]+').length > 0) {
        return size.match('[0-9]+X[0-9]+')[0];
      }
      if (size.match('[0-9]+\\*[0-9]+') && size.match('[0-9]+\\*[0-9]+').length > 0) {
        return size.match('[0-9]+\\*[0-9]+')[0];
      }
      if (size.match('[0-9]+\\s+X\\s+[0-9]+') && size.match('[0-9]+\\s+X\\s+[0-9]+').length > 0) {
        return size.match('[0-9]+\\s+X\\s+[0-9]+')[0];
      }
      if (size.match('[0-9]+\\s+x\\s+[0-9]+') && size.match('[0-9]+\\s+x\\s+[0-9]+').length > 0) {
        return size.match('[0-9]+\\s+x\\s+[0-9]+')[0];
      }
      if (size.match('[0-9]+\\s+\\*\\s+[0-9]+') && size.match('[0-9]+\\s+\\*\\s+[0-9]+').length > 0) {
        return size.match('[0-9]+\\s+\\*\\s+[0-9]+')[0];
      }
    }
    return '';
  }

  private readFile(file: File) {
    let isValidFile: boolean = false;
    this.csvAssets.forEach(csvAsset => {
      if (csvAsset.fileName.toUpperCase().trim() === file.name.toUpperCase())
        isValidFile = true;
    });
    if (isValidFile) {
      this.csvAssets.forEach(csvAsset => {
        if (csvAsset.fileName.toUpperCase().trim() === file.name.toUpperCase()) {
          isValidFile = true;
          var reader = new FileReader();
          reader.onloadend = () => {
            csvAsset.asset = btoa(reader.result);
            csvAsset.fileProvided = "../../assets/tick.jpg";
            csvAsset.fileProvidedStatus = true;
            this.enableSubmit();
            //this.printAssets();
          }
          reader.readAsBinaryString(file);
        }
      });
    } else {
      this.showError("Invalid File Name:", "Image file name " + file.name + " " +
        "is not present on the provided CSV")
    }
  }

  private showError(type: string, description: string) {
    this.hasError = true;
    this.msgs.push({
      severity: 'error', summary: type,
      detail: description
    });
  }

  private validateCsv(data: any) {
    this.hasError = false;
    if (data[0].length != 14 || data[0][0].trim().toUpperCase() != 'GTIN' ||
      data[0][1].trim().toUpperCase() != 'VIEW_ANGLE' ||
      data[0][2].trim().toUpperCase() != 'SIZE' ||
      data[0][3].trim().toUpperCase() != 'BACKGROUND' ||
      data[0][4].trim().toUpperCase() != 'LAST_MODIFIED_DT' ||
      data[0][5].trim().toUpperCase() != 'DESCRIPTION' ||
      data[0][6].trim().toUpperCase() != 'UPC_10' ||
      data[0][7].trim().toUpperCase() != 'UPC_12' ||
      data[0][8].trim().toUpperCase() != 'UPC_13' ||
      data[0][9].trim().toUpperCase() != 'SOURCE' ||
      data[0][10].trim().toUpperCase() != 'IMAGE_TYPE' ||
      data[0][11].trim().toUpperCase() != 'FILE_NAME' ||
      data[0][12].trim().toUpperCase() != 'COLOR_PROFILE' ||
      data[0][13].trim().toUpperCase() != 'IMAGE_ORIENTATION_TYPE') {
      this.showError("Invalid data format:", "The provided CSV data format is invalid. " +
        "The column headers should be present exactly in the following ORDER and SPELLING, " +
        "GTIN, VIEW_ANGLE, SIZE, BACKGROUND, LAST_MODIFIED_DT, DESCRIPTION, UPC_10, UPC_12, UPC_13, " +
        "SOURCE, IMAGE_TYPE, FILE_NAME, COLOR_PROFILE, IMAGE_ORIENTATION_TYPE");
      this.hasError = true;
      return;
    }
  }

  private validateAssets() {
    for (let i = 0; i < this.csvAssets.length; i++) {
      this.validateFields(this.csvAssets[i], i + 2);
    }
    let fileNameSet: Set<String> = new Set();
    this.csvAssets.forEach(csvAsset => fileNameSet.add(csvAsset.fileName));
    if (fileNameSet.size != this.csvAssets.length) {
      this.showError('Validation error:', 'Duplicate filenames present in the provided CSV');
    }
  }


  private validateFields(asset: CsvAsset, index: number) {
    let str: string = '';
    if (!asset.gtin.trim() || asset.gtin.trim().length != 14)
      str += CsvUploadConstants.VALIDATION_ERR_GTIN_LENGTH;
    if (isNaN(+asset.gtin.trim()))
      str += CsvUploadConstants.VALIDATION_ERR_GTIN_TYPE;
    if (!asset.viewAngle.trim())
      str += CsvUploadConstants.VALIDATION_ERR_VIEW_ANGLE_MISSING;
    if (!this.validateProvidedSize(asset.size))
      str += CsvUploadConstants.VALIDATION_ERR_SIZE_FORMAT;
    if (!asset.background.trim())
      str += CsvUploadConstants.VALIDATION_ERR_BACKGROUND_MISSING;
    if (asset.lastModifiedDate.trim() && !this.validateDate(asset.lastModifiedDate))
      str += CsvUploadConstants.VALIDATION_ERR_LAST_MODIFIED_DATE_FORMAT;
    if (!asset.description.trim())
      str += CsvUploadConstants.VALIDATION_ERR_DESCRIPTION_MISSING;
    if (!asset.source.trim())
      str += CsvUploadConstants.VALIDATION_ERR_SOURCE_MISSING;
    if (!asset.imageType.trim())
      str += CsvUploadConstants.VALIDATION_ERR_IMAGE_TYPE_MISSING;
    if (!asset.fileName.trim())
      str += CsvUploadConstants.VALIDATION_ERR_FILE_NAME_MISSING;
    if (!asset.colorProfile.trim())
      str += CsvUploadConstants.VALIDATION_ERR_COLOR_PROFILE_MISSING;
    if (!asset.imageOrientationType.trim())
      str += CsvUploadConstants.VALIDATION_ERR_IMAGE_ORIENTATION_TYPE;
    if (str != '') {
      this.showError(CsvUploadConstants.VALIDATION_PREFIX_ROW_NO + index + ':', str);
      this.hasError = true;
    }
  }

  private validateProvidedSize(str: string): boolean {
    var regEx1 = new RegExp('[0-9]+X[0-9]+', 'i');
    var regEx2 = new RegExp('[0-9]+x[0-9]+', 'i');
    var regEx3 = new RegExp('[0-9]+\\*[0-9]+', 'i');
    var regEx4 = new RegExp('[0-9]+\\s+X\\s+[0-9]+', 'i');
    var regEx5 = new RegExp('[0-9]+\\s+x\\s+[0-9]+', 'i');
    var regEx6 = new RegExp('[0-9]+\\s+\\*\\s+[0-9]+', 'i');
    if (str)
      return regEx1.test(str.trim()) || regEx2.test(str.trim()) || regEx3.test(str.trim()) ||
        regEx4.test(str.trim()) || regEx5.test(str.trim()) || regEx6.test(str.trim());
    else
      return false;
  }

  private validateDate(date: string): boolean {
    let dateExpression1 = new RegExp('^(((0?[1-9]|1[012])/(0?[1-9]|1\\d|2[0-8])|(0?[13456789]|1[012])/(29|30)|' +
      '(0?[13578]|1[02])/31)/(19|[2-9]\\d)\\d{2}|0?2/29/((19|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|(([2468][048]' +
      '|[3579][26])00)))$', 'i');
    let dateExpression2 = new RegExp('^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$', 'i');
    return dateExpression1.test(date.trim()) || dateExpression2.test(date.trim());
  }

  private enableSubmit() {
    let flag: boolean = false;
    this.csvAssets.forEach(cvsAsset => {
      if (!cvsAsset.asset)
        flag = true;
    });
    this.ifImagesNotUploaded = flag;
  }

  private buildAddImageRequest(csvAsset: CsvAsset): ImageAddRequest {
    const IMAGE_TYPE: string = 'ProductImage';

    let identifier: AssetIdentifier;
    identifier = new AssetIdentifier();
    identifier.gtin = csvAsset.gtin;

    var request: ImageAddRequest = new ImageAddRequest();
    request.referenceId = CsvUploadConstants.REFERENCE_ID_PREFIX+
      (<User>this.authService.getUser()).username+'-' + new Date().getMilliseconds()
      + "-" + identifier.gtin;
    request.creationDatetime = new Date().toISOString();
    request.imageType = csvAsset.imageOrientationType;
    request.assetIdentifier = identifier;
    request.assetDetails = this.buildAssets(csvAsset);
    return request;
  }


  private buildAssets(csvAsset: CsvAsset): ImageObject[] {
    let images: ImageObject[] = [];
    let image: ImageObject;
    image = new ImageObject();
    image.sequence = 1;
    image.viewAngle = csvAsset.viewAngle;
    image.providedSize = csvAsset.size;
    image.background = csvAsset.background;
    image.source = csvAsset.source;
    image.description = csvAsset.description;
    image.lastModifiedDate = this.parseDate(csvAsset.lastModifiedDate);
    image.fileName = csvAsset.fileName;
    image.fileExtension = csvAsset.imageType;
    image.assetType = AssetType.ASSETTYPEBASE64ENCODED;
    image.asset = csvAsset.asset;
    image.colorProfile = csvAsset.colorProfile;
    image.upc10 = csvAsset.upc10;
    image.upc12 = csvAsset.upc12;
    image.upc13 = csvAsset.upc13;
    image.imageOrientationType = csvAsset.imageOrientationType;
    images.push(image);
    return images;
  }

  private parseDate(dateString: string): string {
    if (dateString && dateString.trim())
      return new Date(dateString).toISOString();
    return new Date().toISOString();
  }

  private completeProcessing() {
    let completed: boolean = true;
    this.csvAssets.forEach(csvAsset => {
      if (!csvAsset.processed)
        completed = false;
    });
    if (completed) {
      this.progressVisible = false;
      this.msgs = [];
      this.isCompleted = true;
      if(this.ifImagesNotUploaded) {
        this.msgs.push({
          severity: 'warn', summary: 'Warning: ',
          detail: 'Files not provided for the rows highlighted in Red. ' +
          'Hence, those rows will not be processed by Content Services Platform!'
        });
      }
    }
  }

  private convertToCSV() {
    let str: string = '';
    str += CsvUploadConstants.UPLOAD_STATUS_REPORT_FORMAT;
    this.csvAssets.forEach(csvAsset => {
      let line: string = '';
      if (csvAsset.fileProvidedStatus)
        line += 'Yes,';
      else
        line += 'No,';
      line += csvAsset.uploadStatus + ',';
      if (csvAsset.imageId)
        line += csvAsset.imageId + ',';
      else
        line += ','
      line += csvAsset.fileName + ',';
      line += csvAsset.gtin + ',';
      line += csvAsset.viewAngle + ',';
      line += csvAsset.size + ',';
      line += csvAsset.background + ',';
      line += csvAsset.lastModifiedDate + ',';
      line += csvAsset.description + ',';
      line += csvAsset.upc10 + ',';
      line += csvAsset.upc12 + ',';
      line += csvAsset.upc13 + ',';
      line += csvAsset.source + ',';
      line += csvAsset.imageType + ',';
      line += csvAsset.colorProfile + ',';
      line += csvAsset.imageOrientationType;
      str += line + '\r\n';
    })
    return str;
  }

}
