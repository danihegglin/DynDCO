package ch.uzh.dyndco.algorithms.maxsum

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.Constraints
import dispatch._
import dispatch.Defaults._
import ch.uzh.dyndco.util.Monitoring
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.Problem
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

/**
 * Based on: FIXME
 */

object MaxSum {

  def run(problem : MeetingSchedulingProblem) = {

    /**
     * Build graph
     */
    val maxSumGraph = MaxSumGraphFactory.build(problem)
    
    /**
     * Run the graph
     */ 
    val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
    val stats = maxSumGraph.graph.execute(execConfig)
    maxSumGraph.graph.shutdown
    
    /**
     * Results
     */
    println(stats)
    
    for(meeting <- maxSumGraph.neighbourhoods.keys.toList.sorted){
      var correct = true
      var value = -1
      var wrong = Set[Int]()
      for(neighbour <- maxSumGraph.neighbourhoods.apply(meeting).keys){
        if(value < 0){
          value = neighbour.bestValueAssignment
        }
        else {
          if(value != neighbour.bestValueAssignment){
            correct = false
            wrong += neighbour.bestValueAssignment
          }
        }
      }
      
      if(correct){
        println("meeting " + meeting + " -> " + value)
      }
      else {
         println("meeting " + meeting + " -> " + value + ", " + wrong)
      }
    }
    
  }
}
