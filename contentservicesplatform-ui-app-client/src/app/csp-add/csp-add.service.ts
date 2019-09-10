import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ImageAddRequest} from "./model/imageAddRequest";
import {EndPoints} from "../configuration/endPoints";

@Injectable({
  providedIn: 'root'
})
export class CspAddService {

  constructor(private http: HttpClient) { }

  public addImages(request: ImageAddRequest){
    console.log('****requested data is***');
    console.log(request);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };
    return  this.http.post(EndPoints.ADD_ENDPOINT, request, httpOptions);
  }
}
