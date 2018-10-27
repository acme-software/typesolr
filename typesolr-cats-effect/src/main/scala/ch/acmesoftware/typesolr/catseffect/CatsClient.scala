package ch.acmesoftware.typesolr.catseffect

import java.nio.file.Path

import cats.effect.IO
import ch.acmesoftware.typesolr.core
import ch.acmesoftware.typesolr.core.{ClientFactory, DocumentDecoder, DocumentEncoder}
import ch.acmesoftware.typesolr.querydsl.{Query, QueryParser}
import org.apache.solr.client.solrj.SolrClient

case class CatsClient(solr: SolrClient) extends core.Client[IO] {

  override def index[T](document: T)(implicit documentEncoder: DocumentEncoder[T]): IO[Unit] = IO {
    doIndex(document)
  }

  override def query[T](q: Query)(implicit documentDecoder: DocumentDecoder[T]): IO[core.Client.Result[T]] = for {
    solrQuery <- IO(QueryParser.parse(q))
    solrResponse <- IO(solr.query(solrQuery))
  } yield result(solrResponse, q, documentDecoder)

  override def commit(): IO[Unit] = IO{
    doCommit()
  }

  override def ping: IO[core.Client.PingResponse] = IO{
    doPing()
  }

  override def close: IO[Unit] = IO {
    doClose()
  }
}

object CatsClient extends ClientFactory[IO] {

  override def http(url: String, connectionTimeout: Int, socketTimeout: Int): IO[core.Client[IO]] = IO {
    CatsClient(makeHttp(url, connectionTimeout, socketTimeout))
  }

  override def embedded(rootDir: Path, defaultCoreName: String)
                       (implicit creator: ClientFactory.EmbeddedCreator): IO[core.Client[IO]] = IO{
    CatsClient(creator.create(rootDir, defaultCoreName))
  }
}
