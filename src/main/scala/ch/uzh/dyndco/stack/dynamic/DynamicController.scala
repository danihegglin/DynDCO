package ch.uzh.dyndco.stack.dynamic

import com.signalcollect.Graph
import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.maxsum.VariableVertex
import ch.uzh.dyndco.problems.Problem
import scala.util.Random
import ch.uzh.dyndco.stack.graph.DynamicGraph
import ch.uzh.dyndco.stack.vertex.DynamicVertex

class DynamicController (
    id: Any, 
    dynamicGraph : DynamicGraph, 
    problem : Problem) 
  extends DataGraphVertex(id, dynamicGraph) {
    
    /**
     * The collect function
     */
    def collect() = {
      null
    }
    
    /**
     * Changes constraints of a percentage of agents on certain interval
     */
    def changeConstraints(parameters : Array[String]){
      
        var interval : Int = parameters(0).toInt
        var percentage : Double = parameters(1).toDouble
      
        while(true){
          
          // choose agents
          var agents = Set[DynamicVertex]()
          var numToPick = Math.floor(dynamicGraph.numOfAgents() * percentage)
          var allAgents = dynamicGraph.getAgents().toList
          while(agents.size < numToPick){
            agents += allAgents(Random.nextInt(allAgents.size))
          }
          
          // change constraints
          for(agent <- agents){
            agent.changeConstraints()
          }
         
          Thread sleep interval
        }      
    }
    
    /**
     * Changes meetings on a certain interval
     */
    def changeMeetings(parameters : Array[String]){
      
        var interval : Int = parameters(0).toInt
        var firstProb : Double = parameters(1).toDouble
        var secondProb : Double = parameters(2).toDouble
        var thirdProb : Double = parameters(3).toDouble
        var number : Int = parameters(4).toInt
        
        while(true){
          
          for(change <- 1 to number){
          
            // Probabilities
            var first = Random.nextDouble()
            var second = Random.nextDouble()
            var third = Random.nextDouble()
            
            // get meeting
            var meetingId = 
              if(first < firstProb)
                Random.nextInt(dynamicGraph.numOfNeighbourhoods())
              else
                dynamicGraph.nextNeighbourhood()
                
           // get agent
           var agentId =
             if(second < secondProb)
               Random.nextInt(dynamicGraph.numOfAgents())
             else
               dynamicGraph.nextAgent()
            
            // action
            if(third < thirdProb)
              dynamicGraph.getFactory().addAgent(dynamicGraph, problem, agentId, meetingId)
            else
              dynamicGraph.getFactory().removeAgent(dynamicGraph, agentId, meetingId)
            
          }
          
          Thread sleep interval
        }   
    }

}