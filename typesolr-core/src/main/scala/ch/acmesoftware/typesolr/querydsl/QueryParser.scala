package ch.acmesoftware.typesolr.querydsl

import org.apache.solr.client.solrj.SolrQuery

import scala.annotation.tailrec

object QueryParser {

  def parse[T <: Query](query: T): SolrQuery = query match {
    case q: HighlightedQuery[_] => withHighlighting(parse(q.query), q)
    case q: Query => new SolrQuery(q.q)
  }

  private def withHighlighting(sq: SolrQuery, q: HighlightedQuery[_]): SolrQuery = {
    val sqn = sq.getCopy
    sqn.setHighlight(true)
    q.hlFieldNames.foreach(sqn.addHighlightField)
    q.hlPreTag.foreach(sqn.setHighlightSimplePre)
    q.hlPostTag.foreach(sqn.setHighlightSimplePost)
    sqn
  }
}
