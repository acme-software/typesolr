package ch.acmesoftware.typesolr.core

import cats.data.Validated._
import ch.acmesoftware.typesolr.core.Document.{DocumentListItem, InvalidDocument}
import ch.acmesoftware.typesolr.core.FieldDecoder.FieldValidationResult

trait DocumentDecoder[T] {

  def decode(document: Document): FieldValidationResult[T]

  def toDocumentListItem(document: Document): DocumentListItem[T] = {
    decode(document).fold(e => Invalid(InvalidDocument(document, e.toList)), Valid(_))
  }
}