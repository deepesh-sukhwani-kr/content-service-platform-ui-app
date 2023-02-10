import {Component, OnInit} from '@angular/core';
import {CspVendorService} from "./csp-vendor.service";
import {VendorAsset} from "./model/vendor-asset";
import {FormBuilder, FormGroup} from "@angular/forms";
import {CspAddService} from "../csp-add/csp-add.service";
import {AuthService, User} from "kroger-ng-oauth2";
import {ImageObject} from "../csp-add/model/ImageObject";
import {AssetType} from "../csp-add/constants/assetType";
import {AssetIdentifier} from "../csp-add/model/AssetIdentifier";
import {ImageAddRequest} from "../csp-add/model/imageAddRequest";
import {AddImageResponse} from "../csp-add/model/addImageResponse";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {AssetDetailResponse} from "../csp-add/model/assetDetailResponse";
import {Message} from "primeng/components/common/api";
import {UtilService} from "../util/util.service";
import {RawAsset} from "./model/raw-asset";
import * as FileSaver from "file-saver";

@Component({
  selector: 'app-csp-vendor',
  templateUrl: './csp-vendor.component.html',
  styleUrls: ['./csp-vendor.component.less'],
  providers: [CspAddService, UtilService]
})
export class CspVendorComponent implements OnInit {

  constructor(private _formBuilder: FormBuilder, public vendorService: CspVendorService,
              private addService: CspAddService, private authService: AuthService,
              private router: Router, private utilService: UtilService, private http: HttpClient) {
  }

  vendor: string = 'Syndigo';
  selectedValues: string = "Front";
  form: FormGroup;
  assetForm: FormGroup
  vendorAssets: VendorAsset[];
  div_visible: boolean = false;
  displayDialog: boolean = false;
  disableAddButton: boolean = true;
  showGladson: boolean = true;
  selectedAsset: VendorAsset;
  processed: boolean = false;
  msgs: Message[];
  errMsgs: Message[];
  description: string = '';

  ngOnInit() {
    this.form = this._formBuilder.group({
      gtin: ['']
    });
    this.assetForm = this._formBuilder.group({
      description: [''],
      background: [''],
      providedSize: [''],
      filename: ['']
    });
    this.utilService.getRbacConfig().then(rbac => {
      if (rbac.checkRbac) {
        rbac.vendorAddRoles.forEach(role => {
          if (this.authService.hasRole(role))
            this.disableAddButton = false;
        });
        rbac.krogerExternalRoles.forEach(role => {
          if (this.authService.hasRole(role))
            this.showGladson = false;
        });
      }
    });
  }

  getImages() {
    this.vendorAssets = [];
    if (!this.validate()) {
      console.log('called search');
      this.div_visible = true;
      let id: string = this.form.get('gtin').value.trim();
      this.utilService.getEndpoint('vendorSearch').then(endpoint => {
        this.vendorService.getImages(endpoint, this.vendor, id)
          .then(data => this.searchVendor(id, endpoint));
      });
    }
  }

  onDialogHide() {
    this.selectedAsset = null;
  }

  selectImage(event: Event, vendorAsset: VendorAsset) {
    this.assetForm.reset();
    this.selectedAsset = vendorAsset;
    this.displayDialog = true;
    event.preventDefault();
  }

  upload() {
    this.processed = true;
    this.msgs = [];
    this.utilService.getEndpoint('add').then(endpoint => {
      this.addService.addImages(this.buildAddImageRequest(), endpoint)
        .subscribe(successResponse => this.handleSuccess(successResponse),
          failureResponse => this.handleFailure(failureResponse));
    });
  }

  private handleSuccess(response: Object) {
    let res = <AddImageResponse>response;
    console.log(res);
    if (res != null && res.assetDetails != null && res.assetDetails[0]) {
      if (this.isSequenceSuccess(res.assetDetails[0])) {
        this.selectedAsset.imageId = res.assetDetails[0].imageId;
        this.utilService.getEndpoint('retrieval').then(endpoint => {
          this.selectedAsset.cspurl = endpoint + this.selectedAsset.imageId;
        });
        this.msgs.push({
          severity: 'success', summary: 'SUCCESS',
          detail: res.assetDetails[0].statusCode + ' - ' + res.assetDetails[0].statusMessage
        });
      } else {
        this.msgs.push({
          severity: 'error', summary: 'FAILURE',
          detail: res.assetDetails[0].statusCode + ' - ' + res.assetDetails[0].errorDetails
        });
      }
    } else if (res.errorResponse != null) {
      this.msgs.push({
        severity: 'error', summary: 'FAILURE',
        detail: res.errorResponse.statusCode + ' - ' + res.errorResponse.statusMessage
      });
    }
    this.processed = false;
    this.selectedAsset.isProcessed = true;
  }

