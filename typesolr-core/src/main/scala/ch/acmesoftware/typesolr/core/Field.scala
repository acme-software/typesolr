package ch.acmesoftware.typesolr.core

case class Field[T](key: String, value: T) { self =>
  def ~[B](b: Field[B])(implicit encA: FieldEncoder[T], encB: FieldEncoder[B]) = Document.of(self).withField(b)
}

object Field {
  sealed trait DecodingError

  case class TypeMismatch(fieldName: String) extends DecodingError

  case class FieldNotFound(fieldName: String) extends DecodingError

  case class EmptyValue(fieldName: String) extends DecodingError
}