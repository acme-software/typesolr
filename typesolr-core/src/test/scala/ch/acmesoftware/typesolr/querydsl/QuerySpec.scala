package ch.acmesoftware.typesolr.querydsl

import org.scalatest.{FlatSpec, Matchers}

class QuerySpec extends FlatSpec with Matchers {

  "DSL" should "match exact field value" in {
    ("name_s" =:= "Frank").q shouldEqual """name_s: "Frank""""
    ("age_i" =:= 45).q shouldEqual """age_i: 45"""
    ("age_i" =:= 4562834623846234287l).q shouldEqual """age_i: 4562834623846234287"""
    ("age_i" =:= 45.2f).q shouldEqual """age_i: 45.2"""
    ("age_i" =:= 45.2d).q shouldEqual """age_i: 45.2"""
    ("age_i" =:= BigDecimal("4523946239846293864923.8462946829")).q shouldEqual """age_i: 4523946239846293864923.8462946829"""
  }

  it should "support wildcard queries" in {
    ("field_1" =*:= "foo").q shouldEqual "field_1: *foo"
    ("field_1" =:*= "foo").q shouldEqual "field_1: foo*"
    ("field_1" =*:*= "foo").q shouldEqual "field_1: *foo*"
  }

  it should "support fuzzy queries" in {
    ("field_1" =~= "foo").q shouldEqual "field_1: foo~"
    ("field_1" =~= "foo" at 1).q shouldEqual "field_1: foo~1"
    ("field_1" =~= "foo" at 2).q shouldEqual "field_1: foo~2"
  }

  it should "compose and-links" in {
    (("field_1" =:= "Frank") and ("field_2" =:= 4)).q shouldEqual """(field_1: "Frank" and field_2: 4)"""
  }

  it should "compose or-links" in {
    (("field_1" =:= "Frank") or ("field_2" =:= 4)).q shouldEqual """(field_1: "Frank" or field_2: 4)"""
  }
}
