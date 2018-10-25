package ch.acmesoftware.typesolr.zio

import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentDecoder, DocumentEncoder}
import ch.acmesoftware.typesolr.querydsl.{Query, QueryParser}
import org.apache.solr.client.solrj.SolrClient
import scalaz.zio.IO

case class Client(solr: SolrClient) extends core.Client[ThrowableIO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): ThrowableIO[Unit] = IO.point {
    doIndex(document)
  }

  override def query[T](q: Query)(implicit documentDecoder: DocumentDecoder[T]): ThrowableIO[core.Client.Result[T]] = for {
    solrQuery <- IO.now(QueryParser.parse(q))
    solrResponse <- IO.point(solr.query(solrQuery))
    res <- IO.point(result(solrResponse, q, documentDecoder))
  } yield res
}

object Client extends ClientFactory[ThrowableIO] {

  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): ThrowableIO[Client] = IO.point {
    Client(makeHttp(url, connectionTimeout, socketTimeout))
  }
}
