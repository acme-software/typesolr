package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.Client.QueryResult
import org.apache.solr.client.solrj.SolrClient

import scala.language.higherKinds

trait Client[F[_]] {

  val solr: SolrClient

  def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): F[Unit]

  def doIndex[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): Unit = ???

  def query[T](q: String)(implicit documentDecoder: DocumentDecoder[T]): F[QueryResult[T]]

  def doQuery[T](q: String)(implicit documentDecoder: DocumentDecoder[T]): QueryResult[T] = ???
}

object Client {

  sealed trait IndexResult

  case object Success extends IndexResult

  trait QueryResult[T] {
    def documents: Seq[T]
  }

}