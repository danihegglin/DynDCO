package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.maxsum.VariableVertex
import ch.uzh.dyndco.problems.Problem
import scala.util.Random

class DynamicController (id: Any, dynamicGraph : DynamicGraph, graphFactory : GraphFactory, problem : Problem) 
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
    def ConstraintsChange(interval : Int, percentage : Double){
        
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
    def MeetingChange(
        interval : Int, 
        firstProb : Double, 
        secondProb : Double, 
        thirdProb : Double, 
        number : Int){
        
        while(true){
          
          for(change <- 1 to number){
          
            // Probabilities
            var first = Random.nextDouble()
            var second = Random.nextDouble()
            var third = Random.nextDouble()
            
            // get meeting
            var meetingId = 
              if(first < firstProb)
                Random.nextInt(dynamicGraph.neighbourhoods.size)
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
              graphFactory.addAgent(dynamicGraph, problem, agentId, meetingId)
            else
              graphFactory.removeAgent(dynamicGraph, agentId, meetingId)
            
          }
          
          Thread sleep interval
        }   
    }

}