TypeSOLR - A typed Scala Client for Apache Solr 
===============================================

[![Build Status](https://travis-ci.org/acme-software/typesolr.svg?branch=master)](https://travis-ci.org/acme-software/typesolr) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/3f4d6692386840668589bbf17d90437b)](https://www.codacy.com/app/frne/typesolr?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=acme-software/typesolr&amp;utm_campaign=Badge_Grade)

**Provides a typesafe, ideomatic Scala DSL for indexing and querying an Apache Solr search index**

***By now, this is totally experimental and by no means production-ready. Provided information and code can be buggy, 
not yet implemented or completely wrong. Working towards a first stable version (`1.0.0`)***

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

### Usage

This can be used as standalone solr query generator, which produces strings, or together with the TypeSOLR client.

Install using SBT:

```scala
libraryDependencies += "ch.acmesoftware" %% "typesolr-querydsl" % "VERSION"
```

### Operator Reference

| Operator          | Parameters         | Description                          |
|-------------------|--------------------|--------------------------------------|
| `F =:= v`         | None               | Exact value match for a field        |
| `F =*:= S`        | None               | Wildcard match (left side)           |
| `F =:*= S`        | None               | Wildcard match (right side)          |
| `F =*:*= S`       | None               | Wildcard match (both sides)          |
| `F =~= S`         | None               | Fuzzy match                          |
| `F =~=(amount) S` | `amount`: Double   | Fuzzy match with amount              |
| `Q and Q`         | None               | Combines two queries with AND        |
| `Q or Q`          | None               | Combines two queries with OR         |
| `(` / `)`         | None               | Logical grouping                     |

**Legend:**

-  `F` Field name
-  `V` Any value
-  `S` String value
-  `Q` Query

*To be continued*