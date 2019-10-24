import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {VendorResponse} from "./model/vendor-response";

@Injectable({
  providedIn: 'root'
})
export class CspVendorService {

  constructor(private http: HttpClient) { }

  getImages(endpoint: string, vendor: string, gtin: string ) {
    let url: string = endpoint + '?vendorSource='+vendor.toUpperCase().trim()+'&gtin='+gtin;
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
