import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class HomeService {

  constructor(private http: HttpClient) { }

  me(): Observable<any> {
    return this.http.get('/api/me', {});
  }

  break(): Observable<any> {
    // This breaks because you cannot POST to the info endpoint
    return this.http.post('/manage/info', {});
  }
}
