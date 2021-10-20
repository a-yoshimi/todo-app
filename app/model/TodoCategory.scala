/**
 *
 * to do sample project
 *
 */
package model
import lib.model.TodoCategory._

// 画面表示用モデル
case class TodoCategory(
    id: Id,
    name:  String,
    color: Int
                       )
