package ch.acmesoftware.typesolr

package object querydsl {

  implicit class Field(name: String) {

    def =:=[T](value: T) = ExactFieldQuery(name, value)

    def =*:=[T](value: String) = WildcardFieldQuery(name, value, wildcardType = WildcardType.Left)

    def =:*=[T](value: String) = WildcardFieldQuery(name, value, wildcardType = WildcardType.Right)

    def =*:*=[T](value: String) = WildcardFieldQuery(name, value, wildcardType = WildcardType.Both)

    def =~=(value: String) = FuzzyFieldQuery(name, value)
  }
}

