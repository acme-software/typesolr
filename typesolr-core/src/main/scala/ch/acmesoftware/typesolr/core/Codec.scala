package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.DocumentDecoder.DecodingResult

sealed trait Codec[T] extends DocumentEncoder[T] with DocumentDecoder[T]

object Codec {

  def apply[T](enc: T => Document, dec: Document => DecodingResult[T]): Codec[T] = new Codec[T] {
    override def encode(document: T): Document = enc(document)
    override def decode(document: Document): DecodingResult[T] = dec(document)
  }
}