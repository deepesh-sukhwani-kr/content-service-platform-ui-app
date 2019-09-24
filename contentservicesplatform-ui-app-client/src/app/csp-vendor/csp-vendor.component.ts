import {Component, OnInit} from '@angular/core';
import {CspVendorService} from "./csp-vendor.service";
import {VendorAsset} from "./model/vendor-asset";
import {FormBuilder, FormControlName, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-csp-vendor',
  templateUrl: './csp-vendor.component.html',
  styleUrls: ['./csp-vendor.component.less']
})
export class CspVendorComponent implements OnInit {

  constructor(private _formBuilder: FormBuilder, public vendorService: CspVendorService) { }

  vendor: string = 'Kwikee';
  selectedValues: string = "Front";
  form: FormGroup;
  vendorAssets: VendorAsset[];
  div_visible: boolean = false;

  ngOnInit() {
    this.form = this._formBuilder.group({
      gtin: [''],
    });
  }

  getImages(){
    console.log('called search');
    this.div_visible = true;
    let id: string = this.form.get('gtin').value;
    this.vendorService.getImages(this.vendor,id)
      .then(data => {
        this.vendorAssets = data.viewAngleList;
        //this.vendorAssets.forEach(value => console.log(value.viewAngle + '-' + value.url));
        this.div_visible = false;
      })
      .catch(reason => {
        console.log(reason.toString());
        this.div_visible = false;
      });
  }

}
