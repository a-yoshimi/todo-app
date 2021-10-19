/**
 *
 * to do sample project
 *
 */
package model
import lib.model.ToDo._
// TODO表示用モデル
case class ToDo(
                 id:  Int,
                 categoryId: Int,
                 title:  String,
                 body: String,
                 state:  Status,
               )
