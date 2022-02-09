package controllers

import models.{Todo, TodoError}
import models.Todo.todoText
import models.TodoError.{GenericError, OtherError}
import org.scalamock.handlers.CallHandler
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import play.api.test._
import services.CreateTodoService
import utils.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateTodoControllerSpec extends UnitSpec {

  val mockService: CreateTodoService = mock[CreateTodoService]

  trait Test {
    val controller = new CreateTodoController(mockService, stubControllerComponents())

    val validForm: (String, String) = todoText -> "beans"
    val emptyTextForm: (String, String) = todoText -> ""
    val invalidForm: (String, String) = "something else" -> ""

    def stubCall: CallHandler[Future[Either[TodoError, Unit]]] = (mockService.post(_: Todo)).expects(*)
  }

  "CreateTodoController GET" should {
    "render the page" in new Test {
      val res = controller.get()(FakeRequest(GET, "/create").withCSRFToken)

      status(res) mustBe OK
      contentType(res) mustBe Some("text/html")
    }
  }


  "CreateTodoController POST" should {
    "redirect to the index page" when {
      "form is valid and service returns a Right" in new Test {
        stubCall.returns(Future.successful(Right({})))

        val res = controller.post()(FakeRequest(POST, "/create").withFormUrlEncodedBody(validForm))

        status(res) mustBe SEE_OTHER
        header(LOCATION, res) mustBe Some("/")
      }
    }
    "render the create page with an invalid form" when {
      "todoText is empty" in new Test {
        val res = controller.post()(FakeRequest(POST, "/create").withFormUrlEncodedBody(emptyTextForm).withCSRFToken)

        status(res) mustBe BAD_REQUEST
        contentType(res) mustBe Some("text/html")
        contentAsString(res) must include ("form") // renders the create page again
      }
      "todoText is missing" in new Test {
        val res = controller.post()(FakeRequest(POST, "/create").withFormUrlEncodedBody(invalidForm).withCSRFToken)

        status(res) mustBe BAD_REQUEST
        contentType(res) mustBe Some("text/html")
        contentAsString(res) must include ("form") // renders the create page again
      }
      "service returns a Left with an OtherError" in new Test {
        stubCall.returns(Future.successful(Left(OtherError())))

        val res = controller.post()(FakeRequest(POST, "/create").withFormUrlEncodedBody(validForm).withCSRFToken)

        status(res) mustBe BAD_REQUEST
        contentType(res) mustBe Some("text/html")
        contentAsString(res) must include("form") // renders the create page again
      }
    }
    "return an error page" when {
      "service returns a Left" in new Test {
        stubCall.returns(Future.successful(Left(GenericError())))

        val res = controller.post()(FakeRequest(POST, "/create").withFormUrlEncodedBody(validForm))

        status(res) mustBe BAD_REQUEST
        contentType(res) mustBe Some("text/plain")
        contentAsString(res) must not include "form" // doesn't render the create page again
      }
    }
  }
}
