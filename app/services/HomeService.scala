package services

import connectors.MongoConnector
import models.{Todo, TodoError}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class HomeService @Inject()(connector: MongoConnector) {
  def get: Future[Either[TodoError, Seq[Todo]]] = connector.getTodos
}
