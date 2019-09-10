import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CspSearchResponse} from "../model/cspSearchResponse";
import {EndPoints} from "../configuration/endPoints";
import {AuthService, User} from "kroger-ng-oauth2";

@Injectable({
  providedIn: 'root'
})
export class CspSearchService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  getImages(imageId: string, gtin: string ) {
    var param;
    if (imageId)
      param = '&imageId=' + imageId;
    if (gtin)
      param = param + '&gtin=' + gtin;
    return this.http
      .get<any>(EndPoints.SEARCH_ENDPOINT +
        '?referenceId=CSP-Search-'+(<User>this.authService.getUser()).username+'-'+new Date().getMilliseconds()+param)
      .toPromise()
      .then(res => <CspSearchResponse>res)
      .then(data => {
        return data;
      });
  }

}
