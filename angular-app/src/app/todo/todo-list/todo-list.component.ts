import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { TodoApiService } from '../../service/todo-api.service';
import { Input, Output, EventEmitter } from '@angular/core';
import { Category }  from "../../model/category";
import { Todo } from "../../model/Todo";
import {ConfigListData} from "../../model/config";
import {CategoryApiService} from "../../service/category-api.service";

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


  /**
   * 親コンポーネントから値を受け取るプロパティ
   */
  @Input() categoryList: Category[]   = [];
  @Input() stateList:ConfigListData[] = []


  /**
   * 親コンポーネントに対してイベントを発火するためのプロパティ
   */
  @Output() event = new EventEmitter<Todo>();
  @Output() deleteEvent = new EventEmitter<Todo>();

  /**
   * 一覧データ
   */
  todoList: Todo[] = [];

  /**
   * データ不正カテゴリデータ
   */
  initializedCategoryData: Category = {color: 0, id: 0, name: "カテゴリなし", slug: "initialized"}

  /**
   * 初期処理
   */
  ngOnInit(): void {
    // 一覧の取得
    this.refresh()
  }

  refresh(): void {
    // 一覧の取得
    this.todoService.getAll().subscribe(todos => this.todoList = todos);
  }


  /**
   * イベントハンドラ
   * 更新ボタンクリック時のイベントをキャッチして親コンポーネントへのイベントを発火する
   *
   * @memberof CompChildComponent
   */
  onClickUpdate(id: number) {
    let updateData:Todo | undefined = this.todoList.find(todo => todo.id === id)
    this.event.emit(updateData);
  }

  /**
   * 削除
   * @param id
   */
  onClickDelete(id: number) {
    let deleteData:Todo | undefined = this.todoList.find(todo => todo.id === id)
    this.deleteEvent.emit(deleteData);
  }

  /** 一覧表示の変換用関数 **/
  convertToCategory(id: number): Category {
    let category:Category | undefined= this.categoryList.find(element => element.id === id);
    return (category != undefined) ? category : this.initializedCategoryData ;
  }

  convertToState(code: string): string {
    let state:ConfigListData | undefined= this.stateList.find(element => element.code === code);
    return (state != undefined) ? state.name : "エラー";
  }


}

