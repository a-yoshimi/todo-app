import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { TodoApiService } from '../../service/todo-api.service';
import { Todo } from '../../model/Todo';
import { Observable, of } from 'rxjs';
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})

export class TodoListComponent implements OnInit {

  constructor(
    private todoService: TodoApiService,
  public changeDetectorRef: ChangeDetectorRef
  ) { }

  todoList: Todo[] = [];

  ngOnInit(): void {
    console.log('ngOnInit')
    // 一覧の取得
    this.refresh()
  }
  /**
   * ビューの初期化をフックする
   *
   * @memberof ViewChildComponent
   */
  ngAfterViewInit() {
    console.log('[ViewChildComponent][ngAfterViewInit] fired.');
  }

  refresh(): void {
    console.log('refresh')
    // 一覧の取得
    console.log( this.todoService)
    this.todoService.getAll().subscribe(todos => this.todoList = todos);
  }

}

