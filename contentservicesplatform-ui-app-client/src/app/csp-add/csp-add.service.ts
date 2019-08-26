import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
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
    return  this.http.post(EndPoints.ADD_ENDPOINT, request);
  }
}
