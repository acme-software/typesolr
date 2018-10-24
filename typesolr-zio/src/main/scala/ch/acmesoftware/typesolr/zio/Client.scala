package ch.acmesoftware.typesolr.zio

import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentDecoder, DocumentEncoder}
import org.apache.solr.client.solrj.SolrClient
import scalaz.zio.IO

case class Client(solr: SolrClient) extends core.Client[ThrowableIO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): ThrowableIO[Unit] = IO.point {
    doIndex(document)
  }

  override def query[T](q: String)(implicit documentDecoder: DocumentDecoder[T]): ThrowableIO[core.Client.QueryResult[T]] = IO.point{
    doQuery(q)
  }
}

object Client extends ClientFactory[ThrowableIO] {

  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): ThrowableIO[Client] = IO.point {
    Client(makeHttp(url, connectionTimeout, socketTimeout))
  }
}
