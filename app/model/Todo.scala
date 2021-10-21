/**
 *
 * to do sample project
 *
 */
package model
import lib.model.Todo._
import controllers.TodoForm

// Todo 一覧表示ページのviewvalue
case class ViewValueTodo(
                          title:        String,
                          cssSrc:       Seq[String],
                          jsSrc:        Seq[String],
                          categoryList: Seq[TodoCategory],
                          todoList:     Seq[TodoListObj]
                        ) extends ViewValueCommon

// Todo 新規追加ページのviewvalue
case class ViewValueTodoAdd(
                          title:  String,
                          cssSrc: Seq[String],
                          jsSrc:  Seq[String]
                        ) extends ViewValueCommon


// TODO表示用モデル
case class TodoListObj(
    id:       Id,
    category: TodoCategory,
    title:    String,
    body:     String,
    state:    TodoStatus
               )