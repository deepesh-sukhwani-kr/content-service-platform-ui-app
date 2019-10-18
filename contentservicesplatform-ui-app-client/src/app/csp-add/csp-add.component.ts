import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {MessageService, SelectItem} from "primeng/api";
import {Message} from "primeng/components/common/api";
import {ImageAddRequest} from "./model/imageAddRequest";
import {AssetIdentifier} from "./model/AssetIdentifier";
import {ImageObject} from "./model/ImageObject";
import {CspConstants} from "./constants/csp.constants";
import {AssetType} from "./constants/assetType";
import {CspAddService} from "./csp-add.service";
import {AssetDetailResponse} from "./model/assetDetailResponse";
import {AddImageResponse} from "./model/addImageResponse";
import {AuthService, User} from "kroger-ng-oauth2";
import {Router} from "@angular/router";
import {UtilService} from "../util/util.service";
import {ViewAngleResponse} from "../util/model/view-angle-response";


/**
 * @author Dhriti Ghosh
 */
@Component({
  selector: 'app-csp-add',
  templateUrl: './csp-add.component.html',
  styleUrls: ['./csp-add.component.less'],
  providers: [MessageService, UtilService]
})
export class CspAddComponent implements OnInit {

  form: FormGroup;
  sources: SelectItem[];
  viewAngles: SelectItem[];
  backgrounds: SelectItem[];
  uploadedFiles: File[] = [];
  selectedVendor: string;
  imageAttributes: FormArray;
  msgs: Message[][] = [];
  msg: string;
  msgSeverity: string;
  imageIds: string[] = [];
  imageUrls: string[] = [];
  fileStrings: string[] = [];
  div_visible: boolean = false;
  ifSubmitted: boolean = false;
  viewAngleResponse: string[];

  constructor(private _formBuilder: FormBuilder, private http: HttpClient,
              private messageService: MessageService, private  addService: CspAddService,
              private utilService: UtilService,
              private authService: AuthService,  private router: Router) {

  }

  ngOnInit() {
    this.initiateSources();
    this.initiateViewAngles();
    this.initiateBackgrounds();
    this.initiateForm();
  }

  submit() {
    this.validate();
    if(this.ifEmpty(this.msgs) && !this.msg) {
      this.div_visible = true;
      var request: ImageAddRequest = this.buildAddImageRequest();
      (<HTMLInputElement>document.getElementById("submit")).disabled = true;
      this.ifSubmitted = true;
      this.form.disable();
      this.utilService.getEndpoint('add').then(endpoint => {
        this.addService.addImages(request, endpoint)
          .subscribe(successResponse => this.handleSuccess(successResponse),
            failureResponse => this.handleFailure(failureResponse));
      })

    }
  }

  reset() {
    let length = this.imageAttributes.length;
    for (var i = length - 1; i >= 0; i--) {
      this.imageAttributes.removeAt(i);
    }
    this.msgs = [];
    this.msg = "";
    this.msgSeverity = "";
    this.setVisibility("gtinMessage", "hidden");
    this.form.reset();
    this.addAttributes();
    (<HTMLInputElement>document.getElementById("submit")).disabled = false;
    this.div_visible = false;
    this.form.enable();
    this.ifSubmitted = false;

  }

  onUpload(event, index: number) {
    (<HTMLInputElement>document.getElementById("submit")).disabled = true;
    this.ifSubmitted = true;
    for (let file of event.files) {
      this.uploadedFiles[index] = file;
      var reader = new FileReader();
      reader.onloadend = () => {
        this.fileStrings[index] = btoa(reader.result);
        this.messageService.clear('uploadToast');
        this.messageService.add({
          key: 'uploadToast',
          severity: 'success',
          summary: 'Success',
          detail: 'File uploaded successfully'
        });
        (<HTMLInputElement>document.getElementById("submit")).disabled = false;
      }
      reader.readAsBinaryString(this.uploadedFiles[index]);

    }

    (<HTMLInputElement>document.getElementById("fileName" + index)).value = this.uploadedFiles[index].name;
  }

