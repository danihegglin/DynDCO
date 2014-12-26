package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.util.IdFactory

object MultipleTest extends DeployableAlgorithm {
  
  def execute(parameters: Map[String, String], nodeActors: Array[ActorRef]){
	
    /**
  	 * Configuration FIXME make arguments
  	 */
//  	var TIMESLOTS : Int = 20 // args(0)
//  	var AGENTS : Int = 20 // args(1)
//  	var MEETINGS : Int = 2 // args(2)

    /**
     * Testconfigs
     */
    var RUNS : Int = 10 // Should be enough
    var FACTOR : Int = 2 // If 10 is ok, 2 should also be ok
    var MAX : Int = 10000 // This should really be enough
  	
  	/**
  	 * Signal Collect Mode
  	 */
//  	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
    
    /**
     * Run tests
     */
    var agents : Int = 2
    var meetings : Int = 1
    var timeslots : Int = 20
    while(agents < MAX){
      while(meetings < MAX){
        println("--- AGENTS " + agents + " ---")
        println("--- MEETINGS " + meetings + " ---")
        for(run <- 1 to RUNS){
          println("--- RUN " + run + " of " + RUNS + " ---")
          val runID = IdFactory.build(timeslots, meetings, agents, run)
          val problem = MeetingSchedulingFactory.build(timeslots,meetings,agents)
          Monitoring.start(runID)
          MaxSum.run(problem)
          Monitoring.sucess(runID)
        }
        meetings *= FACTOR
      }
      agents *= FACTOR
      meetings = 1
    }
    
  }
	
}
