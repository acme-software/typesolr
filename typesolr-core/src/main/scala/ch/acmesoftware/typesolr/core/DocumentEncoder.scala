package ch.acmesoftware.typesolr.core

trait DocumentEncoder[T] {
  def encode(document: T): Document
}
