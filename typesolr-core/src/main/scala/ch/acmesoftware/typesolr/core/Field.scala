package ch.acmesoftware.typesolr.core

case class Field[T](key: String, value: T) { self =>
  def ~[B](b: Field[B])(implicit encA: FieldEncoder[T], encB: FieldEncoder[B]) = Document.withField(self).withField(b)
}