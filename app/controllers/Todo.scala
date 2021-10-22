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
import lib.model.Todo
import lib.model.Todo._
import controllers.TodoForm._

@Singleton
class TodoController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import TodoForm._

  val cssSrcSeq = Seq("main", "category", "list", "todo", "input").map(s => s + ".css")
  val jsSrcSeq  = Seq("main.js")

  private def baselist( ): Future[ViewValueTodo]  = {
    val fCategory = TodoCategoryRepository.fecheAll()
    val fTodo = TodoRepository.fecheAll()
    for (
      category <- fCategory;
      todo <- fTodo
    ) yield {
      val categoryList = category.map(category =>
        TodoCategory(
          category.id,
          category.v.name,
          category.v.slug,
          category.v.color
        )
      )
      val todoList = todo.map(todo =>
        TodoListObj(
          todo.id,
          categoryList.find(category =>
            category.id == todo.v.categoryId).getOrElse(TodoCategory(todo.v.categoryId, "カテゴリなし", "からっぽ",  0)),
          todo.v.title,
          todo.v.body,
          todo.v.state
        )
      )
      ViewValueTodo(
        title = "Todo",
        cssSrc = cssSrcSeq,
        jsSrc = jsSrcSeq,
        categoryOptionList = categoryList.map(category => (category.id.toString, category.name)),
        todoList = todoList
      )
    }
  }

  /*
   * Todo 一覧表示
   * */
  def list() = Action.async { implicit request: MessagesRequest[AnyContent] =>
      for (
        vvt <- baselist
      )yield {
        Ok(views.html.Todo(vvt, form))
      }
  }

  /*
   * 登録処理
   */
  def add() = Action async { implicit request: MessagesRequest[AnyContent] =>
    val formValidationResult = form.bindFromRequest()
    formValidationResult.fold(
      {formWithErrors: Form[TodoForm] =>
        for (
          vvt <- baselist
        )yield {
          BadRequest(views.html.Todo(vvt, formWithErrors))
        }
      },
      { dataForm: TodoForm =>
        val todoData: Todo#WithNoId = Todo(
          lib.model.TodoCategory.Id(dataForm.categoryId),
          dataForm.title,
          dataForm.body,
          Todo.TodoStatus.IS_TODO
        )
        for (
          todoCreate  <- TodoRepository.add(todoData)
        ) yield {
          todoCreate match {
            case _ =>
              Redirect(routes.TodoController.list())
          }
        }
      }
    )
  }
  /*
   * 編集画面 表示
   */
  def updateIndex(id: Long) = Action async{ implicit request: MessagesRequest[AnyContent] =>
    val fTodo     = TodoRepository.get(Todo.Id(id))
    for (
      todo <- fTodo;
      vvt  <- baselist
    ) yield {
      todo match {
        case Some(data) => {
          Ok(views.html.Todo(vvt, form.fill(TodoForm(
            id         = id,
            categoryId = data.v.categoryId,
            title      = data.v.title,
            body       = data.v.body,
            state      = data.v.state.code
          ))))
        }
        case _ => {
          Ok(views.html.Todo(vvt, form))
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
      {formWithErrors: Form[TodoForm] =>
        for (
          vvt <- baselist
        )yield {
          BadRequest(views.html.Todo(vvt, formWithErrors))
        }
      },
      { dataForm: TodoForm =>
        val taegrtData: Todo#EmbeddedId =
          new Todo(
            id         = Some(Todo.Id(id)),
            categoryId = lib.model.TodoCategory.Id(dataForm.categoryId),
            title      = dataForm.title,
            body       = dataForm.body,
            state      = Todo.TodoStatus.find(_.code == dataForm.state).getOrElse(Todo.TodoStatus.IS_TODO)
        ).toEmbeddedId
        for (
          todoUpdate  <- TodoRepository.update(taegrtData)
        ) yield {
          todoUpdate match {
            case _ =>
              Redirect(routes.TodoController.list())
          }
        }
      }
    )
  }
  /*
   *  Todo 削除
   */
  def delete(id: Long) = Action async {
      val todoId = lib.model.Todo.Id(id)
      for {
        todoDelete <- TodoRepository.remove(todoId)
      } yield {
        todoDelete match {
          case _ =>
            Redirect(routes.TodoController.list())
        }
      }
  }
}
