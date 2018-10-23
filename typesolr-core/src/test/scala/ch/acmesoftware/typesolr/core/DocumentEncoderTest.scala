package ch.acmesoftware.typesolr.core

import org.scalatest.{FlatSpec, Matchers}


class DocumentEncoderTest extends FlatSpec with Matchers {

  case class TestUser(name: String, active: Boolean, age: Option[Long])

  val userCodec = Codec[TestUser](
    u => ("name_s" -> u.name) ~ ("active_b" -> u.active) ~("age_i" -> u.age),
    d => for {
      name <- d.get[String]("name_s")
      active <- d.get[Boolean]("active_b"),
      age <- d.get[Option[Long]]("age_i")
    } yield TestUser(name, active, age)
  )

  "Client" should "transform case classes to documents" in {

    val u = TestUser("Frank", true, Some(29))

    val encoded = userCodec.encode(u)

    val decoded = userCodec.decode(encoded)

    decoded shouldEqual Right(u)
  }
}
