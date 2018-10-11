package ch.acmesoftware.typesolr.querydsl

import ch.acmesoftware.typesolr.querydsl.Query.{And, Or}

trait Query { self =>

  def and(q: Query): Query = And(this, q)

  def or(q: Query): Query = Or(this, q)

  def build: String = compile(self)

  private def compile(query: Query): String = query match {
    case And(a, b) => s"""(${compile(a)} and ${compile(b)})"""
    case Or(a, b) => s"""(${compile(a)} or ${compile(b)})"""
    case ExactFieldQuery(key, value: String) => s"""${escapeField(key)}: ${enquote(value)}""" // make things more secure
    case ExactFieldQuery(key, value) => s"""${escapeField(key)}: ${processValue(value)}"""
    case WildcardFieldQuery(key, value, wildcardType) => s"""${escapeField(key)}: ${wildcardType.applyTo(processValue(value))}"""
    case FuzzyFieldQuery(key, value, fuzzyness) =>
      val fuzzyOp = "~" + fuzzyness.map(_.toString).getOrElse("")
      s"""${escapeField(key)}: ${processValue(value)}$fuzzyOp"""
  }

  private def enquote(s: String) = "\"" + s.replace("\"", "\'") + "\""

  private def processValue[T](value: T): String = value match {
    case s: String => s
    case b: Boolean => if(b) "true" else "false"
    case a => a.toString
  }

  private def escapeField(name: String) = name.replace(":", "-").replace("(", "_").replace(")", "_") //TODO: complete
}

object Query {

  case class And(a: Query, b: Query) extends Query

  case class Or(a: Query, b: Query) extends Query

}