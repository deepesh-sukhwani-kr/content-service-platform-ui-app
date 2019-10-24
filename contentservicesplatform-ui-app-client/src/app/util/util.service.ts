import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EndpointResponse} from "./model/endpoint-response";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private http: HttpClient) { }

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
}
