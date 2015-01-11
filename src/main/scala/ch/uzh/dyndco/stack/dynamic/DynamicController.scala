package ch.uzh.dyndco.stack.dynamic

import com.signalcollect.Graph
import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.maxsum.VariableVertex
import ch.uzh.dyndco.problems.Problem
import scala.util.Random
import ch.uzh.dyndco.stack.graph.DynamicGraph
import ch.uzh.dyndco.stack.vertex.DynamicVertex

class DynamicController (
    dynamicGraph : DynamicGraph, 
    problem : Problem) {
  
   /**
     * Control
     */
    var isRunning : Boolean = true
    
    /**
     * Changes constraints of a percentage of agents on certain interval
     */
    def changeConstraints(parameters : Array[String]){
      
        new Thread(new Runnable(){
          
          def run(){
            
            // parameters
            var interval : Int = parameters(0).toInt
            var percentage : Double = parameters(1).toDouble
            
            // wait on graph
            while(!isStarted){}
          
            // run
            while(isRunning){
              
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
                agent.finished = false
              }
              
              // reactivate agents
              for(agent <- allAgents){
                agent.finished = false
              }
             
              Thread sleep interval
            } 
          }
        }).start();
    }
    
    /**
     * Changes agents on a certain interval
     */
    def changeVariables(parameters : Array[String]){
      
      new Thread(new Runnable(){
          
          def run(){
            
            // parameters
            var interval : Int = parameters(0).toInt
            var nextMeetingProb : Double = parameters(1).toDouble
            var nextAgentProb : Double = parameters(2).toDouble
            var removeProb : Double = parameters(3).toDouble
            var number : Int = parameters(4).toInt
            
            // wait on graph
            while(!isStarted){}
      
           while(isRunning){
              
              checkFinish()
          
              for(change <- 1 to number){
              
                // get meeting
                var meetingId = 
                  if(Random.nextDouble() < nextMeetingProb)
                    Random.nextInt(dynamicGraph.numOfNeighbourhoods())
                  else
                    dynamicGraph.nextNeighbourhood()
                    
               // get agent
               var isNew = false
               var agentId =
                 if(Random.nextDouble() < nextAgentProb)
                   Random.nextInt(dynamicGraph.numOfAgents())
                 else{
                   isNew = true
                   dynamicGraph.nextAgent()
                 }
                
                // get action
                if(Random.nextDouble() < removeProb){
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
        }).start(); 
    }
    
    /**
     * Change domain
     */
    def changeDomain(parameters : Array[String]){
      
       new Thread(new Runnable(){
          
          def run(){
            
            // Parameters
            var interval : Int = parameters(0).toInt
            var percentage : Double = parameters(1).toDouble
            var increaseProb : Double = parameters(2).toDouble
            
            // wait on graph
            while(!isStarted){}
            
            while(isRunning){
              
              checkFinish()
              
              // change domain
              if(Random.nextDouble() > increaseProb){
                
                // increase
                problem.increaseDomain(percentage)
                val newDomain = problem.getDomain();
                for(agent <- dynamicGraph.getAgents()){
                  agent.changeDomain(newDomain)
                }
                
              } else {
                
                // decrease
                problem.decreaseDomain(percentage)
                val newDomain = problem.getDomain()
                for(agent <- dynamicGraph.getAgents()){
                  agent.changeDomain(newDomain)
                }
                
              }
            
            
              Thread sleep interval
            } 
          }
        }).start();
      
    }
    
    /**
     * Helper functions
     */
    private def isStarted() : Boolean = {
      true // FIXME
    }
    
    private def checkFinish(){
      var isFinished = true
      for(vertex <- dynamicGraph.getAgents()){
        if(!vertex.finished)
          isFinished = false
      }
      
      if(isFinished){
        isRunning = false
      }
    }

}

