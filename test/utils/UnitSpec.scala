package utils

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.test.Helpers.{defaultAwaitTimeout, await => helpersAwait}

import scala.concurrent.Future

trait UnitSpec extends AnyWordSpecLike with Matchers with MockFactory {
  def await[T](f: Future[T]): T = helpersAwait(f) // makes awaiting look a bit cleaner
}
