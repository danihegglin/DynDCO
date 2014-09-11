package ch.uzh.dyndco.edges

import com.signalcollect.DefaultEdge
import ch.uzh.dyndco.vertices.PageRankVertex

class PageRankEdge(targetId: Int)
    extends DefaultEdge(targetId) {
  type Source = PageRankVertex
  def signal = source.state * weight / source.sumOfOutWeights
}