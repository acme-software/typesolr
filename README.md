TypeSOLR - A typesafe Scala Client for Apache Solr 
==================================================

[![Build Status](https://travis-ci.org/acme-software/typesolr.svg?branch=master)](https://travis-ci.org/acme-software/typesolr) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/3f4d6692386840668589bbf17d90437b)](https://www.codacy.com/app/frne/typesolr?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=acme-software/typesolr&amp;utm_campaign=Badge_Grade)

***This library rovides a typesafe, ideomatic Scala DSL for indexing and querying an Apache Solr search index***

Features
--------
- Rich client lib for [Apache Solr](http://lucene.apache.org/solr/) based on [SolrJ](https://lucene.apache.org/solr/guide/7_1/using-solrj.html) 7.x
- Tagless final DSL with implementations for
  - [Cats Effect](https://typelevel.org/cats-effect/)
  - [ZIO](https://scalaz.github.io/scalaz-zio/)
  - More to follow
- Builder for [http](#http-client) and [embedded](#embedded-client) clients
- Intuitive document builder and indexer
- Codec for product and case class mapping
- Typesafe query DSL
- Covering advances SOLR features like
  - Highlighting
  - More to follow

Usage
-----

Install using SBT:

```scala
libraryDependencies += "ch.acmesoftware" %% "typesolr-core" % "VERSION"
```

This lib is intended to be used together with an IO-Monad, because talking to a SOLR server is effectful. By now, there are 
implementations for [Cats Effect](https://typelevel.org/cats-effect/) and [ZIO](https://scalaz.github.io/scalaz-zio/).

Choose one:

```scala
libraryDependencies += "ch.acmesoftware" %% "typesolr-cats-effect" % "VERSION"
libraryDependencies += "ch.acmesoftware" %% "typesolr-zio" % "VERSION"
```

In the following examples, `cats-effect`s `IO[T]` / `CatsClient` is used. There is also a `ZioClient`.

Client
------

The `Client` ist the main entry point to interact with SOLR. It provides methods to index documents and query them. 

### Http Client

There are different options to build a solr client from. The default one is an **HTTP connection**. See the following example:

```scala
import cats.effect.IO
import ch.acmesoftware.typesolr.core._
import ch.acmesoftware.typesolr.catseffect._

val client: IO[Client] = CatsClient.http("http://localhost:8983/techproducts")
```

### Embedded Client

But, for testing or local stuff, you can also run an **embedded solr instance**. To achieve this, you need an additional SBT
dependency:

```scala
// build.sbt
libraryDependencies += "ch.acmesoftware" %% "typesolr-embedded" % "VERSION"
```

Now you can build embedded clients by providing a solr core root directory (e.g. `/tmp/test-solr-dir`) on your local 
machine:

```scala
import cats.effect.IO
import ch.acmesoftware.typesolr.core._
import ch.acmesoftware.typesolr.catseffect._
import ch.acmesoftware.typesolr.embedded._

val client: IO[Client] = CatsClient.embedded("/tmp/test-solr-dir")
```

**Heads Up:**

The embedded solr server should not be used in production. The IO-Monad will fail, if there's something wrong with the 
provided directory. See solr documentation for details.

Document Builder
----------------

A SOLR Document is represented by a `ch.acmesoftware.typesolr.core.Document` instance. it can be built using the 
document building DSL or by mapping any kind of objects like (case) classes using a `ch.acmesoftware.typesolr.core.Codec`
which is able to encode and decode custom types.

### Building DSL

The simplest case of creating an indexable document is using its own API. One can use the named DSl:

```scala
import ch.acmesoftware.typesolr.core._

val doc = Document.
  withField("field_a" ->"test").
  withField("field_b" -> Option(42)).
  withField("field_c" -> true)
```

...or the ASCII DSL:


```scala
import ch.acmesoftware.typesolr.core._

val doc = ("field_a" ->"test") ~
      ("field_b" -> Option(42)) ~
      ("field_c" -> true)
```

The two implementations do exactly the same, it's just a matter of choice, which one you use. Basically, you can combine 
them, but I'd recommend sticking to one.

Typesafe Query DSL
------------------

TypeSOLR provides a typesafe domain specific language to produce SOLR queries. The main purpose of using it, is of course, 
like with all typesafe DSL, that you catch errors at compile time. The final aim of this component is: *If it compiles, 
all queries are valid.*

### Basic example

```scala
import ch.acmesoftware.typesolr.querydsl._

val query = "field_a" =:= "foo" and (
  "field_b" =*:*= "wildcard" or 
  "field_b" =~= "fuzzy match"
)

query.q // (field_a: "foo" and (field_b:: *wildcard* or field_b: ~fuzzy match))
```

**Explanation:**

-  Brackets, `(` and `)` work like in every logical computation.
-  A field query is built using the field name (e.g. `"field_a"`), a comparison operator (e.g. `=:=` for exact match) and
the expected value (e.g. `"foo"`) between brackets.
-  Queries can be combined using `and` / `or` operators. Standard logical rules apply. Use brackets for structure. The 
result of a combination will be a `Query` again.
-  the other operators used in the example are a left-and-right wildcard match (`=*:*=`) and a fuzzy match (`=~=`). See 
the operators reference for details.

### Operator Reference

#### Logical

| Operator      | Method / Object                                         | Description                          |
|---------------|---------------------------------------------------------|--------------------------------------|
| `and`         | `Query.And(a, b)`                                       | Combines two queries with AND        |
| `or`          | `Query.Or(a, b)`                                        | Combines two queries with OR         |
| `(` / `)`     | - Not needed, because of infix notation -               | Logical grouping                     |

#### Field Queries

| Operator      | Method                                                  | Description                          |
|---------------|---------------------------------------------------------|--------------------------------------|
| `=:=`         | `exact[T](value: T)`                                    | Exact value match for a field        |
| `=*:=`        | `wildcard[T](value: String, wildcardType: WildcardType)`| Wildcard match (left side)           |
| `=:*=`        | `wildcard[T](value: String, wildcardType: WildcardType)`| Wildcard match (right side)          |
| `=*:*=`       | `wildcard[T](value: String, wildcardType: WildcardType)`| Wildcard match (both sides)          |
| `=~=`         | `fuzzy(value: String)`                                  | Fuzzy match                          |
| `=~= "a" at 1`| `fuzzy(value: String).at(amount: Int)`                  | Fuzzy match with amount              |

#### Additional

| Operator                        | Method                             | Description                                               |
|---------------------------------|------------------------------------|-----------------------------------------------------------|
| `^^ "field"`                    | `highlight[T](field: String)`      | Enables SOLR's term highlighting for the given field name |
| `^^ "a" :: "b" :: "c"`          | `highlight[T](fields: Seq[String])`| Enables SOLR's term highlighting for multiple fields      |
| `^^ "field" t "<b>" </> "</b>"` | `highlight[T](fields: Seq[String])`| Highlighting with custom pre and pist tags                |
