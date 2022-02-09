package controllers

import forms.CreateTodoForm
import forms.CreateTodoForm._
import models.Todo.todoText
import models.TodoError.OtherError
import play.api.data.FormError
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.CreateTodoService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateTodoController @Inject()(service: CreateTodoService, val controllerComponents: ControllerComponents)(
  implicit val ec: ExecutionContext
) extends BaseController
  with I18nSupport {

  def get(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.createTodo(CreateTodoForm.form))
  }

  def post(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(views.html.createTodo(formWithErrors))),
        todo =>
          service.post(todo).map {
            case Left(error: OtherError) =>
              BadRequest(
                views.html.createTodo(
                  CreateTodoForm.form
                    .fill(todo)
                    .withError(FormError(todoText, error.message))
                ))
            case Left(error) => BadRequest(error.message)
            case Right(_) => Redirect(routes.HomeController.get())
          }
      )
  }
}
