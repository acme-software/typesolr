package ch.acmesoftware.typesolr.core

import org.apache.solr.client.solrj.impl.HttpSolrClient

import scala.language.higherKinds

trait ClientFactory[F[_]] {

  /** Creates an http solr client **/
  def http(url: String, connectionTimeout: Int, socketTimeout: Int): F[Client[F]]

  def http(url: String): F[Client[F]] = http(url, 10000, 60000)

  protected def makeHttp(url: String, connectionTimeout: Int, socketTimeout: Int) = new HttpSolrClient.Builder(url)
    .withConnectionTimeout(connectionTimeout)
    .withSocketTimeout(socketTimeout)
    .build()
}
