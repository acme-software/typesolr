package ch.acmesoftware.typesolr.core

import org.apache.solr.common.SolrInputField

trait FieldEncoder[T] {

  def encode(k: String, v: T): SolrInputField
}

object FieldEncoder {

  implicit val stringEncoder: FieldEncoder[String] = (k, v) => makeField(k, v)
  implicit val intEncoder: FieldEncoder[Int] = (k, v) => makeField(k, v)
  implicit val longEncoder: FieldEncoder[Long] = (k, v) => makeField(k, v)
  implicit val floatEncoder: FieldEncoder[Float] = (k, v) => makeField(k, v)
  implicit val doubleEncoder: FieldEncoder[Double] = (k, v) => makeField(k, v)

  implicit def optionEncoder[T](implicit enc: FieldEncoder[T]): FieldEncoder[Option[T]] = (k, v) => v.
    map(enc.encode(k, _)).
    getOrElse(emptyField(k))

  implicit def listEncoder[T](implicit enc: FieldEncoder[T]): FieldEncoder[List[T]] = (k, v) => v.
  map(enc.encode(k, _)).
    foldLeft(emptyField(k))((f, entry) => {
      val field = f.deepCopy()
      field.addValue(entry.getValue)
      field
    })

  implicit def seqEncoder[T](implicit enc: FieldEncoder[List[T]]): FieldEncoder[Seq[T]] = (k, v) => enc.encode(k, v.toList)

  def encode[T](k: String, v: T)(implicit encoder: FieldEncoder[T]): SolrInputField = encoder.encode(k, v)

  def makeField[T](name: String, value: T) = {
    val field = new SolrInputField(name)
    field.setValue(value)
    field
  }

  def emptyField(name: String) = new SolrInputField(name)
}