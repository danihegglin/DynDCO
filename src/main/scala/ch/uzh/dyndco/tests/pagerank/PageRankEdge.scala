package ch.uzh.dyndco.algorithms.tests.pagerank

import com.signalcollect.DefaultEdge

class PageRankEdge(targetId: Int)
    extends DefaultEdge(targetId) {
  type Source = PageRankVertex
  def signal = source.state * weight / source.sumOfOutWeights
}