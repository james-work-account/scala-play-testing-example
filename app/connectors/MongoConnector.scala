package connectors

import models.{Todo, TodoError}
import reactivemongo.api.bson.collection.BSONCollection

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoConnector @Inject()(implicit val ec: ExecutionContext, mongoConfig: MongoConfig) extends BaseMongoConnector {

  override def collection: Future[BSONCollection] = mongoConfig.collection

  def createTodo(todo: Todo): Future[Either[TodoError, Unit]] = {
    ???
  }

  def getTodos: Future[Either[TodoError, Seq[Todo]]] = {
    ???
  }

  def getTodo(todo: Todo): Future[Either[TodoError, Seq[Todo]]] = {
    ???
  }

  def markTodoComplete(todo: Todo): Future[Either[TodoError, Unit]] = {
    ???
  }
}
