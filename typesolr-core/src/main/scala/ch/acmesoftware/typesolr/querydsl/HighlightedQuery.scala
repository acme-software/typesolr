package ch.acmesoftware.typesolr.querydsl

case class HighlightedQuery[T <: Query](query: T, hlFieldNames: Seq[String], hlPreTag: Option[String], hlPostTag: Option[String]) extends Query {
  override def q: String = query.q

  def t(prePost: (String, String)): HighlightedQuery[T] = betweenTags(prePost._1, prePost._2)

  def betweenTags(pre: String, post: String): HighlightedQuery[T] = copy(hlPreTag = Some(pre), hlPostTag = Some(post))
}

private[querydsl] trait HighlightDslWord { self: Query with HighlightDslWord =>

  def ^^(fieldName: String): HighlightedQuery[Query with HighlightDslWord] = highlight(Seq(fieldName))

  def ^^(fieldNames: Seq[String]): HighlightedQuery[Query with HighlightDslWord] = highlight(fieldNames)

  def highlight(fieldName: String): HighlightedQuery[Query with HighlightDslWord] = highlight(Seq(fieldName))

  def highlight(fieldNames: Seq[String]): HighlightedQuery[Query with HighlightDslWord] = HighlightedQuery(self, fieldNames, None, None)
}