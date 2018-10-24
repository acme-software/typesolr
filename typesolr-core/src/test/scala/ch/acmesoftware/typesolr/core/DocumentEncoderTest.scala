package ch.acmesoftware.typesolr.core

import cats.data.Validated._
import cats.implicits._
import org.scalatest.{FlatSpec, Matchers}

class DocumentEncoderTest extends FlatSpec with Matchers {

  case class TestUser(name: String, active: Boolean)

  val userCodec = Codec[TestUser](
    u => ("name_s" -> u.name) ~
      ("active_b" -> u.active),
    d => (
      d.field[String]("name_s"),
      d.field[Boolean]("active_b")
    ).mapN(TestUser)
  )

  "Client" should "transform case classes to documents" in {

    val u = TestUser("Frank", true)

    val encoded = userCodec.encode(u)

    val decoded = userCodec.decode(encoded)

    decoded shouldEqual Valid(u)
  }
}
