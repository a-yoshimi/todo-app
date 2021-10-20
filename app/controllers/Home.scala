/**
 *
 * to do sample project
 *
 */
package controllers
import java.sql.Timestamp

import scala.concurrent.duration._
import scala.concurrent._

import javax.inject._
import play.api.mvc._
import model._
import lib.persistence.default.{TodoRepository,TodoCategoryRepository}
import lib.model.Todo._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit req =>

    // カテゴリ一覧の取得
    val categoryList = Await.result(TodoCategoryRepository.fecheAll(), Duration.Inf).map(category =>
      TodoCategory(
        category.id,
        category.v.name,
        category.v.color
      )
    )
    println("a")
    // todo一覧の取得
      val todoList  = Await.result(TodoRepository.fecheAll(), Duration.Inf).map(todo =>
      Todo(
        todo.id,
        categoryList.find(category =>
          category.id == todo.v.categoryId).getOrElse(
          Some(
            TodoCategory(
              todo.v.categoryId,
              "カテゴリなし",
              0
            )
          ).get
        ).name,
        todo.v.title,
        todo.v.body,
        todo.v.state,
      )
    )


    val vv = ViewValueHome(
      title        = "Todo管理",
      cssSrc       = Seq("main","category","list","todo").map(s=> s  + ".css"),
      jsSrc        = Seq("main.js"),
      categoryList = categoryList,
      todoList     = todoList
    )
    Ok(views.html.Home(vv))
  }

//      Await.result(TodoRepository.fecheAll(), Duration.Inf).map{ todo =>
//      println(todo.id)
//      Todo(
//        todo.id.toInt,
//        2,
//        "A",
//        "内容",
//        lib.model.Todo.TodoStatus.IS_TODO
//      )
}
