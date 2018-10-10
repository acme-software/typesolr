package ch.acmesoftware.typesolr.core

import org.apache.solr.common.{SolrInputDocument, SolrInputField}

import scala.collection.JavaConverters._

final case class InputDocument(fields: Map[String, SolrInputField] = Map.empty) {

  def withField[T](name: String, value: T)(implicit encoder: FieldEncoder[T]): InputDocument =
    InputDocument(fields + (name -> encoder.encode(name, value)))

  def ~[T](f: (String, T))(implicit encoder: FieldEncoder[T]): InputDocument = withField(f._1, f._2)

  def build(): SolrInputDocument = new SolrInputDocument(fields.asJava)

  def fieldCount: Int = fields.size
}

object InputDocument {
  def apply[T](field: (String, T))(implicit encoder: FieldEncoder[T]): InputDocument = InputDocument().withField(field._1, field._2)
  def apply(fields: Map[String, SolrInputField] = Map.empty): InputDocument = new InputDocument(fields)
}