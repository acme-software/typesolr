package ch.acmesoftware.typesolr.core

import cats.data.Validated
import cats.implicits._
import ch.acmesoftware.typesolr.core.FieldDecoder.{FieldNotFound, FieldValidationError, FieldValidationResult}
import org.apache.solr.common.{SolrDocument, SolrInputDocument}

import scala.collection.JavaConverters._

case class Document(fields: Map[String, List[String]]) {

  def ~[T](f: Field[T])(implicit enc: FieldEncoder[T]): Document = withField(f)

  def withField[T](f: Field[T])(implicit enc: FieldEncoder[T]): Document = copy(fields + enc.encode(f))

  def withRawField(name: String, value: List[String]): Document = copy(fields + (name -> value))

  def field[T](key: String)(implicit dec: FieldDecoder[T]): FieldValidationResult[T] = fields.get(key).
    map(v => dec.decode(key, v).map(_.value)).
    getOrElse(FieldNotFound(key).invalidNel)

  def toSolrInputDoc: SolrInputDocument = fields.foldLeft(new SolrInputDocument())((doc, f) => {
    val d2 = doc.deepCopy()
    f._2.foreach(d2.addField(f._1, _))
    d2
  })

  def size: Int = fields.size
}

object Document {

  type DocumentListItem[T] = Validated[InvalidDocument, T]

  def of[A](f: Field[A])(implicit enc: FieldEncoder[A]) = Document(Map(enc.encode(f)))

  def fromSolr(doc: SolrDocument): Document = doc.getFieldNames.asScala.
    foldLeft(Document(Map.empty))(
      (d, n) => d.withRawField(n, doc.getFieldValues(n).asScala.map(_.toString).toList)
    )

  case class InvalidDocument(doc: Document, validationErrors: List[FieldValidationError])
}