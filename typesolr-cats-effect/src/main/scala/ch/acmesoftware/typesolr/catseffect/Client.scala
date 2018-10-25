package ch.acmesoftware.typesolr.catseffect

import cats.effect.IO
import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentDecoder, DocumentEncoder}
import ch.acmesoftware.typesolr.querydsl.{Query, QueryParser}
import org.apache.solr.client.solrj.SolrClient

case class Client(solr: SolrClient) extends core.Client[IO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): IO[Unit] = IO {
    doIndex(document)
  }

  override def query[T](q: Query)(implicit documentDecoder: DocumentDecoder[T]): IO[core.Client.Result[T]] = for {
    solrQuery <- IO.pure(QueryParser.parse(q))
    solrResponse <- IO(solr.query(solrQuery))
    res <- IO(result(solrResponse, q, documentDecoder))
  } yield res
}

object Client extends ClientFactory[IO] {
  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): IO[core.Client[IO]] = IO {
    Client(makeHttp(url, connectionTimeout, socketTimeout))
  }
}
