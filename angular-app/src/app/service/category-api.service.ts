import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { Todo } from '../model/Todo';

import {Observable, of, throwError} from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import {Category} from "../model/category";
@Injectable({
  providedIn: 'root'
})
export class CategoryApiService {

  private todoUrl = 'http://localhost:9000/api/category';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  }

  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Todo 一覧取得
   */
  getAll(): Observable<Category[]> {
    // @ts-ignore
    return this.http.get<Category[]>(this.todoUrl, this.httpOptions )
      .pipe(catchError(this.handleError));
  }

  add(category: Category): Observable<Category> {
    return this.http.post<Category>(this.todoUrl + "/add", category, this.httpOptions);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    return throwError(
      'Something bad happened; please try again later.');
  }

}
