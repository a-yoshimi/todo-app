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
                 color:      Short,
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

  // 色選択用
  val Colorlist = Seq(
    ("1","1:lightblue"), ("2","2:lightpink"), ("3","3:lightgreen"), ("4","4:lightcoral"), ("5","5:lightyellow")
  )

  def apply(name: String, slug: String, color: Short): TodoCategory#WithNoId = {
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