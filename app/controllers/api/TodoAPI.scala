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
  def add(): Action[AnyContent] = Action.async  {request =>
   request.body.asJson.map { json =>

      val todoData: Todo#WithNoId = Todo(
        lib.model.TodoCategory.Id((json \ "categoryId").as[Long]),
        (json \ "title").as[String],
        (json \ "body").as[String],
        Todo.TodoStatus.IS_TODO
      )
      for (
        todoCreate  <- TodoRepository.add(todoData)
      ) yield {
        Ok(Json.obj("result" -> todoCreate.toString))
      }
    }.getOrElse {
     Future.successful( BadRequest(Json.obj("message" -> "登録に失敗しました")))
    }
  }

  /**
   * Todo 更新
   * @return
   */
  def update(): Action[AnyContent] = Action.async  {request =>
    request.body.asJson.map { json =>
      val stateCode = (json \ "state").as[Short]
      val taegrtData: Todo#EmbeddedId = Todo(
        id         = Some(lib.model.Todo.Id((json \ "id").as[Long])),
        categoryId = lib.model.TodoCategory.Id((json \ "categoryId").as[Long]),
        title      =  (json \ "title").as[String],
        body       = (json \ "body").as[String],
        state      = Todo.TodoStatus.find(_.code == stateCode).getOrElse(Todo.TodoStatus.IS_TODO)
      ).toEmbeddedId
      for (
        todoUpdate  <- TodoRepository.update(taegrtData)
      ) yield {
        Ok(Json.obj("result" -> todoUpdate.toString))
      }
    }.getOrElse {
      Future.successful( BadRequest(Json.obj("message" -> "更新に失敗しました")))
    }
  }

  /**
   * Todo  削除
   * @return
   */
  def delete(): Action[AnyContent] = Action.async  {request =>
    request.body.asJson.map { json =>
      val targetId = lib.model.Todo.Id((json \ "id").as[Long])
      for (
        todoDelete <- TodoRepository.remove(targetId)
      ) yield {
        Ok(Json.obj("result" -> todoDelete.toString))
      }
    }.getOrElse {
      Future.successful( BadRequest(Json.obj("message" -> "削除に失敗しました")))
    }
  }

}