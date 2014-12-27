package ch.uzh.dyndco.algorithms.dpop

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.algorithms.dpop.DPOPVertex

class DPOPGraph (
    root_ : DPOPVertex, 
    middle_ : Map[Int, DPOPVertex], 
    leaf_ : Set[DPOPVertex], 
    graph_ : Graph[Any,Any]) {
  
  var root = root_
  var middle = middle_
  var leaf = leaf_
  var graph = graph_
  
}
