package ch.acmesoftware.typesolr.integrationtest

import java.nio.file.Paths

import ch.acmesoftware.typesolr.catseffect._
import ch.acmesoftware.typesolr.core._
import ch.acmesoftware.typesolr.querydsl._
import ch.acmesoftware.typesolr.embedded._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.apache.commons.io.FileUtils._
import org.apache.solr.common.SolrException

class CatsEmbeddedSpec extends CatsIntegrationTest with BeforeAndAfterAll {

  private val testCoreConfigDir = Paths.get(Option(System.getProperty("TYPESOLR_IT_EMBEDDED_CONFIGDIR")).
    getOrElse("integration-test/data/test-core")).toAbsolutePath

  private val solrCoreBaseDir = Paths.get(Option(System.getProperty("TYPESOLR_IT_EMBEDDED_BASEDIR")).
    getOrElse("/tmp/typesolr-test")).toAbsolutePath

  override def beforeAll(): Unit = {

    assert(testCoreConfigDir.toFile.isDirectory && testCoreConfigDir.toFile.canRead, "testCoreConfigDir must be a directory and readable")

    if(!solrCoreBaseDir.toFile.exists()) {
      assert(solrCoreBaseDir.toFile.mkdirs(), "Cannot create solr embedded base dir")
    }

    assert(solrCoreBaseDir.toFile.isDirectory && solrCoreBaseDir.toFile.canWrite, "solrCoreBaseDir must be a directory and writable")

    copyDirectory(testCoreConfigDir.toFile, solrCoreBaseDir.toFile)
  }

  override def afterAll(): Unit = {
    deleteDirectory(solrCoreBaseDir.toFile)
  }

  "Embedded creation" should "be possible on a valid solr core directory" in {
    val client = CatsClient.embedded(solrCoreBaseDir).unsafeRunSync()

    client.ping.unsafeRunSync().status shouldEqual 0

    client.close.unsafeRunSync()
  }

  it should "fail the io monad, if directory is invalid" in {
    isIoFailed(CatsClient.embedded("/tmp/does/not/exist"), _.isInstanceOf[SolrException]) shouldBe true
  }

  "Client" should "index a document and query it again" in {

    val client = CatsClient.embedded(solrCoreBaseDir).unsafeRunSync()

    val doc = ("field_a" -> "foo") ~ ("field_b" -> true)

    val res = (for {
      _ <- client.index(doc)
      _ <- client.commit()
      r <- client.query("field_a" =:= "foo" and "field_b" =:= true)
    } yield r).unsafeRunSync()

    res.isClean shouldBe true
    res.size shouldEqual 1

    client.close.unsafeRunSync()
  }
}
