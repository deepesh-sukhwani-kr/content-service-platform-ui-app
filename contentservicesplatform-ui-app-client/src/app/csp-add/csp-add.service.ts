import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ImageAddRequest} from "./model/imageAddRequest";

@Injectable({
  providedIn: 'root'
})
export class CspAddService {

  constructor(private http: HttpClient) { }

  public addImages(request: ImageAddRequest, endpoint: string){
    console.log("ADD endpoint is: "+ endpoint);
    console.log('****requested data is***');
    console.log(request);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };

    return  this.http.post(endpoint, request, httpOptions);
  }
}
