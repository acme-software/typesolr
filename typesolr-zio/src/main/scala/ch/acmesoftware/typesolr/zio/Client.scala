package ch.acmesoftware.typesolr.zio

import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentEncoder}
import org.apache.solr.client.solrj.SolrClient
import scalaz.zio.IO

case class Client(solr: SolrClient) extends core.Client[ThrowableIO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): ThrowableIO[Unit] = IO.point {
    solr.add(documentEncoder.encode(document).build())
  }

  override def query[T](): IO[Throwable, core.Client.QueryResult[T]] = IO.point {
    doQuery()
  }
}

object Client extends ClientFactory[ThrowableIO] {

  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): ThrowableIO[Client] = IO.point {
    Client(makeHttp(url, connectionTimeout, socketTimeout))
  }
}
