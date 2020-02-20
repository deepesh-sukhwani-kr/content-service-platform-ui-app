import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EndpointService {

  constructor(private http: HttpClient) { }

  public getEndpoint(api: string){
    return this.http
      .get<any>('/imp/ui/v1/server/endpoints')
      .toPromise()
      .then(res => <Map<String, String>>res)
      .then(data => {
        return data.get(api.toLowerCase().trim());
      });
  }
}
