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

  private def baselist( ): Future[ViewValueCategory]  = {
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
        title = "Category",
        cssSrc = cssSrcSeq,
        jsSrc = jsSrcSeq,
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
      )yield {
        Ok(views.html.TodoCategory(vvc, form))
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
          vvt <- baselist
        )yield {
          BadRequest(views.html.TodoCategory(vvt, formWithErrors))
        }
      },
      { dataForm: TodoCategoryForm =>
        val categoryData: TodoCategory#WithNoId = TodoCategory(
          dataForm.name,
          dataForm.slug,
          dataForm.color
        )
        for (
          todoCreate  <- TodoCategoryRepository.add(categoryData)
        ) yield {
          todoCreate match {
            case _ =>
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
    val fCategory     = TodoCategoryRepository.get(TodoCategory.Id(id))
    for (
      category <- fCategory;
      vvc  <- baselist
    ) yield {
      category match {
        case Some(data) => {
          Ok(views.html.TodoCategory(vvc, form.fill(TodoCategoryForm(
            id         = id,
            name       = data.v.name,
            slug       = data.v.slug,
            color      = data.v.color
          ))))
        }
        case _ => {
          Ok(views.html.TodoCategory(vvc, form))
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
          BadRequest(views.html.TodoCategory(vvc, formWithErrors))
        }
      },
      { dataForm: TodoCategoryForm =>
        val taegrtData: TodoCategory#EmbeddedId =
          new TodoCategory(
            id         = Some(TodoCategory.Id(id)),
            name       = dataForm.name,
            slug       = dataForm.slug,
            color      = dataForm.color,
        ).toEmbeddedId
        for (
          todoUpdate  <- TodoCategoryRepository.update(taegrtData)
        ) yield {
          todoUpdate match {
            case _ =>
              Redirect(routes.TodoCategoryController.list())
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
        todoDelete <- TodoCategoryRepository.remove(todoCategoryId)
      } yield {
        todoDelete match {
          case _ =>
            Redirect(routes.TodoCategoryController.list())
        }
      }
  }
}
