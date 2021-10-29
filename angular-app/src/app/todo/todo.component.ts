import {Component, OnInit, ViewChild} from '@angular/core';

import { FormBuilder } from '@angular/forms';
import { TodoApiService } from '../service/todo-api.service';
import { Todo } from '../model/Todo';
import {TodoListComponent} from "./todo-list/todo-list.component";

@Component({
  selector: 'app-todo',
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.css']
})



export class TodoComponent implements OnInit {

  todoForm = this.formBuilder.group({
    id:         [''],
    categoryId: ['1'],
    title:      [''],
    body:       [''],
    state:      [{value: '0', disabled: true}]
  });

  constructor(
    private formBuilder: FormBuilder,
    private todoService: TodoApiService,
  ) { }

  // Todo-List コンポーネント
  @ViewChild(TodoListComponent)
  public todolist!: TodoListComponent;

  ngOnInit(): void {}

  onSubmit(): void {
    // Process checkout data here
    console.log("add or update ")
    let todoData: Todo =
      {
        id: 0,
        categoryId: Number(this.todoForm.get('categoryId')?.value),
        title: this.todoForm.get('title')?.value,
        body: this.todoForm.get('body')?.value,
        state: this.todoForm.get('state')?.value
      }

    this.todoService
      .add(todoData)
      .subscribe(todo =>
  {this.todolist.refresh()
        this.resetTodoForm()});

  }

  resetTodoForm(){
    this.todoForm.setValue(
      {
        id: 0,
        categoryId: ['1'],
        title:      [''],
        body:       [''],
        state:      [{value: '0', disabled: true}]
      }
    )
  }


}
