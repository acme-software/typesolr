package ch.acmesoftware.typesolr

package object querydsl {

  implicit class FieldOps(name: String) {

    def =:=[T](value: T): ExactFieldQuery[T] = exact(value)

    def exact[T](value: T) = ExactFieldQuery(name, value)

    def =*:=[T](value: String): WildcardFieldQuery[String] = wildcard(value, wildcardType = WildcardType.Left)

    def =:*=[T](value: String): WildcardFieldQuery[String] = wildcard(value, wildcardType = WildcardType.Right)

    def =*:*=[T](value: String): WildcardFieldQuery[String] = wildcard(value, wildcardType = WildcardType.Both)

    def wildcard[T](value: String, wildcardType: WildcardType = WildcardType.Both) = WildcardFieldQuery(name, value, wildcardType = wildcardType)

    def =~=(value: String): FuzzyFieldQuery[String] = fuzzy(value)

    def fuzzy(value: String) = FuzzyFieldQuery(name, value)
  }

  implicit class StringOps(a: String) {
    def </>(b: String): (String, String) = (a, b)
    def ::(b: String): List[String] = List(a, b)
  }
}

