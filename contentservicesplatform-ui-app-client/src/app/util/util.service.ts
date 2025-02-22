import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EndpointResponse} from "./model/endpoint-response";
import {AuthService, User} from "kroger-ng-oauth2";
import {AuthorizedRolesResponse} from "./model/authorized-roles-response";
import {CspSearchResponse} from "../model/cspSearchResponse";
import {RbacConfig} from "./model/rbac-config";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  authorizedRoles: string[];

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getViewAngels(){
    return this.http.get('/imp/ui/server/viewAngles');
  }

  public getEndpoint(api: string){
    return this.http
      .get<any>('/imp/ui/server/endpoints')
      .toPromise()
      .then(res => <EndpointResponse>res)
      .then(data => {
        if('add'.toLowerCase() == api.toLowerCase().trim())
          return data.add;
        if('search'.toLowerCase() == api.toLowerCase().trim())
          return data.search;
        if('vendorSearch'.toLowerCase() == api.toLowerCase().trim())
          return data.vendorSearch;
        if('retrieval'.toLowerCase() == api.toLowerCase().trim())
          return data.retrieval;
      });
  }

  public isAuthorizedToAddImage() {
    return this.http.get('/imp/ui/server/addImageRoles').toPromise().then(responseData => {
      let response = <AuthorizedRolesResponse>responseData;
      this.authorizedRoles = [];
      response.addImageAuthorizedRoles.forEach( data => {
        this.authorizedRoles.push(data as string)
      });
      return this.authorizedRoles;
    });
  }

  public getRbacConfig(){
    return this.http
      .get<any>('/imp/ui/server/rbacConfig')
      .toPromise()
      .then(res => <RbacConfig>res)
      .then(data => {
        return data;
      });
  }
}
