package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder

class MaxSumGraph (
    varVertices_ : Set[VariableVertex], 
    funcVertices_ : Set[FunctionVertex], 
    neighbourhoods_ : Map[Int, Map[VariableVertex,FunctionVertex]], 
    graph_ : Graph[Any,Any]) {
  
  var varVertices = varVertices_
  var funcVertices = funcVertices_
  var neighbourhoods = neighbourhoods_
  var graph = graph_
  
}
