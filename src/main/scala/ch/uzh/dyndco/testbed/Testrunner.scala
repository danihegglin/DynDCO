package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef

object Testrunner extends DeployableAlgorithm {
  
  def execute(parameters: Map[String, String], nodeActors: Array[ActorRef]){
	
    /**
  	 * Configuration FIXME make arguments
  	 */
  	var TIMESLOTS : Int = 20 // args(0)
  	var AGENTS : Int = 3 // args(1)
  	var MEETINGS : Int = 2 // args(2)
  	
  	/**
  	 * Signal Collect Mode
  	 */
//  	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
    
    /**
     * Build problem
     */
    val problem = MeetingSchedulingFactory.build(TIMESLOTS,MEETINGS,AGENTS)
    
    /**
     * Run tests
     */
    MaxSum.run(problem)
    
  }
	
}
