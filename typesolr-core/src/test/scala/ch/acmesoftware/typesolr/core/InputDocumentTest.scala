package ch.acmesoftware.typesolr.core

import org.scalatest.{FlatSpec, Matchers}

class InputDocumentTest extends FlatSpec with Matchers {

  "InputDocument" should "be created using ~ operator" in {
    val doc: Document = ("field_1" -> "foo") ~
      ("field_2" -> 1) ~
      ("field_3" -> (Some("test"): Option[String])) //TODO: Care about this ugly option hack

    doc.size shouldEqual 3
  }
}
