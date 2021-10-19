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
import lib.persistence.default.{ToDoRepository}
import lib.model.ToDo._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit req =>

    // Todo一覧
//    val todoList = Seq(
//      Todo(1,1,"デザインをいい感じにする","ヘッダーのデザインをもっといい感じに", Status.IS_TODO),
//      Todo(2,1,"デザインをいい感じにする2","ヘッダーのデザインをもっといい感じに", Status.IS_TODO),
//      Todo(3,1,"デザインをいい感じにする3","ヘッダーのデザインをもっといい感じに", Status.IS_TODO)
//    )

    println("開始")
//    val toDoLists =  Await.result(ToDoRepository.fecheAll(), Duration.Inf)
    val toDoLists   = Await.result(ToDoRepository.fecheAll(), Duration.Inf).map{ todo =>
      println(todo.id)
      model.ToDo(
        todo.id.toInt,
        1,
        "A",
        "内容",
        lib.model.ToDo.Status.IS_TODO
      )

    }
    println(toDoLists)
    println("終了")

    val vv = ViewValueHome(
      title    = "Todo管理",
      cssSrc   = Seq("main","category","list","todo").map(s=> s  + ".css"),
      jsSrc    = Seq("main.js"),
      todoList = toDoLists,
    )

    Ok(views.html.Home(vv))
  }
}
