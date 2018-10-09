package ch.acmesoftware.typesolr

package object core {

  implicit def tupleToInputDocument[T](t: (String, T))(implicit encoder: FieldEncoder[T]): InputDocument = InputDocument(t)

}
