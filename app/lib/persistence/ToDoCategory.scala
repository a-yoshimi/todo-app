/**
 * This is a sample of TodoCategory Application.
 *
 */

package lib.persistence

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import lib.model.TodoCategory
import slick.jdbc.JdbcProfile

// TodoCategoryRepository: TodoCategoryTableへのクエリ発行を行うRepository層の定義
//~~~~~~~~~~~~~~~~~~~~~~
case class TodoCategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[TodoCategory.Id, TodoCategory, P]
    with db.SlickResourceProvider[P] {

  import api._

  /**
   * Get TodoCategory Data
   */
  def fecheAll(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable, "slave") { slick =>
      slick.result
    }

  /**
   * Get TodoCategory Data by Key
   */
  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable, "slave") { _
      .filter(_.id === id)
      .result.headOption
    }

  /**
   * Add TodoCategory Data
   */
  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoCategoryTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  /**
   * Update TodoCategory Data
   */
  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable) { slick =>
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
   * Delete TodoCategory Data
   */
  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable) { slick =>
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
   * Delete TodoCategory Data and Update Todo Data
   */
  def removeAndUpdateTodo(id: Id): Future[Option[EntityEmbeddedId]] =
    DBAction(TodoCategoryTable) { case (db, slick1) =>
    DBAction(TodoTable) { case (_, slick2) =>
        val action = for {
          old <- slick1.filter(_.id === id).result.headOption
          _ <- slick1.filter(_.id === id).delete
          _ <- slick2.filter(_.categoryId === id).result.headOption
          _ <- slick2.filter(_.categoryId === id).map(_.categoryId).update(TodoCategory.Id(0))
        } yield old
        db.run(action.transactionally)
    }}
//    RunDBAction(TodoCategoryTable) { slick =>
//      val row = slick.filter(_.id === id)
//      for {
//        old <- row.result.headOption
//        _   <- old match {
//          case None    => DBIO.successful(0)
//          case Some(_) => row.delete
//        }
//      } yield old
}
