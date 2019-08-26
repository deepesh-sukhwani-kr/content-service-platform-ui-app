import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Image} from "../model/image";
import {CspSearchResponse} from "../model/cspSearchResponse";
import {EndPoints} from "../configuration/endPoints";

@Injectable({
  providedIn: 'root'
})
export class CspSearchService {

  constructor(private http: HttpClient) {
  }

  getImages(imageId: string, gtin: string ) {
    var param;
    if (imageId)
      param = '&imageId=' + imageId;
    if (gtin)
      param = param + '&gtin=' + gtin;
    return this.http
      .get<any>(EndPoints.SEARCH_ENDPOINT +
        '?referenceId=CSP-Search-'+new Date().getMilliseconds()+param)
      .toPromise()
      .then(res => <CspSearchResponse>res)
      .then(data => {
        return data;
      });
  }

}
