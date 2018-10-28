package ch.acmesoftware.typesolr.integrationtest

import java.nio.file.Paths
import java.util.UUID

import cats.data.Validated._
import cats.implicits._
import ch.acmesoftware.typesolr.catseffect._
import ch.acmesoftware.typesolr.core._
import ch.acmesoftware.typesolr.embedded._
import ch.acmesoftware.typesolr.querydsl._
import org.apache.commons.io.FileUtils._
import org.apache.solr.common.SolrException
import org.scalatest.BeforeAndAfterAll

class CatsEmbeddedSpec extends CatsIntegrationTest with BeforeAndAfterAll {

  private val testCoreConfigDir = Paths.get(Option(System.getProperty("TYPESOLR_IT_EMBEDDED_CONFIGDIR")).
    getOrElse("integration-test/data/test-core")).toAbsolutePath

  private val solrCoreBaseDir = Paths.get(Option(System.getProperty("TYPESOLR_IT_EMBEDDED_BASEDIR")).
    getOrElse("/tmp/typesolr-test")).toAbsolutePath

  override def beforeAll(): Unit = {

    assert(testCoreConfigDir.toFile.isDirectory && testCoreConfigDir.toFile.canRead, "testCoreConfigDir must be a directory and readable")

    if (!solrCoreBaseDir.toFile.exists()) {
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

    val doc = ("field_a" -> "foo") ~ ("field_b" -> true)

    val res = (for {
      client <- CatsClient.embedded(solrCoreBaseDir)
      _ <- client.index(doc)
      _ <- client.commit()
      r <- client.query("field_a" =:= "foo" and "field_b" =:= true)
      _ <- client.close
    } yield r).unsafeRunSync()

    res.isClean shouldBe true
    res.size shouldEqual 1
  }

  it should "index a case class and query it again" in {

    case class TestDoc(id: UUID, foo: String, bar: Int)

    implicit val testDocCodec: Codec[TestDoc] = Codec(d =>
      ("id" -> d.id) ~
        ("foo_s" -> d.foo) ~
        ("bar_i" -> d.bar),
      d => (
        d.field[UUID]("id"),
        d.field[String]("foo_s"),
        d.field[Int]("bar_i")
      ).mapN(TestDoc)
    )

    val doc1 = TestDoc(UUID.randomUUID(), "some-test", 42)
    val doc2 = TestDoc(UUID.randomUUID(), "some-other-test", 42)

    val res = (for {
      client <- CatsClient.embedded(solrCoreBaseDir)
      _ <- client.index(doc1)
      _ <- client.index(doc1) // index a second time to test id idempotence
      _ <- client.index(doc2)
      _ <- client.commit()
      r1 <- client.query[TestDoc]("foo_s" =:= "some-test")
      r2 <- client.query[TestDoc]("bar_i" =:= 42)
      _ <- client.close
    } yield (r1, r2)).unsafeRunSync()

    res._1.isClean shouldBe true
    res._1.size shouldEqual 1
    res._1.valid(_ => ()).headOption shouldEqual Some(doc1)

    res._2.isClean shouldBe true
    res._2.size shouldEqual 2
  }
}
