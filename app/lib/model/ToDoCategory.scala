/**
 * This is a sample of Todo Application.
 *
 */

package lib.model

import ixias.model._

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import TodoCategory._
case class TodoCategory(
                 id:         Option[Id],
                 name:       String,
                 slug:       String,
                 color:      Int,
                 updatedAt:  LocalDateTime = NOW,
                 createdAt:  LocalDateTime = NOW
               ) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object TodoCategory {

  val  Id = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId [Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  def apply(name: String, slug: String, color: Int): TodoCategory#WithNoId = {
    new Entity.WithNoId(
      new TodoCategory(
        id    = None,
        name  = name,
        slug  = slug,
        color = color
      )
    )
  }
}