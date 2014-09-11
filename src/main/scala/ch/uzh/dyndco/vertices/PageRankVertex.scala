package ch.uzh.dyndco.vertices

import com.signalcollect.DataGraphVertex

class PageRankVertex(id: Int, baseRank: Double = 0.15)
    extends DataGraphVertex(id, baseRank) {
  type Signal = Double
  def dampingFactor = 1 - baseRank
  def collect = baseRank + dampingFactor * signals.sum
}