import {Component, ElementRef, OnInit} from '@angular/core';
import {Message} from 'primeng/components/common/api';
import {FormBuilder, FormGroup} from "@angular/forms";
import {CspSearchService} from "./csp-search.service";
import {Image} from "../model/image";
import {SelectItem} from "primeng/api";
import {CspErrorDetails} from "../model/cspErrorDetails";
import {DomSanitizer} from "@angular/platform-browser";
import {UtilService} from "../util/util.service";

@Component({
  selector: 'app-csp-search',
  templateUrl: './csp-search.component.html',
  styleUrls: ['./csp-search.component.less'],
  providers: [UtilService]
})
export class CspSearchComponent implements OnInit {

  msgs: Message[] = [];
  form: FormGroup;
  images: Image[] = [];
  error: CspErrorDetails;
  div_visible: boolean = false;
  selectedImage: Image;
  filterOptions: SelectItem[];
  sortOptions: SelectItem[];
  sortKey: string;
  sortField: string;
  sortOrder: number;
  displayDialog: boolean = false;
  filterField: string;
  retrievalUrl: string;

  constructor(private _formBuilder: FormBuilder, private el: ElementRef,
              public searchService: CspSearchService, public sanitizer: DomSanitizer,
              public utilService: UtilService) {
  }

  ngOnInit() {
    this.utilService.getEndpoint('retrieval').then(endpoint => {
      this.retrievalUrl = endpoint;
    });
    this.buildForm();
    this.populateSortOptions();
    this.populateFilterOptions();
  }

  private populateSortOptions(): void {
    this.sortOptions = [
      {label: 'Newest First', value: '!lastModifiedDate'},
      {label: 'Oldest First', value: 'lastModifiedDate'},
      {label: 'Image ID', value: 'imageId'}
    ];
  }

  private populateFilterOptions(): void {
    this.filterOptions = [
      {label: 'View Angle', value: 'viewAngle'},
      {label: 'Resolution DPI', value: 'resDpi'},
      {label: 'Description', value: 'description'},
      {label: 'Provided Size', value: 'providedSize'},
      {label: 'Source', value: 'source'}
    ];
  }

  private buildForm(): void {
    this.form = this._formBuilder.group({
      gtin: [''],
      imageId: ['']
    });
  }

  validate(): boolean {
    this.msgs = [];
    if (!this.form.get('imageId').value && !this.form.get('gtin').value) {
      this.showError('Validation error',
        'At least one of the following should be present: Image ID or GTIN');
      return true;
    }
    if (this.form.get('gtin').value && this.form.get('gtin').value.length != 14) {
      this.showError('Validation error',
        'GTIN should be 14 digits long ');
      return true;
    }
    if (this.form.get('gtin').value && isNaN(this.form.get('gtin').value)) {
      this.showError('Validation error',
        'GTIN should be in digits ');
      return true;
    }
    return false;
  }

  getImages() {
    if (!this.validate()) {
      (<HTMLInputElement>document.getElementById("search")).disabled = true;
      this.form.disable();
      this.div_visible = true;
      this.utilService.getEndpoint('search').then(endpoint => this.search(endpoint))
    }
  }


  reset() {
    this.msgs = [];
    this.form.reset();
    this.form.enable();
    this.div_visible = false;
    (<HTMLInputElement>document.getElementById("search")).disabled = false;
    document.getElementById("result").style.visibility = "hidden";
    document.getElementById("filterText").style.visibility = "hidden";
  }

  selectImage(event: Event, image: Image) {
    this.selectedImage = image;
    this.displayDialog = true;
    event.preventDefault();
  }

  onSortChange(event) {
    let value = event.value;
    if (value.indexOf('!') === 0) {
      this.sortOrder = -1;
      this.sortField = value.substring(1, value.length);
    }
    else {
      this.sortOrder = 1;
      this.sortField = value;
    }
  }

  onFilterChange(event) {
    document.getElementById("filterText").style.visibility = "visible";
    this.filterField = event.value;
  }

  onDialogHide() {
    this.selectedImage = null;
  }

  showError(type: string, description: string) {
    this.msgs = [];
    this.msgs.push({
      severity: 'error', summary: type,
      detail: description
    });
  }

  private search(endpoint: string): void{
    this.searchService.getImages(endpoint, this.form.get('imageId').value, this.form.get('gtin').value)
      .then(data => {
        this.images = data.images;
        this.error = data.error;
        if (!this.error) {
          document.getElementById("result").style.visibility = "visible";
        }
        else {
          this.showError('System error', this.error.errorCode + ': ' + this.error.errorDescription);
        }
        this.div_visible = false;
      })
      .catch(reason => {
        this.showError('System error',
          'Could not connect to the backend. Please try again later.');
        this.div_visible = false;
      });
  }

}
