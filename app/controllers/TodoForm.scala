package controllers

import java.time.LocalDateTime
import lib.model.TodoCategory

object TodoForm {
  import play.api.data.Forms._
  import play.api.data.Form
  import play.api.data.FormError
  import play.api.data.format.Formatter

  case class TodoForm(
    id:         Long,
    categoryId: TodoCategory.Id,
    title:      String,
    body:       String,
    state:      Short
  )

  val form = Form(
    mapping(
      "id"         -> longNumber,
      "categoryId" -> of[TodoCategory.Id],
      "title"      -> nonEmptyText,
      "body"       -> nonEmptyText,
      "state"      -> shortNumber(min = 0, max = 255)
    )(TodoForm.apply)(TodoForm.unapply)
  )

  private implicit def categoryIdFormat: Formatter[TodoCategory.Id] = new Formatter[TodoCategory.Id] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], TodoCategory.Id] = {
      data.get(key) match {
          case None | Some("") => Left(Seq(FormError(key, "error.required")))
          case Some(v)         => Right(TodoCategory.Id(v.toLong))
        }
    }

    def unbind(key: String, value: TodoCategory.Id) = Map(key -> value.toString)
  }

}
