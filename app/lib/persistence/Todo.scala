/**
 * This is a sample of Todo Application.
 *
 */

package lib.persistence

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import lib.model.{Todo, TodoCategory}
import slick.jdbc.JdbcProfile
import lib.model.Todo.TodoStatus

// TodoRepository: TodoTableへのクエリ発行を行うRepository層の定義
//~~~~~~~~~~~~~~~~~~~~~~
case class TodoRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[Todo.Id, Todo, P]
    with db.SlickResourceProvider[P] {

  import api._

  /**
   * Get Todo Data
   */
  def fecheAll(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick.result
    }

  /**
   * Get Todo Data by Key
   */
  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { _
      .filter(_.id === id)
      .result.headOption
    }

  /**
   * Add Todo Data
   */
  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoTable) { slick =>
      println("登録")
      slick returning slick.map(_.id) += entity.v
    }

  /**
   * Update Todo Data
   */
  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  /**
   * Delete Todo Data
   */
  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }

  /**
   * Update Todo Data  for Delete TodoCategory Data
   */
  def updateForDeleteCategory(categoryId: TodoCategory.Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.categoryId === categoryId)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.map(_.categoryId).update(TodoCategory.Id(0))
        }
      } yield old
    }
}