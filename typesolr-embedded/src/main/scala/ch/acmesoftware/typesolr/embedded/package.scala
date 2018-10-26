package ch.acmesoftware.typesolr

import java.nio.file.{Path, Paths}

import ch.acmesoftware.typesolr.core.ClientFactory.EmbeddedCreator
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer

package object embedded {

  implicit val embeddedCreator: EmbeddedCreator =
    (rootDir: Path, defaultCoreName: String) => new EmbeddedSolrServer(rootDir, defaultCoreName)
}
