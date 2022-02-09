package connectors

import com.google.inject.ImplementedBy
import reactivemongo.api.{AsyncDriver, DB, MongoConnection}
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.bson.collection.BSONCollection

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[MongoConfigImpl])
trait MongoConfig {

  implicit val ec: ExecutionContext

  private val mongoUri = "mongodb://localhost:27017"

  private val driver: AsyncDriver                 = AsyncDriver()
  private val parsedUri: Future[ParsedURI]        = MongoConnection.fromString(mongoUri)
  private val connection: Future[MongoConnection] = parsedUri.flatMap(driver.connect(_))

  def database: Future[DB] = connection.flatMap(_.database("todos"))

  def collection: Future[BSONCollection] = database.map(_.collection("todos"))
}

@Singleton
class MongoConfigImpl @Inject()(implicit val ec: ExecutionContext) extends MongoConfig