import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ImageAddRequest} from "./model/imageAddRequest";
import {AddImageResponse} from "./model/addImageResponse";
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
    this.http.post('/imp/ui/v1/server/addMultipleImages', request)
      .subscribe(value => console.log('test post: ' + value));
    return  this.http.post(EndPoints.ADD_ENDPOINT, request, httpOptions);
  }
}
