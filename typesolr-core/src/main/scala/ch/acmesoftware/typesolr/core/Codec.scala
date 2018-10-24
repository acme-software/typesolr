package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.DocumentDecoder.DocumentValidationResult

sealed trait Codec[T] extends DocumentEncoder[T] with DocumentDecoder[T]

object Codec {

  def apply[T](enc: T => Document, dec: Document => DocumentValidationResult[T]): Codec[T] = new Codec[T] {
    override def encode(document: T): Document = enc(document)

    override def decode(document: Document): DocumentValidationResult[T] = dec(document)
  }
}