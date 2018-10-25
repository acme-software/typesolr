package ch.acmesoftware.typesolr.querydsl

import ch.acmesoftware.typesolr.querydsl.WildcardType.NoWildcard

trait FieldQuery extends Query with HighlightDslWord

final case class ExactFieldQuery[T](name: String, value: T) extends FieldQuery {
  override def q: String = value match {
    case v: String => s"""${escapeField(name)}: ${enquote(v)}"""
    case v => s"""${escapeField(name)}: ${processValue(value)}"""
  }
}

final case class WildcardFieldQuery[T](name: String, value: T, wildcardType: WildcardType = NoWildcard) extends Query {
  override def q: String = s"""${escapeField(name)}: ${wildcardType.applyTo(processValue(value))}"""
}

final case class FuzzyFieldQuery[T](name: String, value: T, fuzzyAmount: Option[Int] = None) extends Query {

  def at(amount: Int):FuzzyFieldQuery[T] = copy(fuzzyAmount = Some(amount))

  override def q: String = {
    val fuzzyOp = "~" + fuzzyAmount.map(_.toString).getOrElse("")
    s"""${escapeField(name)}: ${processValue(value)}$fuzzyOp"""
  }
}

