package ch.acmesoftware.typesolr.querydsl

import ch.acmesoftware.typesolr.querydsl.WildcardType.NoWildcard

trait FieldQuery extends Query

final case class ExactFieldQuery[T](name: String, value: T) extends FieldQuery

final case class WildcardFieldQuery[T](name: String, value: T, wildcardType: WildcardType = NoWildcard) extends Query

final case class FuzzyFieldQuery[T](name: String, value: T, fuzzyAmount: Option[Int] = None) extends Query {

  def at(amount: Int):FuzzyFieldQuery[T] = copy(fuzzyAmount = Some(amount)) // TODO: Use refined types for parameter
}

