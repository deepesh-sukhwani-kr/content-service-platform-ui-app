import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService, User} from "kroger-ng-oauth2";
import {EndPoints} from "../configuration/endPoints";
import {VendorResponse} from "./model/vendor-response";

@Injectable({
  providedIn: 'root'
})
export class CspVendorService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  getImages(vendor: string, gtin: string ) {
    let url: string = EndPoints.VENDOR_SEARCH_ENDPOINT +
      '?vendorSource='+vendor.toUpperCase().trim()+'&gtin='+gtin;
    console.log("Hitting : "+url);
    return this.http
      .get<VendorResponse>(url)
      .toPromise()
      .then(res => <VendorResponse>res)
      .then(data => {
        return data;
      });
  }
}
