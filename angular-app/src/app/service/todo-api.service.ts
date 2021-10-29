import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { Todo } from '../model/Todo';

import {Observable, of, throwError} from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TodoApiService {

  private todoUrl = 'http://localhost:9000/api/todo';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  }

  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Todo 一覧取得
   */
  getAll(): Observable<Todo[]> {
    // @ts-ignore
    return this.http.get<Todo[]>(this.todoUrl, this.httpOptions )
      .pipe(tap(todos => console.log('load all todo', todos)))
      .pipe(catchError(this.handleError));
  }

  add(todo: Todo): Observable<Todo> {
    return this.http.post<Todo>(this.todoUrl + "/add", todo, this.httpOptions);
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
