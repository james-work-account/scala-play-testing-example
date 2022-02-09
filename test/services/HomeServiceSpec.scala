package services

import connectors.MongoConnector
import models.{Todo, TodoError}
import models.TodoError.GenericError
import org.scalamock.handlers.CallHandler
import utils.UnitSpec

import scala.concurrent.Future

class HomeServiceSpec extends UnitSpec {
  val mockConnector: MongoConnector = mock[MongoConnector]

  trait Test {
    val service = new HomeService(mockConnector)

    def stubCall: CallHandler[Future[Either[TodoError, Seq[Todo]]]] = (mockConnector.getTodos _: () => Future[Either[TodoError, Seq[Todo]]]).expects()
  }

  "get" must {
    "return Right" when {
      "connector returns a Right" in new Test {
        stubCall.returns(Future.successful(Right(Seq())))

        val res = await(service.get)

        res mustBe Right(Seq())
      }
    }
    "return Left" when {
      "connector returns a Left" in new Test {
        stubCall.returns(Future.successful(Left(GenericError())))

        val res = await(service.get)

        res mustBe Left(GenericError())
      }
    }
  }
}
