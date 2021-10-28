/**
 *
 * to do sample project
 *
 */
package controllers.api

import java.sql.Timestamp
import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{Future, _}
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.data._
import play.api.i18n._
import play.api.mvc._
import play.api.libs.json._
import play.libs.concurrent.HttpExecutionContext


import model._
import lib.persistence.default.{TodoRepository,TodoCategoryRepository}
import lib.model.Todo
import lib.model.Todo._
import controllers.TodoForm._

@Singleton
class TodoAPIController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
   * Todo 一覧取得
   * @return
   */
  def list(): Action[AnyContent] = Action.async {
    for (
      todos <- TodoRepository.fecheAll()
    ) yield {
      val result = todos.map(todo =>
          Json.obj(
            "id"         -> todo.id.toInt,
            "categoryId" -> todo.v.categoryId.toInt,
            "title"      -> todo.v.title,
            "body"       -> todo.v.body,
            "state"      -> todo.v.state.code
          )
      )
      Ok( Json.toJson(result))
    }
  }
  /**
   * Todo 登録
   * @return
   */
  def add(): Action[AnyContent] = Action {request =>
    println( request.body)
    Ok(Json.obj("test" -> "wakanna"))
  }
}