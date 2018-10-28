package ch.acmesoftware.typesolr.integrationtest

import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}

trait CatsIntegrationTest extends FlatSpec with Matchers {

  def isIoFailed[T](io: IO[T], eCmp: Throwable => Boolean) = io.attempt.unsafeRunSync() match {
    case Right(_) => false
    case Left(e) => eCmp(e)
  }
}
