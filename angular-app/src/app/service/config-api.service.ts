import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { ConfigListData } from '../model/config';

import {Observable, of, throwError} from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class ConfigApiService {
  private todoUrl = 'http://localhost:9000/api/config';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  }

  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Todo サーバ問い合わせ一覧取得
   */
  getState(): Observable<ConfigListData[]> {
    // @ts-ignore
    return this.http.get<ConfigListData[]>(this.todoUrl + "/state" , this.httpOptions )
      .pipe(catchError(this.handleError));
  }


  /**
   * Todo サーバ問い合わせ一覧取得
   */
  getColor(): Observable<ConfigListData[]> {
    // @ts-ignore
    return this.http.get<ConfigListData[]>(this.todoUrl+ "/color" , this.httpOptions )
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }
}
