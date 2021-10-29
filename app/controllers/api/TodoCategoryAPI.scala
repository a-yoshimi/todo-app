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
import lib.persistence.default.{TodoCategoryRepository}
import lib.model.TodoCategory
import lib.model.TodoCategory._

@Singleton
class TodoCategoryAPIController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
   * TodoCategory 一覧取得
   * @return
   */
  def list(): Action[AnyContent] = Action.async {
    for (
      categoriess <- TodoCategoryRepository.fecheAll()
    ) yield {
      val result = categoriess.map(category =>
          Json.obj(
            "id"         -> category.id.toInt,
            "name"       -> category.v.name,
            "slug"       -> category.v.slug,
            "color"      -> category.v.color
          )
      )
      Ok( Json.toJson(result))
    }
  }

  /**
   * TodoCategory 登録
   * @return
   */
  def add(): Action[AnyContent] = Action.async  {request =>
   request.body.asJson.map { json =>

      val categoryData: TodoCategory#WithNoId = TodoCategory(
        (json \ "name").as[String],
        (json \ "slug").as[String],
        (json \ "color").as[Short]
      )
      for (
        categoryCreate  <- TodoCategoryRepository.add(categoryData)
      ) yield {
        Ok(Json.obj("result" -> categoryCreate.toString))
      }
    }.getOrElse {
     Future.successful( BadRequest(Json.obj("message" -> "登録に失敗しました")))
    }
  }

  /**
   * TodoCategory 更新
   * @return
   */
  def update(): Action[AnyContent] = Action.async  {request =>
    request.body.asJson.map { json =>
      val taegrtData: TodoCategory#EmbeddedId = TodoCategory(
        id         = Some(lib.model.TodoCategory.Id((json \ "id").as[Long])),
        name       = (json \ "name").as[String],
        slug       = (json \ "slug").as[String],
        color      = (json \ "color").as[Short]
      ).toEmbeddedId
      for (
        categoryUpdate  <- TodoCategoryRepository.update(taegrtData)
      ) yield {
        Ok(Json.obj("result" -> categoryUpdate.toString))
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
      val targetId = lib.model.TodoCategory.Id((json \ "id").as[Long])
      for (
        todoDelete <- TodoCategoryRepository.remove(targetId)
      ) yield {
        Ok(Json.obj("result" -> todoDelete.toString))
      }
    }.getOrElse {
      Future.successful( BadRequest(Json.obj("message" -> "削除に失敗しました")))
    }
  }

}