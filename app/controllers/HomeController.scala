package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.HomeService

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(
                                service: HomeService,
                                val controllerComponents: ControllerComponents
                              )(implicit executionContext: ExecutionContext) extends BaseController {

  def get(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    service.get.map {
      case Left(error)  => BadRequest(error.message)
      case Right(todos) => Ok(views.html.index(todos))
    }
  }
}
