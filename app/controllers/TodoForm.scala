package controllers

import java.time.LocalDateTime

object TodoForm {
  import play.api.data.Forms._
  import play.api.data.Form

  case class TodoForm(
    id:         Long,
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Short
  )

  val form = Form(
    mapping(
      "id"         -> longNumber,
      "categoryId" -> longNumber,
      "title"      -> nonEmptyText,
      "body"       -> nonEmptyText,
      "state"      -> shortNumber(min = 0, max = 255)
    )(TodoForm.apply)(TodoForm.unapply)
  )

}
