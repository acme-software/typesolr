package ch.acmesoftware.typesolr.querydsl

sealed trait WildcardType {
  def applyTo(in: String): String
}

object WildcardType {

  case object Left extends WildcardType {
    override def applyTo(in: String): String = s"*$in"
  }

  case object Right extends WildcardType {
    override def applyTo(in: String): String = s"$in*"
  }

  case object Both extends WildcardType {
    override def applyTo(in: String): String = s"*$in*"
  }

  case object NoWildcard extends WildcardType {
    override def applyTo(in: String): String = in
  }

}