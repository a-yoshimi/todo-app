import {Component, OnInit, ViewChild} from '@angular/core';

import { FormBuilder } from '@angular/forms';
import { TodoApiService } from '../service/todo-api.service';
import { Todo } from '../model/Todo';

import { ConfigApiService } from '../service/config-api.service';
import { ConfigListData } from "../model/config";

import { CategoryApiService } from '../service/category-api.service'
import { Category} from "../model/category";
import {TodoListComponent} from "./todo-list/todo-list.component";
@Component({
  selector: 'app-todo',
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.css']
})



export class TodoComponent implements OnInit {

  todoForm = this.formBuilder.group({
    id:         ['0'],
    categoryId: ['1'],
    title:      [''],
    body:       [''],
    state:      [{value: '0', disabled: false}]
    // state:      [{value: '0', disabled: true}]
  });

  constructor(
    private formBuilder: FormBuilder,
    private todoService: TodoApiService,
    private configService: ConfigApiService,
    private categoryService: CategoryApiService,
  ) { }

  // Todo-List コンポーネント
  @ViewChild(TodoListComponent)
  public todolist!: TodoListComponent;
  stateList : ConfigListData[]  = []
  colorList : ConfigListData[]  = []
  categoryList: Category[]       = [];

  ngOnInit(): void {
    // Form.selectに設定するの取得
    this.configService.getState().subscribe(state => this.stateList = state);
    this.configService.getColor().subscribe(color => this.colorList = color);
    this.categoryService.getAll().subscribe(category => this.categoryList = category);

  }

  onSubmit(): void {
    let todoData: Todo =
      {
        id: Number(this.todoForm.get('id')?.value),
        categoryId: Number(this.todoForm.get('categoryId')?.value),
        title: this.todoForm.get('title')?.value,
        body: this.todoForm.get('body')?.value,
        state: this.todoForm.get('state')?.value
      }
      console.log(todoData.id)
      if (todoData.id === 0 )
      {
        this.todoService.add(todoData).subscribe(todo => {
          this.todolist.refresh()
          this.resetTodoForm()
        });
      } else{
        this.todoService.update(todoData).subscribe(todo => {
          this.todolist.refresh()
          this.resetTodoForm()
        });
      }


  }

  resetTodoForm(){
    this.setTodoForm('0', '1','','','0',false)
    }

  /**
   * イベントハンドラ
   * listコンポーネントから発火されたイベントをキャッチして文字列を受け取る
   *
   * @param {String} eventData 子コンポーネントから渡される文字列
   * @memberof CompParentComponent
   */
  onClickUpdate(eventData: Todo) {

    this.setTodoForm(
      eventData.id.toString(),
      eventData.categoryId.toString(),
      eventData.title,
      eventData.body,
      eventData.state.toString(),
      false
    )
    console.log(this.todoForm)
  }

  /**
   * イベントハンドラ
   * listコンポーネントから発火されたイベントをキャッチして文字列を受け取る
   *
   * @param {String} eventData 子コンポーネントから渡される文字列
   * @memberof CompParentComponent
   */
  onClickDelete(eventData: Todo) {
    this.todoService.delete(eventData.id).subscribe(todo => {
      this.todolist.refresh()
      this.resetTodoForm()
    });
  }

  private setTodoForm(id: string, categoryId: string, title: string, body: string, state: string, stateUnenable: boolean){
    this.todoForm = this.formBuilder.group({
      id:         [id],
      categoryId: [categoryId],
      title:      [title],
      body:       [body],
      state:      [{value: state, disabled: stateUnenable}]
    });
  }

}