  deleteRow(index: number) {
    if (this.msgs[index]) {
      var len = this.msgs.length;
      for (var i = index; i < len; i++)
        this.msgs[i] = this.msgs[i + 1];
      this.msgs = this.msgs.slice(0, len);
    }
    if (this.uploadedFiles[index]) {
      var len = this.uploadedFiles.length;
      for (var i = index; i < len; i++)
        this.uploadedFiles[i] = this.uploadedFiles[i + 1];
      this.uploadedFiles = this.uploadedFiles.slice(0, len);
    }
    this.imageAttributes.removeAt(index);
  }

  onSourceChange(event, i) {
    if (event.value === "imp-support-legacy-ds") {
      this.setVisibility("fileUpload" + i, "visible");
      this.setVisibility("vendorUrl" + i, "hidden");
      this.setVisibility("vendorUrlLabel" + i, "hidden");
      (<HTMLInputElement>document.getElementById("fileName" + i)).value = "";
      (<HTMLInputElement>document.getElementById("fileName" + i)).disabled = true;
      this.ifSubmitted = false;
    } else if (event.value === "kwikee" || event.value === "gladson") {
      this.setVisibility("fileUpload" + i, "hidden");
      this.setVisibility("vendorUrl" + i, "hidden");
      this.setVisibility("vendorUrlLabel" + i, "hidden");
      (<HTMLInputElement>document.getElementById("fileName" + i)).value = "";
      (<HTMLInputElement>document.getElementById("fileName" + i)).disabled = false;
      this.ifSubmitted = true;
    } else {
      this.selectedVendor = event.value.toUpperCase();
      this.setVisibility("vendorUrl" + i, "visible");
      this.setVisibility("vendorUrlLabel" + i, "visible");
      this.setVisibility("fileUpload" + i, "hidden");
      (<HTMLInputElement>document.getElementById("fileName" + i)).value = "";
      (<HTMLInputElement>document.getElementById("fileName" + i)).disabled = false;
      this.ifSubmitted = true;
    }
  }

  private handleSuccess(response: Object) {
    let res = <AddImageResponse>response;
    console.log('i am here');
    this.setVisibility("gtinMessage", "hidden");
    this.msgs = [];
    if(res.errorResponse){
      this.showGtinValidationError(
        res.errorResponse.statusCode + '-' + res.errorResponse.statusMessage);
      return;
    }
    res.assetDetails.forEach(data => {
      this.msgs.push(null);
      this.imageIds.push(null);
      this.imageUrls.push(null);
    });
    var resultLength = res.assetDetails.length;
    for (var index = 0; index < resultLength; index++) {
      var messageArray: Message[] = [];
      var sequence = +res.assetDetails[index].sequence - 1;
      if (this.isSequenceSuccess(res.assetDetails[index])) {
        messageArray.push({
          severity: 'success', summary: 'SUCCESS',
          detail: res.assetDetails[index].statusCode + ' - ' + res.assetDetails[index].statusMessage
        });
        this.imageIds[sequence] = res.assetDetails[index].imageId;
        this.imageUrls[sequence] = res.assetDetails[index].imageUrl;
        this.setVisibility("imageId" + sequence, "visible");
      } else {
        messageArray.push({
          severity: 'error', summary: 'FAILURE',
          detail: res.assetDetails[index].statusCode + ' - ' + res.assetDetails[index].errorDetails
        });
      }
      this.msgs[sequence] = messageArray;
      this.div_visible = false;
    }
  }

  private handleFailure(reason: Object) {
    if((<HttpErrorResponse>reason).url)
      this.router.navigate(['/login']);
    this.div_visible = false;
    this.showGtinValidationError('Could not connect to the backend. Please try again later.');
    console.log(reason);
  }

  private isSequenceSuccess(sequence: AssetDetailResponse) {
    return sequence.statusMessage === 'SUCCESS' || sequence.statusMessage === 'SUCCESSWITHDUPLICATES' ||
      sequence.statusMessage === 'FAILUREWITHDUPLICATES';
  }

  private initiateSources() {
    this.sources = [
      {label: 'Kwikee', value: 'kwikee'},
      {label: 'Gladson', value: 'gladson'},
      {label: 'OneWorldSync', value: 'url'},
      {label: 'Imp-Support-Legacy-DS', value: 'imp-support-legacy-ds'}
    ];
  }

