package controllers


object TodoCategoryForm {
  import play.api.data.Forms._
  import play.api.data.Form

  case class TodoCategoryForm(
    id:         Long,
    name :      String,
    slug:       String,
    color:      Short
  )

  val form = Form(
    mapping(
      "id"         -> longNumber,
      "name"       -> nonEmptyText,
      "slug"       -> nonEmptyText,
      "color"      -> shortNumber(min = 0, max = 255)
    )(TodoCategoryForm.apply)(TodoCategoryForm.unapply)
  )

}
