package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.Field.DecodingError

trait FieldDecoder[T] {
  def decode(key: String, value: List[String]): Either[DecodingError, Field[T]]
}

