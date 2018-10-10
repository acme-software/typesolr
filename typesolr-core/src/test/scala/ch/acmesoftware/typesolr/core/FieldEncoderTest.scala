package ch.acmesoftware.typesolr.core

import org.apache.solr.common.SolrInputField
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class FieldEncoderTest extends FlatSpec with Matchers {

  "FieldEncoder" should "transform a string" in {
    testInputField(FieldEncoder.encode("test_s", "Foo"))("test_s", "Foo")
  }

  it should "transform an Option[String]" in {
    testInputField(FieldEncoder.encode("test_s", Some("Foo"): Option[String]))("test_s", "Foo")
    testInputField(FieldEncoder.encode("test_s", None: Option[String]))("test_s", null)
  }

  it should "transform a List[String]" in {
    testInputFieldMulti(FieldEncoder.encode("test_s", Nil: List[String]))("test_s", null)
    testInputFieldMulti(FieldEncoder.encode("test_s", List("Foo")))("test_s", List("Foo"))
    testInputFieldMulti(FieldEncoder.encode("test_s", List("Foo", "Bar")))("test_s", List("Foo", "Bar"))
  }

  it should "transform a Seq[String]" in {
    testInputFieldMulti(FieldEncoder.encode("test_s", Nil: Seq[String]))("test_s", null)
    testInputFieldMulti(FieldEncoder.encode("test_s", Seq("Foo")))("test_s", Seq("Foo"))
    testInputFieldMulti(FieldEncoder.encode("test_s", Seq("Foo", "Bar")))("test_s", Seq("Foo", "Bar"))
  }

  def testInputField[T](f: SolrInputField)(shouldName: String, shouldValue: T) = {
    f.getName shouldEqual shouldName
    f.getValue.asInstanceOf[T] shouldEqual shouldValue
  }

  def testInputFieldMulti[T](f: SolrInputField)(shouldName: String, shouldValue: Seq[T]) = {
    f.getName shouldEqual shouldName
    Option(f.getValues).map(_.asScala.toSeq shouldEqual shouldValue).getOrElse(f.getValue shouldEqual shouldValue)
  }
}
