/**
 *
 * to do sample project
 *
 */
package controllers.api

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import play.libs.concurrent.HttpExecutionContext

import lib.model.{Todo, TodoCategory}
import lib.model.Todo._
import lib.model.TodoCategory._

@Singleton
class ConfigAPIController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
   *  Todoステータス 取得
   * @return
   */
  def getStateList(): Action[AnyContent] = Action {

    Ok(Json.toJson(Todo.OptionStatus.map(_ match {
      case (code, name) => Json.obj("code" -> code, "name" -> name)
      case _ => Json.obj("code" -> "Dummy", "name" -> "Dummy")
    })))
  }

  /**
   *  カテゴリーカラー 取得
   * @return
   */
  def getColorList(): Action[AnyContent] = Action {

    Ok(Json.toJson(TodoCategory.Colorlist.map(_ match {
      case (code, name) => Json.obj("code" -> code, "name" -> name)
      case _ => Json.obj("code" -> "Dummy", "name" -> "Dummy")
    })))
  }

}