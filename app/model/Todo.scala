/**
 *
 * to do sample project
 *
 */
package model
import lib.model.Todo._
import controllers.TodoForm

// Todo ページのviewvalue
case class ViewValueTodo(
                          title:              String,
                          cssSrc:             Seq[String],
                          jsSrc:              Seq[String],
                          categoryOptionList: Seq[(String, String)],
                          todoList:           Seq[TodoListObj]
                        ) extends ViewValueCommon

// TODO表示用モデル
case class TodoListObj(
    id:       Id,
    category: TodoCategory,
    title:    String,
    body:     String,
    state:    TodoStatus
               )