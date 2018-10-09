package ch.acmesoftware.typesolr.catseffect

import cats.effect.IO
import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentEncoder}
import org.apache.solr.client.solrj.SolrClient

case class Client(solr: SolrClient) extends core.Client[IO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): IO[Unit] = IO {
    solr.add(documentEncoder.encode(document).build())
  }

  override def query[T](): IO[core.Client.QueryResult[T]] = IO {
    ???
  }
}

object Client extends ClientFactory[IO] {
  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): IO[core.Client[IO]] = IO {
    Client(makeHttp(url, connectionTimeout, socketTimeout))
  }
}
