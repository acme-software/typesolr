package ch.acmesoftware.typesolr.core

import cats.data.ValidatedNel
import ch.acmesoftware.typesolr.core.FieldDecoder.FieldValidationResult

trait FieldDecoder[T] {
  def decode(key: String, value: List[String]): FieldValidationResult[Field[T]]
}

object FieldDecoder {
  sealed trait FieldValidationError

  case class TypeMismatch(fieldName: String) extends FieldValidationError

  case class FieldNotFound(fieldName: String) extends FieldValidationError

  case class EmptyValue(fieldName: String) extends FieldValidationError

  type FieldValidationResult[A] = ValidatedNel[FieldValidationError, A]
}