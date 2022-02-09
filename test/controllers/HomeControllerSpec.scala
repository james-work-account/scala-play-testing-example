package controllers

import models.TodoError.GenericError
import models.{Todo, TodoError}
import org.scalamock.handlers.CallHandler
import play.api.test.Helpers._
import play.api.test._
import services.HomeService
import utils.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HomeControllerSpec extends UnitSpec {

  val mockService: HomeService = mock[HomeService]

  trait Test {
    val controller = new HomeController(mockService, stubControllerComponents())

    def stubCall: CallHandler[Future[Either[TodoError, Seq[Todo]]]] = (mockService.get _: () => Future[Either[TodoError, Seq[Todo]]]).expects()
  }

  "HomeController GET" should {
    "render the index page" when {
      "service returns no Todos" in new Test {
        stubCall.returns(Future.successful(Right(Seq())))

        val res = controller.get()(FakeRequest(GET, "/"))

        status(res) mustBe OK
        contentType(res) mustBe Some("text/html")
        contentAsString(res) must not include ("beans")
      }
      "service returns Todos" in new Test {
        stubCall.returns(Future.successful(Right(Seq(Todo("beans")))))

        val res = controller.get()(FakeRequest(GET, "/"))

        status(res) mustBe OK
        contentType(res) mustBe Some("text/html")
        contentAsString(res) must include("beans")
      }
    }
    "render an error" when {
      "service returns a Left" in new Test {
        stubCall.returns(Future.successful(Left(GenericError())))

        val res = controller.get()(FakeRequest(GET, "/"))

        status(res) mustBe BAD_REQUEST
        contentType(res) mustBe Some("text/plain")
      }
    }
  }
}
