package ch.acmesoftware.typesolr.embedded

import java.nio.file.Paths

import cats.effect.IO
import ch.acmesoftware.typesolr.catseffect.Client
import ch.acmesoftware.typesolr.core.ClientFactory
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer

object CatsEffect {

  implicit class EmbeddedClientFactory(clientFactory: ClientFactory[IO]) extends EmbeddedFactory[IO] {
    override def embedded(basePath: String, defaultCoreName: String): IO[Client] = IO {
      Client(new EmbeddedSolrServer(Paths.get(basePath), defaultCoreName))
    }
  }

}
