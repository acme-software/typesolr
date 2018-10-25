package ch.acmesoftware.typesolr.querydsl

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConverters._

class QueryParserSpec extends FlatSpec with Matchers {

  "QueryParser" should "create a solr query representation" in {
    val q = "name_s" =:= "Frank"
    val res = QueryParser.parse(q)

    res.getQuery shouldEqual q.q
  }

  it should "add highlight fields from DSL query correctly" in {
    val res = QueryParser.parse("name_s" =:= "Frank" highlight "name_s")

    res.getHighlight shouldBe true
    res.getHighlightFields.toList shouldEqual List("name_s")

    val res2 = QueryParser.parse("name_s" =:= "Frank" highlight "name_s" betweenTags("<b>", "</b>"))

    res2.getHighlight shouldBe true
    res2.getHighlightFields.toList shouldEqual List("name_s")
    res2.getHighlightSimplePre shouldEqual "<b>"
    res2.getHighlightSimplePost shouldEqual "</b>"
  }

  it should "support ASCII DSL for highlighting" in {
    val res = QueryParser.parse("name_s" =:= "Frank" ^^ "name_s")

    res.getHighlight shouldBe true
    res.getHighlightFields.toList shouldEqual List("name_s")

    val res2 = QueryParser.parse("name_s" =:= "Frank" ^^ "name_s" t "<b>" </> "</b>")

    res2.getHighlight shouldBe true
    res2.getHighlightFields.toList shouldEqual List("name_s")
    res2.getHighlightSimplePre shouldEqual "<b>"
    res2.getHighlightSimplePost shouldEqual "</b>"
  }

  it should "support highlighting of multiple fields" in {
    val res = QueryParser.parse("name_s" =:= "Frank" ^^ "name_s" :: "description_t" :: "foo")

    res.getHighlight shouldBe true
    res.getHighlightFields.toSet shouldEqual Set("name_s", "description_t", "foo")
  }
}
