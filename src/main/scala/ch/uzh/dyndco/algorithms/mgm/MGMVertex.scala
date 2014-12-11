package ch.uzh.dyndco.algorithms.mgm

import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.algorithms.mgm.MGMMessage
import ch.uzh.dyndco.problems.Constraints

 class MGMVertex (
    id: Any, 
    agentView: MGMMessage,
    timeslots: Int,
    constraints : Constraints,
    index : Map[Int, Map[Any, Int]]
    ) extends DynamicVertex(id, agentView){
//  
//  /• Send its current value ai
//to neighbors and receive values from neighbors.
//• Choose a value a
//∗
//i
//such that the local gain g
//∗
//i
//is maximised (assuming neighbors
//do not change value).
//• Send the gain g
//∗
//i
//to neighbors and receive gain from neighbors.
//• If the gain for the agent is the highest in the neighborhood, update the value
//of xi
//to a
//∗
//i
//.
  
	def collect() = {
	  
	}
  
  

}