import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EndpointResponse} from "./model/endpoint-response";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private http: HttpClient) { }

  public getViewAngels(){
    return this.http.get('/imp/ui/v1/server/viewAngles');
  }

  public getEndpoint(api: string){
    return this.http
      .get<any>('/imp/ui/v1/server/endpoints')
      .toPromise()
      .then(res => <EndpointResponse>res)
      .then(data => {
        return data.add;
      });
  }
}
