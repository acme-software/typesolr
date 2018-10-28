package ch.acmesoftware.typesolr.zio

import java.nio.file.Path

import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentDecoder, DocumentEncoder}
import ch.acmesoftware.typesolr.querydsl.{Query, QueryParser}
import org.apache.solr.client.solrj.SolrClient

import scalaz.zio.IO

case class ZioClient(solr: SolrClient) extends core.Client[ThrowableIO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): ThrowableIO[Unit] = IO.point {
    doIndex(document)
  }

  override def query[T](q: Query)(implicit documentDecoder: DocumentDecoder[T]): ThrowableIO[core.Client.Result[T]] = for {
    solrQuery <- IO.now(QueryParser.parse(q))
    solrResponse <- IO.point(solr.query(solrQuery))
    res <- IO.point(result(solrResponse, q, documentDecoder))
  } yield res

  override def commit(): ThrowableIO[Unit] = IO.point(doCommit())

  override def ping: ThrowableIO[core.Client.PingResponse] = IO.point(doPing())

  override def close: ThrowableIO[Unit] = IO.point(doClose())
}

object ZioClient extends ClientFactory[ThrowableIO] {

  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): ThrowableIO[ZioClient] = IO.point {
    ZioClient(makeHttp(url, connectionTimeout, socketTimeout))
  }

  override def embedded(rootDir: Path, defaultCoreName: String)
                       (implicit creator: ClientFactory.EmbeddedCreator): ThrowableIO[core.Client[ThrowableIO]] = IO.point{
    ZioClient(creator.create(rootDir, defaultCoreName))
  }
}