  private initiateViewAngles() {
    this.utilService.getViewAngels().subscribe(data => {
      let response = <ViewAngleResponse>data;
      this.viewAngles = [];
      response.viewAngles.forEach( data => {
        this.viewAngles.push({label: data.toUpperCase(), value: data})
      })
    });
  }

  private initiateBackgrounds() {
    this.backgrounds = [
      {label: 'White', value: 'white'},
      {label: 'Transparent', value: 'transparent'}
    ];
  }

  private initiateForm() {
    this.form = this._formBuilder.group({
      gtin: [''],
      imageAttributes: this._formBuilder.array([])
    });
    this.imageAttributes = this.form.get('imageAttributes') as FormArray;
    this.addAttributes();
  }

  private ifEmpty(messages: Message[][]): boolean{
    if(!this.msgs || this.msgs.length === 0)
      return true;
    let flag: boolean = true;
    this.msgs.forEach(data => {
      if(data && data.length > 0)
        flag = false;
    })
    return flag;
  }

  addAttributes() {
    const imageAttrObj = this._formBuilder.group({
      source: [],
      viewAngle: [],
      resolution: [],
      background: [],
      description: [],
      providedSize: [],
      url: [],
      file: [],
      fileName: [],
      lastModified: []
    });
    this.imageAttributes.push(imageAttrObj);
    this.uploadedFiles.push(null);
    this.imageUrls.push(null);
    this.imageIds.push(null);
    this.fileStrings.push(null);
  }

  getMessage(i: number): Message[]{
    console.log(i);
    console.log(this.msgs[i]);
    return this.msgs[i];
  }

  private validate() {
    this.validateGtin();
    this.msgs = [];
    for (var i = 0; i < this.imageAttributes.length; i++) {
      this.validateImage(i);
    }
  }

  private validateGtin(): boolean {
    let flag: boolean = false;
    if (!this.form.get('gtin').value || this.form.get('gtin').value.length != 14)
      this.showGtinValidationError('Validation error: Length of GTIN should be 14 characters');
    else if (isNaN(this.form.get('gtin').value))
      this.showGtinValidationError('Validation error: GTIN should be in digits');
    else {
      this.setVisibility("gtinMessage", "hidden");
      flag = true;
    }
    return flag;
  }

  private showGtinValidationError(description: string) {
    this.msg = description;
    this.msgSeverity = 'error';
    this.setVisibility("gtinMessage", "visible");
  }

  private validateImage(index: number): boolean{
    let flag: boolean = true;
    var messageArray: Message[] = [];
    var fields: String = "";
    if (!this.imageAttributes.at(index).get('source').value)
      fields = "Source, ";
    if (!this.imageAttributes.controls[index].get('viewAngle').value)
      fields = fields + "View Angle, ";
    if (!this.imageAttributes.controls[index].get('background').value)
      fields = fields + "Back-ground, ";
    if (!this.imageAttributes.controls[index].get('providedSize').value)
      fields = fields + "Provided Size, ";
    if (!this.imageAttributes.controls[index].get('description').value)
      fields = fields + "Description, ";
    if (!(<HTMLInputElement>document.getElementById("fileName" + index)).value)
      fields = fields + "File Name, ";
    if (fields != "") {

      this.showError('Validation error',
      'Please enter the following mandatory fields: ' + fields, messageArray);
    }
    this.validateProvidedSize(index, messageArray);
    this.validateUploadUrl(index, messageArray);
    this.msgs.push(messageArray);
    if(messageArray.length > 0)
      flag = false;
    return flag;
  }

  private validateUploadUrl(index: number, messageArray: Message[]) {
    if (this.imageAttributes.at(index).get('source').value === 'imp-support-legacy-ds'
      && this.uploadedFiles[index] === null) {
      this.showError(CspConstants.ERROR_TYPE_VALIDATION, CspConstants.ERROR_SOURCE_LEGACY_VALIDATION, messageArray);
    } else if (this.imageAttributes.at(index).get('source').value === 'url'
      && !this.imageAttributes.at(index).get('url').value) {
      var src = this.imageAttributes.at(index).get('source').value;
      if (!src || src === null)
        src = '';
      this.showError(CspConstants.ERROR_TYPE_VALIDATION,
        'Please provide ' + src + ' URL', messageArray);
    }
  }

