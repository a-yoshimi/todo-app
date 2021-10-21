/**
 * This is a sample of Todo Application.
 *
 */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import Todo._
case class Todo(
                 id:         Option[Id],
                 categoryId: TodoCategory.Id,
                 title:      String,
                 body:       String,
                 state:      TodoStatus,
                 updatedAt:  LocalDateTime = NOW,
                 createdAt:  LocalDateTime = NOW
               ) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object Todo {

  val  Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId [Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class TodoStatus(val code: Short, val name: String) extends EnumStatus
  object TodoStatus extends EnumStatus.Of[TodoStatus] {
    case object IS_TODO     extends TodoStatus(code = 0,   name = "TODO(着手前)")
    case object IS_PROGRESS extends TodoStatus(code = 100, name = "進行中")
    case object IS_COMPLETE extends TodoStatus(code = 255, name = "完了")
  }

  def apply(categoryId: TodoCategory.Id, title: String, body: String, state: TodoStatus): Todo#WithNoId = {
    new Entity.WithNoId(
      new Todo(
        id         = None,
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }

  def build(categoryId: TodoCategory.Id, title: String, body: String, state: TodoStatus): Todo#WithNoId =
    new Todo(
      id         = None,
      categoryId = categoryId,
      title      = title,
      body       = body,
      state      = state
    ).toWithNoId
}