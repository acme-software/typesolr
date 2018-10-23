package ch.acmesoftware.typesolr.core

trait FieldEncoder[T] {

  def encode(f: Field[T]): (String, List[String])
}