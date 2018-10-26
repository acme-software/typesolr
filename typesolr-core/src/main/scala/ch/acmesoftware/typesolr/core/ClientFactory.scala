package ch.acmesoftware.typesolr.core

import java.nio.file.{Path, Paths}

import ch.acmesoftware.typesolr.core.ClientFactory.EmbeddedCreator
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.HttpSolrClient

import scala.annotation.implicitNotFound
import scala.language.higherKinds

trait ClientFactory[F[_]] {

  /** Creates an http solr client */
  def http(url: String, connectionTimeout: Int, socketTimeout: Int): F[Client[F]]

  def http(url: String): F[Client[F]] = http(url, 10000, 60000)

  /** Creates a local embedded solr server */
  def embedded(rootDir: Path, defaultCoreName: String)(implicit creator: EmbeddedCreator): F[Client[F]]

  def embedded(rootDir: Path)(implicit creator: EmbeddedCreator): F[Client[F]] = embedded(rootDir, "default")

  def embedded(rootDir: String, defaultCoreName: String)(implicit creator: EmbeddedCreator): F[Client[F]] = embedded(Paths.get(rootDir), defaultCoreName)

  def embedded(rootDir: String)(implicit creator: EmbeddedCreator): F[Client[F]] = embedded(rootDir, "default")

  protected def makeHttp(url: String, connectionTimeout: Int, socketTimeout: Int) = new HttpSolrClient.Builder(url)
    .withConnectionTimeout(connectionTimeout)
    .withSocketTimeout(socketTimeout)
    .build()
}

object ClientFactory {

  /** Used by typesolr-embedded to create embedded solr instances */
  @implicitNotFound("Implicit EmbeddedCreator not found. You need to add the \"ch.acmesoftware\" %% \"typesolr-embedded\" %% \"version\" dependency in SBT and import ch.acmesoftware.typesolr.embedded._")
  trait EmbeddedCreator {
    def create(rootDir: Path, defaultCoreName: String): SolrClient
  }

}