package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.DocumentDecoder.DecodingResult
import ch.acmesoftware.typesolr.core.Field.DecodingError

trait DocumentDecoder[T] {

  def decode(document: Document): DecodingResult[T]
}

object DocumentDecoder {

  type DecodingResult[T] = Either[DecodingError, T]

}