package ch.acmesoftware.typessolr.embedded

import ch.acmesoftware.typesolr.catseffect.Client
import org.scalatest.{FlatSpec, Matchers}
import ch.acmesoftware.typesolr.embedded.CatsEffect._

class CatsEffectTest extends FlatSpec with Matchers {

  "CatsEffect Clientfactory" should "create an embedded client" in {

    val client = Client.embedded("/tmp/solr", "ATestCore").unsafeRunSync()
  }
}
