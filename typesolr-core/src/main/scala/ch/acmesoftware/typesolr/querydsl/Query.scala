package ch.acmesoftware.typesolr.querydsl

import ch.acmesoftware.typesolr.querydsl.Query.{And, Or}

trait Query { self =>

  def q: String

  def and(q: Query): Query = And(this, q)

  def or(q: Query): Query = Or(this, q)

  protected def enquote(s: String): String = "\"" + s.replace("\"", "\'") + "\""

  protected def processValue[T](value: T): String = value match {
    case s: String => s
    case a => a.toString
  }

  protected def escapeField(name: String) = name.replace(":", "-").replace("(", "_").replace(")", "_") //TODO: complete
}

object Query {

  case class And(a: Query, b: Query) extends Query with HighlightDslWord { self =>
    override def q: String = s"""(${a.q} and ${b.q})"""
  }

  case class Or(a: Query, b: Query) extends Query with HighlightDslWord {
    override def q: String = s"""(${a.q} or ${b.q})"""
  }

}