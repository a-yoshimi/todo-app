import { Component, OnInit } from '@angular/core';
import { TodoApiServiceService } from '../../service/todo-api-service.service';
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
    private todoService: TodoApiServiceService
  ) { }

  todoList: Todo[] = [];

  ngOnInit(): void {

    // 一覧の取得
    this.todoService.getAll().subscribe(todos => this.todoList = todos);
  }

}

