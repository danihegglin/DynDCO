package ch.uzh.dyndco.algorithms.mgm

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder

class MGMGraph (
    vertices_ : Set[MGMVertex], 
    neighbourhoods_ : Map[Int, Set[MGMVertex]], 
    graph_ : Graph[Any,Any]) {
  
  var vertices = vertices_
  var neighbourhoods = neighbourhoods_
  var graph = graph_
  
}
