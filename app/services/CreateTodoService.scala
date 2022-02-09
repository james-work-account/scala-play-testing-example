package services

import connectors.MongoConnector
import models.TodoError.GenericError
import models.{Todo, TodoError}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateTodoService @Inject()(connector: MongoConnector)(implicit val ec: ExecutionContext) {
  def post(todo: Todo): Future[Either[TodoError, Unit]] = {
    connector.getTodo(todo).flatMap {
      case Left(error)                      => Future.successful(Left(error)) // connector call failed
      case Right(result) if result.nonEmpty => Future.successful(Left(GenericError(Some("oh no")))) // connector call succeeded but todo already exists
      case Right(_)                         => connector.createTodo(todo)
    }
  }
}
