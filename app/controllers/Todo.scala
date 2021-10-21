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

  val cssSrcSeq = Seq("main", "category", "list", "todo").map(s => s + ".css")
  val jsSrcSeq  = Seq("main.js")
  /*
   * Todo 一覧表示
   * */
  def list() = Action.async {
    val fCategory = TodoCategoryRepository.fecheAll()
    val fTodo     = TodoRepository.fecheAll()
    for (
      category <- fCategory;
      todo     <- fTodo
    ) yield {
      val categoryList = category.map(category =>
        TodoCategory(
          category.id,
          category.v.name,
          category.v.color
        )
      )
      val todoList = todo.map(todo =>
        TodoListObj(
          todo.id,
          categoryList.find(category =>
            category.id == todo.v.categoryId).getOrElse(TodoCategory(todo.v.categoryId, "カテゴリなし", 0)),
          todo.v.title,
          todo.v.body,
          todo.v.state
        )
      )

      val vv = ViewValueTodo(
        title  = "Todo管理",
        cssSrc = cssSrcSeq,
        jsSrc  = jsSrcSeq,
        categoryList = categoryList,
        todoList = todoList
      )
      Ok(views.html.TodoList(vv))
    }
  }

  private val postUrl = routes.TodoController.add

  /*
   * 登録画面 表示
   */
  def addIndex() = Action { implicit request: MessagesRequest[AnyContent] =>
    val vvta = ViewValueTodoAdd(
      title        = "Todo追加",
      cssSrc       = cssSrcSeq,
      jsSrc        = jsSrcSeq
    )
    Ok(views.html.TodoAdd(vvta, form, postUrl))
  }

  /*
   * 登録処理
   */
  def add() = Action async { implicit request: MessagesRequest[AnyContent] =>
    println("呼び出してるよ")
    val formValidationResult = form.bindFromRequest()
    println(form.bindFromRequest())
    formValidationResult.fold(
      {formWithErrors: Form[TodoForm] =>
        println("エラーなったよ")
        println(formWithErrors)
        println("エラーなったよ2")
        Future(BadRequest(
          views.html.TodoAdd(
            ViewValueTodoAdd(
              title        = "Todo追加: エラー",
              cssSrc       = cssSrcSeq,
              jsSrc        = jsSrcSeq),
            form,
            postUrl)
      ))},
      { dataForm: TodoForm =>
        val todoData: Todo#WithNoId = Todo.build(
          lib.model.TodoCategory.Id(1),
          "title",
          "body",
          Todo.TodoStatus.IS_TODO
        )
        val addExec = TodoRepository.add(todoData)
        for (
          todoCreate  <- addExec
        ) yield {
          todoCreate match {
            case _ =>
              Redirect(routes.TodoController.list())
                .flashing("success" -> "Todoを追加しました!!")
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
