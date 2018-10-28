package ch.acmesoftware.typesolr.core

import ch.acmesoftware.typesolr.core.Client.{PingResponse, Result}
import ch.acmesoftware.typesolr.core.Document.{DocumentListItem, InvalidDocument}
import ch.acmesoftware.typesolr.querydsl.Query
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.response.{QueryResponse, SolrPingResponse}

import scala.collection.JavaConverters._
import scala.language.higherKinds

trait Client[F[_]] {

  val solr: SolrClient

  def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): F[Unit]

  def query[T](q: Query)(implicit documentDecoder: DocumentDecoder[T]): F[Result[T]]

  def commit(): F[Unit]

  def ping: F[PingResponse]

  def close: F[Unit]

  protected def doIndex[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): Unit = {
    val encodedDoc = documentEncoder.encode(document)

    solr.add(encodedDoc.toSolrInputDoc)
  }

  protected def doCommit(): Unit = solr.commit()

  protected def doPing(): PingResponse = PingResponse.fromSolr(solr.ping())

  protected def doClose(): Unit = solr.close()

  protected def result[T](sr: QueryResponse, q: Query, documentDecoder: DocumentDecoder[T]): Result[T] = {
    val docs = sr.getResults.iterator().asScala.
      map(Document.fromSolr).
      map(documentDecoder.toDocumentListItem).
      toList

    Result(docs, q)
  }
}

object Client {

  case class Result[T](docs: List[DocumentListItem[T]], query: Query) {

    def size: Int = docs.size

    /** Returns only successfully decoded documents, executing errorEffect for invalid documents */
    def valid(invalidEffect: InvalidDocument => Unit): List[T] = docs.flatMap(_.fold(
      e => {
        invalidEffect(e)
        None
      },
      Some(_)
    ))

    /** Returns the number of failures **/
    def errors: Int = docs.size - valid(_ => ()).size

    /** Returns true if the result contains errored documents **/
    def isClean: Boolean = errors == 0

    /** Folds all results to a given type
      *
      * @param errorFn   Function to process errors
      * @param successFn Function to process successes
      */
    def fold[A](errorFn: InvalidDocument => A, successFn: T => A): List[A] = docs.map(_.fold(errorFn, successFn))
  }

  case class PingResponse(status: Int, qTime: Int)

  object PingResponse {
    def fromSolr(s: SolrPingResponse): PingResponse = PingResponse(s.getStatus, s.getQTime)
  }
}