  private showError(type: string, description: string, messageArray: Message[]) {
    messageArray.push({
      severity: 'error', summary: type,
      detail: description
    });
  }

  private validateProvidedSize(index: number, messageArray: Message[]) {
    var re = new RegExp('^[0-9]+X[0-9]+$', 'i');
    if (!re.test(this.imageAttributes.controls[index].get('providedSize').value))
      this.showError(CspConstants.ERROR_TYPE_VALIDATION, CspConstants.ERROR_PROVIDED_SIZE_VALIDATION, messageArray);
  }

  private setVisibility(element: string, visibility: string) {
    document.getElementById(element).style.visibility = visibility;
  }

  private buildAddImageRequest(): ImageAddRequest {
    const IMAGE_TYPE: string = 'ProductImage';

    let identifier: AssetIdentifier;
    identifier = new AssetIdentifier();
    identifier.gtin = this.form.get('gtin').value;

    var request: ImageAddRequest = new ImageAddRequest();
    request.referenceId = "CSP-UI-ADD-"
      +(<User>this.authService.getUser()).username+'-' + new Date().getMilliseconds();
    request.creationDatetime = new Date().toISOString();
    request.imageType = IMAGE_TYPE;
    request.assetIdentifier = identifier;
    request.assetDetails = this.populateAssets();
    return request;
  }

  private populateAssets(): ImageObject[] {
    let images: ImageObject[] = [];
    for (var index = 0; index < this.imageAttributes.length; index++)
      images.push(this.readImageAttributes(index));
    return images;
  }

  private readImageAttributes(index: number): ImageObject {
    let image: ImageObject;
    image = new ImageObject();
    image.sequence = index + 1;
    image.viewAngle = this.imageAttributes.controls[index].get('viewAngle').value;
    image.providedSize = this.imageAttributes.controls[index].get('providedSize').value;
    image.background = this.imageAttributes.controls[index].get('background').value;
    image.source = this.imageAttributes.controls[index].get('source').value;
    image.description = this.imageAttributes.controls[index].get('description').value;
    if (this.imageAttributes.at(index).get('source').value === 'imp-support-legacy-ds'
      && this.uploadedFiles[index] != null) {
      this.handleFile(index, image);
    } else if (this.imageAttributes.at(index).get('source').value != 'imp-support-legacy-ds') {
      this.handleUrl(index, image);
    }
    return image;
  }

  private handleFile(index: number, image: ImageObject) {
    image.lastModifiedDate = this.uploadedFiles[index].lastModifiedDate.toISOString();
    image.fileName = this.uploadedFiles[index].name;
    image.fileExtension = this.uploadedFiles[index].type.substring(
      this.uploadedFiles[index].type.indexOf("/") + 1,
      this.uploadedFiles[index].type.length);
    image.assetType = AssetType.ASSETTYPEBASE64ENCODED;
    image.asset = this.fileStrings[index];
    console.log(image.asset);
  }

  private handleUrl(index: number, image: ImageObject) {
    image.lastModifiedDate = new Date().toISOString();
    image.fileName = (<HTMLInputElement>document.getElementById("fileName"+index)).value;
    image.fileExtension = this.getFileExtension(image.fileName);
    image.asset = this.imageAttributes.at(index).get('url').value;
    let source: string = this.imageAttributes.controls[index].get('source').value;
    if (source === 'kwikee')
      image.assetType = AssetType.KWIKEEAPM;
    if (source === 'gladson')
      image.assetType = AssetType.GLADSONAPM;
    if (source === 'url')
      image.assetType = AssetType.ASSETTYPEURL;

  }

  private getFileExtension(fileName: string): string {
    let array: string[];
    array = fileName.split('.');
    if (array.length > 1) {
      return array[array.length - 1];
    } else
      return '.jpeg';
  }
}
