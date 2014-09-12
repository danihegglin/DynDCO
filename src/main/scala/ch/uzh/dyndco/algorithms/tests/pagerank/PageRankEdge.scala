package ch.uzh.dyndco.algorithms.tests.pagerank

import ch.uzh.dyndco.algorithms.tests.pagerank.PageRankVertex

class PageRankEdge(targetId: Int)
    extends DefaultEdge(targetId) {
  type Source = PageRankVertex
  def signal = source.state * weight / source.sumOfOutWeights
}