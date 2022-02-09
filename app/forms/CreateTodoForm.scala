package forms

import models.Todo
import models.Todo._
import play.api.data.Form
import play.api.data.Forms._

object CreateTodoForm {
  val form: Form[Todo] = Form(mapping(
    todoText -> nonEmptyText
  )(Todo.apply)(Todo.unapply))
}
