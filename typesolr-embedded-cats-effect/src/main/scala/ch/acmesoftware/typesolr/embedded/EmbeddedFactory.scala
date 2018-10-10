package ch.acmesoftware.typesolr.embedded

import ch.acmesoftware.typesolr.core.Client

import scala.language.higherKinds

trait EmbeddedFactory[F[_]] {
  def embedded(basePath: String, defaultCoreName: String = "Default"): F[Client[F]]
}