  private isSequenceSuccess(sequence: AssetDetailResponse) {
    if (!sequence)
      return false;
    return sequence.statusMessage === 'SUCCESS' || sequence.statusMessage === 'SUCCESSWITHDUPLICATES' ||
      sequence.statusMessage === 'FAILUREWITHDUPLICATES';
  }


  private handleFailure(response: Object) {
    console.log(response);
    if ((<HttpErrorResponse>response).url)
      this.router.navigate(['/login']);
    this.processed = false;
  }

  private buildAddImageRequest(): ImageAddRequest {
    const IMAGE_TYPE: string = 'ProductImage';

    let identifier: AssetIdentifier;
    identifier = new AssetIdentifier();
    identifier.gtin = this.selectedAsset.gtin;

    var request: ImageAddRequest = new ImageAddRequest();
    request.referenceId = 'DAP-UI-VENDOR-SEARCH-UPLOAD-' +
      (<User>this.authService.getUser()).username + '-' + new Date().getMilliseconds()
      + "-" + identifier.gtin;
    request.creationDatetime = new Date().toISOString();
    request.imageType = 'ProductImage';
    request.assetIdentifier = identifier;
    request.assetDetails = this.buildAssets();
    return request;
  }

  private buildAssets(): ImageObject[] {
    let images: ImageObject[] = [];
    let image: ImageObject;
    image = new ImageObject();
    image.sequence = 1;
    image.viewAngle = this.selectedAsset.viewAngle;
    image.providedSize = this.selectedAsset.providedSize;
    image.background = this.selectedAsset.background;
    image.description = this.selectedAsset.description;
    image.source = 'SYNDIGO-CURATED';
    image.lastModifiedDate = this.selectedAsset.lastModifiedDate;
    image.fileName = this.selectedAsset.filename;
    image.fileExtension = this.getFileExtension(image.fileName);
    let source: string = this.vendor.toLowerCase().trim();
    if (source === 'syndigo')
      image.assetType = AssetType.SYNDIGOAPM;
    image.asset = this.selectedAsset.url;
    images.push(image);
    return images;
  }

  private getFileExtension(fileName: string): string {
    let array: string[];
    array = fileName.split('.');
    if (array.length > 1) {
      return array[array.length - 1];
    } else
      return '.jpeg';
  }

  private validate(): boolean {
    this.errMsgs = [];
    if (this.form.get('gtin').value && this.form.get('gtin').value.trim().length != 14) {
      this.showError('Validation error',
        'GTIN should be 14 digits long ');
      return true;
    }
    if (this.form.get('gtin').value && isNaN(this.form.get('gtin').value.trim())) {
      this.showError('Validation error',
        'GTIN should be in digits ');
      return true;
    }
    return false;
  }

  private showError(type: string, description: string) {
    this.errMsgs = [];
    this.errMsgs.push({
      severity: 'error', summary: type,
      detail: description
    });
  }

  private searchVendor(id: string, endpoint: string): void {
    this.vendorService.getImages(endpoint, this.vendor, id)
      .then(data => {
        this.vendorAssets = data.viewAngleList;
        this.div_visible = false;
        this.description = data.description;
        this.vendorAssets.forEach(asset => {
          asset.lastModifiedDate = (new Date()).toISOString();
          asset.gtin = data.gtin;
          asset.description = data.description;
          asset.providedSize = data.providedSize;
          asset.background = data.background;
          asset.filename = data.gtin + '_' + asset.viewAngle + '.jpeg';
          asset.isProcessed = false;
        })
      })
      .catch(reason => {
        console.log(reason.toString());
        this.div_visible = false;
      });
  }

  public clear(): void {
    this.vendorAssets = [];
    this.form.get('gtin').reset();
    this.errMsgs = [];
  }

  public getRawImage(asset: VendorAsset) {
    let request: RawAsset;
    request = new RawAsset();
    request.url = asset.url;
    console.log(request.url);
    request.vendor = this.vendor;
    const httpOptions: {
      headers?: HttpHeaders | {
        [header: string]: string | string[];
      };
      observe: 'response';
      params?: HttpParams | {
        [param: string]: string | string[];
      };
      reportProgress?: boolean;
      responseType: 'blob';
      withCredentials?: boolean;
    } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'image/jpeg'
      }),
      observe: 'response',
      responseType: 'blob'
    };
    this.http.post('/imp/ui/v1/server/getAssset', request, httpOptions).subscribe((data) => {

      let blob: Blob;
      blob = new Blob([data.body], {type: 'image/jpeg'});
      FileSaver.saveAs(blob, asset.gtin + '_' + asset.viewAngle + '.jpeg');
    });
  }
}
