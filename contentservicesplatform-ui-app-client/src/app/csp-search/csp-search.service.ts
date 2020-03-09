import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CspSearchResponse} from "../model/cspSearchResponse";
import {AuthService, User} from "kroger-ng-oauth2";

@Injectable({
  providedIn: 'root'
})
export class CspSearchService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  getImages(endpoint: string, imageId: string, gtin: string ) {
    var param;
    if (imageId)
      param = '&imageId=' + imageId;
    if (gtin)
      param = param + '&gtin=' + gtin;
    return this.http
      .get<any>(endpoint +
        '?referenceId=DAP-UI-'+(<User>this.authService.getUser()).username+'-'+new Date().getMilliseconds()+param)
      .toPromise()
      .then(res => <CspSearchResponse>res)
      .then(data => {
        return data;
      });
  }

}
