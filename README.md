TypeSOLR - A typed Scala Client for Apache Solr 
===============================================

[![Build Status](https://travis-ci.org/acme-software/typesolr.svg?branch=master)](https://travis-ci.org/acme-software/typesolr) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/3f4d6692386840668589bbf17d90437b)](https://www.codacy.com/app/frne/typesolr?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=acme-software/typesolr&amp;utm_campaign=Badge_Grade)

**Provides a typesafe, ideomatic Scala DSL for indexing and querying an Apache Solr search index**

***By now, this is totally experimental and by no means production-ready. Provided information and code can be buggy, 
not yet implemented or completely wrong. Working towards a first stable version (`1.0.0`)***

Usage
-----

Install using SBT:

```scala
libraryDependencies += "ch.acmesoftware" %% "typesolr-core" % "VERSION"
```

This lib is intended to use together with an IO-Monad, because talking to a SOLR server is effectful. By now, there are 
implementations for [Cats Effect](https://typelevel.org/cats-effect/) and [ZIO](https://scalaz.github.io/scalaz-zio/).

Use one of the following:

```scala
libraryDependencies += "ch.acmesoftware" %% "typesolr-cats-effect" % "VERSION"
libraryDependencies += "ch.acmesoftware" %% "typesolr-zio" % "VERSION"
```

Basically, this is a tagless final DSL, so you can also implement your own effect type...

Typesafe Query DSL
------------------

TypeSOLR provides a typesafe domain specific language to produce SOLR queries. The main purpose of using it, is of course, 
like with all typesafe DSL, that you catch errors at compile time. The final aim of this component is: *If it compiles, 
all queries are valid.*

### Basic example

```scala
import ch.acmesoftware.typesolr.querydsl._

val q = ("field_a" =:= "foo" and ("field_b" =*:*= "wildcard" or "field_b" =~= "fuzzy match"))
q.build // (field_a: "foo" and (field_b:: *wildcard* or field_b: ~fuzzy match))
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
| `=~=(amount)` | `fuzzy(value: String).at(amount: Int)`                  | Fuzzy match with amount              |

#### Additional

| Operator                        | Method                             | Description                                               |
|---------------------------------|------------------------------------|-----------------------------------------------------------|
| `^^ "field"`                    | `highlight[T](field: String)`      | Enables SOLR's term highlighting for the given field name |
| `^^ "a" :: "b" :: "c"`          | `highlight[T](fields: Seq[String])`| Enables SOLR's term highlighting for multiple fields      |
| `^^ "field" t "<b>" </> "</b>"` | `highlight[T](fields: Seq[String])`| Highlighting with custom pre and pist tags                |

*To be continued*