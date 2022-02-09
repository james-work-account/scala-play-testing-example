package services

import connectors.MongoConnector
import models.{Todo, TodoError}
import models.TodoError.GenericError
import org.scalamock.handlers.CallHandler
import utils.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateTodoServiceSpec extends UnitSpec {
  val mockConnector: MongoConnector = mock[MongoConnector]

  trait Test {
    val service = new CreateTodoService(mockConnector)

    val todo: Todo = Todo("beans")

    def stubGetTodo: CallHandler[Future[Either[TodoError, Seq[Todo]]]] = (mockConnector.getTodo(_: Todo)).expects(*)

    def stubCreateTodo: CallHandler[Future[Either[TodoError, Unit]]] = (mockConnector.createTodo(_: Todo)).expects(*)
  }

  "post" must {
    "return Right" when {
      "connector.getTodo returns an empty Seq and connector.createTodo returns a Right" in new Test {
        stubGetTodo.returns(Future.successful(Right(Seq())))
        stubCreateTodo.returns(Future.successful(Right({})))

        val res = await(service.post(todo))

        res mustBe Right({})
      }
    }
    "return Left" when {
      "connector.getTodo returns a non-empty Seq" in new Test {
        stubGetTodo.returns(Future.successful(Right(Seq(todo))))

        val res = await(service.post(todo))

        res mustBe Left(GenericError(Some("oh no")))
      }
      "connector.getTodo returns a Left" in new Test {
        stubGetTodo.returns(Future.successful(Left(GenericError())))

        val res = await(service.post(todo))

        res mustBe Left(GenericError())
      }
      "connector.createTodo returns a Left" in new Test {
        stubGetTodo.returns(Future.successful(Right(Seq())))
        stubCreateTodo.returns(Future.successful(Left(GenericError())))

        val res = await(service.post(todo))

        res mustBe Left(GenericError())
      }
    }
  }
}
