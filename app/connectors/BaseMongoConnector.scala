package connectors

import models.TodoError
import play.api.Logger
import reactivemongo.api.bson.collection.BSONCollection

import scala.concurrent.Future

trait BaseMongoConnector {

  def collection: Future[BSONCollection]

  val logger: Logger = Logger(this.getClass)

  def recoverError[A]: PartialFunction[Throwable, Either[TodoError, A]] = ???

}
