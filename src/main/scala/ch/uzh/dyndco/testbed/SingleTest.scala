package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.util.IdFactory

object SingleTest extends App {
  
    /**
  	 * Configuration
  	 */
  	var TIMESLOTS : Int = 20
    var MEETINGS : Int = 3
  	var AGENTS : Int = 15
    
    /**
     * Build id
     */
    val id = IdFactory.build(TIMESLOTS, MEETINGS, AGENTS, 1)

    /**
     * Build problem
     */
    val problem = MeetingSchedulingFactory.build(TIMESLOTS, MEETINGS, AGENTS)
    
    /**
     * Run graph
     */
    Monitoring.start(id)
    MaxSum.run(problem)
    Monitoring.sucess(id)
    
    System.exit(0)
       
}
