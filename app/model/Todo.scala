/**
 *
 * to do sample project
 *
 */
package model
import lib.model.Todo._
// TODO表示用モデル
case class Todo(
    id:       Id,
    category: String,
    title:    String,
    body:     String,
    state:    TodoStatus
               )