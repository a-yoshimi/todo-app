/**
*
*
*
* */
package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.{Todo, TodoCategory}

// TodoTable: Todoテーブルへのマッピングを行う
//~~~~~~~~~~~~~~
case class TodoTable[P <: JdbcProfile]()(implicit val driver: P)
  extends Table[Todo, P] {
  import api._

  // Definition of DataSourceName
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do") ,
    "slave"  -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  // Definition of Query
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // Definition of Table
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Table(tag: Tag) extends BasicTable(tag, "to_do") {
    import Todo._
    // Columns
    /* @1 */ def id         = column[Id]              ("id",          O.UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def categoryId = column[TodoCategory.Id] ("category_id", O.UInt32)
    /* @3 */ def title      = column[String]          ("title",       O.Utf8Char255)
    /* @3 */ def body       = column[String]          ("body",        O.Text)
    /* @4 */ def state      = column[TodoStatus]      ("state",       O.UInt32)
    /* @5 */ def updatedAt  = column[LocalDateTime]   ("updated_at",  O.TsCurrent)
    /* @6 */ def createdAt  = column[LocalDateTime]   ("created_at",  O.Ts)

    type TableElementTuple = (
      Option[Id], TodoCategory.Id, String, String, TodoStatus, LocalDateTime, LocalDateTime
      )


    // DB <=> Scala の相互のmapping定義
    def * = (id.?, categoryId, title, body, state, updatedAt, createdAt) <> (
      // Tuple(table) => Model
      (t: TableElementTuple) => Todo(
        t._1, t._2, t._3, t._4, t._5, t._6, t._7
      ),
      // Model => Tuple(table)
      (v: TableElementType) => Todo.unapply(v).map { t => (
        t._1, t._2, t._3, t._4, t._5, LocalDateTime.now(), t._7
      )}
    )
  }
}