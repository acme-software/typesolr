package ch.acmesoftware.typessolr.embedded

import java.nio.file.Paths
import java.util.UUID

import ch.acmesoftware.typesolr.catseffect.Client
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import ch.acmesoftware.typesolr.embedded.CatsEffect._
import org.apache.commons.io.FileUtils._

class CatsEffectTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val solrBasePath = "/tmp/solr-test"
  val corePath = Paths.get(solrBasePath).resolve(UUID.randomUUID().toString)

  override def beforeAll(): Unit = {

    corePath.toFile.mkdirs()

    copyDirectory(Paths.get("res/test-core").toAbsolutePath.toFile, corePath.toFile)
  }

  "CatsEffect Clientfactory" should "create an embedded client" in {

    val client = Client.embedded(corePath.toString, "Test").unsafeRunSync()

    client
  }
}
