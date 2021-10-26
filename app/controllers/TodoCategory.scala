/**
 *
 * to do sample project
 *
 */
package controllers

import java.sql.Timestamp
import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{Future, _}
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.http.DefaultHttpErrorHandler
import play.api.data._
import play.api.i18n._
import play.api.mvc._

import model._
import lib.persistence.default.{TodoRepository,TodoCategoryRepository}
import lib.model.TodoCategory
import lib.model.TodoCategory._
import controllers.TodoCategoryForm._

@Singleton
class TodoCategoryController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import TodoCategoryForm._

  val cssSrcSeq = Seq("main", "category", "list", "todo", "input").map(s => s + ".css")
  val jsSrcSeq  = Seq("main.js")

  private def baselist(): Future[ViewValueCategory]  = {
    for (
      category <-  TodoCategoryRepository.fecheAll()
    ) yield {
      val categoryList = category.map(category =>
        model.TodoCategory(
          category.id,
          category.v.name,
          category.v.slug,
          category.v.color
        )
      )
      ViewValueCategory(
        title        = "Category",
        cssSrc       = cssSrcSeq,
        jsSrc        = jsSrcSeq,
        categoryList = categoryList
      )
    }
  }

  /*
   * Category 一覧表示
   * */
  def list() = Action.async { implicit request: MessagesRequest[AnyContent] =>
      for (
        vvc <- baselist
      ) yield {
        Ok(views.html.TodoCategory(vvc, form, Seq.empty[String]))
      }
  }

  /*
   * 登録処理
   */
  def add() = Action async { implicit request: MessagesRequest[AnyContent] =>

    val formValidationResult = form.bindFromRequest()
    formValidationResult.fold(
      {formWithErrors: Form[TodoCategoryForm] =>
        for (
          vvc <- baselist
        )yield {
          BadRequest(views.html.TodoCategory(vvc, formWithErrors, Seq("登録失敗しました")))
        }
      },
      { dataForm: TodoCategoryForm =>
        val categoryData: TodoCategory#WithNoId = TodoCategory(
          dataForm.name,
          dataForm.slug,
          dataForm.color
        )
        for {
          todoCreate <- TodoCategoryRepository.add(categoryData)
          vvc <- baselist
        } yield {
          todoCreate match {
            case _    =>
              Redirect(routes.TodoCategoryController.list())
          }
        }
      }
    )
  }
  /*
   * 編集画面 表示
   */
  def updateIndex(id: Long) = Action async{ implicit request: MessagesRequest[AnyContent] =>
    for (
      vvc  <- baselist
    ) yield {
      vvc.categoryList.find(data => data.id.toInt == id ) match {
        case Some(data) => {
          Ok(views.html.TodoCategory(vvc, form.fill(TodoCategoryForm(
            id         = id,
            name       = data.name,
            slug       = data.slug,
            color      = data.color
          )), Seq.empty[String]))
        }
        case _ => {
          BadRequest(views.html.TodoCategory(vvc,  form, Seq("対象のカテゴリデータがありません : " + id.toString)))
        }
      }
    }
  }

  /*
  * 編集処理
  */
  def update(id: Long) = Action async{ implicit request: MessagesRequest[AnyContent] =>
    val formValidationResult = form.bindFromRequest()
    formValidationResult.fold(
      {formWithErrors: Form[TodoCategoryForm] =>
        for (
          vvc <- baselist
        )yield {
          BadRequest(views.html.TodoCategory(vvc, formWithErrors,  Seq("更新失敗しました")))
        }
      },
      { dataForm: TodoCategoryForm =>
        val taegrtData: TodoCategory#EmbeddedId = TodoCategory(
            id         = Some(TodoCategory.Id(id)),
            name       = dataForm.name,
            slug       = dataForm.slug,
            color      = dataForm.color,
        ).toEmbeddedId
        for {
          todoUpdate  <- TodoCategoryRepository.update(taegrtData)
          vvc <- baselist
        }yield {
          todoUpdate match {
            case None => BadRequest(views.html.TodoCategory(vvc, form,  Seq("対象の更新データはありません")))
            case _ => Redirect(routes.TodoCategoryController.list())
          }
        }
      }
    )
  }
  /*
   *  Todo 削除
   */
  def delete(id: Long) = Action async {
      val todoCategoryId = lib.model.TodoCategory.Id(id)
      for {
        deleteAndUpdate <- TodoCategoryRepository.removeAndUpdateTodo(todoCategoryId)
      } yield {
        deleteAndUpdate match {
          case _ =>
            Redirect(routes.TodoCategoryController.list())
        }
      }
  }
}
