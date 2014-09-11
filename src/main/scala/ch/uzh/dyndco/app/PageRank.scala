package ch.uzh.dyndco.app

import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.vertices.PageRankVertex
import ch.uzh.dyndco.edges.PageRankEdge

object PageRank extends App {
  val graph = GraphBuilder.build
  graph.addVertex(new PageRankVertex(1))
  graph.addVertex(new PageRankVertex(2))
  graph.addEdge(1, new PageRankEdge(2))
  graph.addEdge(2, new PageRankEdge(1))
  val stats = graph.execute
  graph.foreachVertex(println(_))
  println(stats)
  graph.shutdown
}