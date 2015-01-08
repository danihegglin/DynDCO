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
     * Finish Control
     */
    var finished : Boolean = false
    var run : Boolean = true
    
    override def scoreSignal: Double = {
      if(this.finished) 0
      else 1
    }
    
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
      
        while(run){
          
          checkFinish()
          
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
        var nextMeetingProb : Double = parameters(1).toDouble
        var nextAgentProb : Double = parameters(2).toDouble
        var removeProb : Double = parameters(3).toDouble
        var number : Int = parameters(4).toInt
        
        while(run){
          
          for(change <- 1 to number){
          
            // Probabilities
            var first = Random.nextDouble()
            var second = Random.nextDouble()
            var third = Random.nextDouble()
            
            // get meeting
            var meetingId = 
              if(first < nextMeetingProb)
                Random.nextInt(dynamicGraph.numOfNeighbourhoods())
              else
                dynamicGraph.nextNeighbourhood()
                
           // get agent
           var isNew = false
           var agentId =
             if(second < nextAgentProb)
               Random.nextInt(dynamicGraph.numOfAgents())
             else{
               isNew = true
               dynamicGraph.nextAgent()
             }
            
            // action
            if(third < removeProb){
              dynamicGraph.getFactory().addAgent(dynamicGraph, problem, agentId, meetingId)
            }
            else {
                if(!isNew){
                  try {
                    dynamicGraph.getFactory().removeAgent(dynamicGraph, agentId, meetingId)
                  }
                  catch {
                    case e : Exception => e.printStackTrace()                    
                  }
                }
            }
            
          }
          
          Thread sleep interval
        }   
    }
    
    private def checkFinish(){
      var isFinished = true
      for(vertex <- dynamicGraph.getAgents()){
        if(!vertex.finished)
          isFinished = false
      }
      
      if(isFinished){
        run = false
        finished = true
      }
    }

}