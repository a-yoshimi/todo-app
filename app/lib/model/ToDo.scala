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
import ToDo._
case class ToDo(
                 id:         Option[Id],
                 categoryId: Int,
                 title:      String,
                 body:       String,
                 state:      Status,
                 updatedAt:  LocalDateTime = NOW,
                 createdAt:  LocalDateTime = NOW
               ) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object ToDo {

  val  Id = the[Identity[Id]]
  type Id = Long @@ ToDo
  type WithNoId = Entity.WithNoId [Id, ToDo]
  type EmbeddedId = Entity.EmbeddedId[Id, ToDo]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object IS_TODO     extends Status(code = 0,   name = "TODO(着手前)")
    case object IS_PROGRESS extends Status(code = 100, name = "進行中")
    case object IS_COMPLETE extends Status(code = 255,   name = "完了")
  }

  def apply(categoryId: Int, title: String, body: String, state: Status): ToDo#WithNoId = {
    new Entity.WithNoId(
      new ToDo(
        id         = None,
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }
}