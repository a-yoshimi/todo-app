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

import play.api.mvc._
import model._
import lib.persistence.default.{TodoRepository,TodoCategoryRepository}
import lib.model.Todo._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action.async {
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
        Todo(
          todo.id,
          categoryList.find(category =>
            category.id == todo.v.categoryId).getOrElse(TodoCategory(todo.v.categoryId, "カテゴリなし", 0)),
          todo.v.title,
          todo.v.body,
          todo.v.state
        )
      )

      val vv = ViewValueHome(
        title = "Todo管理",
        cssSrc = Seq("main", "category", "list", "todo").map(s => s + ".css"),
        jsSrc = Seq("main.js"),
        categoryList = categoryList,
        todoList = todoList
      )
      Ok(views.html.Home(vv))
    }
  }
}
