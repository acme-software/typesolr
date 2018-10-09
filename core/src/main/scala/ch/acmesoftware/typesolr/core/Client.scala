package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.Client.QueryResult

import scala.language.higherKinds

trait Client[F[_]] {
  def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): F[Unit]

  def query[T](): F[QueryResult[T]]
}

object Client {

  sealed trait IndexResult

  case object Success extends IndexResult

  trait QueryResult[T] {
    def documents: Seq[T]
  }

}