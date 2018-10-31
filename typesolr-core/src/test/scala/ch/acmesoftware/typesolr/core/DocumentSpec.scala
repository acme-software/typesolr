package ch.acmesoftware.typesolr.core

import org.scalatest.{FlatSpec, Matchers}

class DocumentSpec extends FlatSpec with Matchers {

  "New instance creation" should "be achievable using its own API" in {
    val doc = Document.
      withField("field_a" ->"test").
      withField("field_b" -> Option(42)).
      withField("field_c" -> true)

    doc.fields.size shouldEqual 3
  }

  it should "be achievable using ~ operator" in {
    val doc = ("field_a" ->"test") ~
      ("field_b" -> Option(42)) ~
      ("field_c" -> true)

    doc.fields.size shouldEqual 3
  }
}
