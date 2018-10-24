package ch.acmesoftware.typesolr.core

import cats.data.ValidatedNel
import ch.acmesoftware.typesolr.core.DocumentDecoder.DocumentValidationResult
import ch.acmesoftware.typesolr.core.FieldDecoder.FieldValidationError

trait DocumentDecoder[T] {

  def decode(document: Document): DocumentValidationResult[T]
}

object DocumentDecoder {
  type DocumentValidationResult[T] = ValidatedNel[FieldValidationError, T]
}