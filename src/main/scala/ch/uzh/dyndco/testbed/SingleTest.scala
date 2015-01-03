package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.util.IdFactory
import ch.uzh.dyndco.algorithms.mgm.MGM
import ch.uzh.dyndco.algorithms.dpop.DPOP

object SingleTest extends App {
  
    /**
  	 * Configuration
  	 */
  	var TIMESLOTS : Int = 50
    var MEETINGS : Int = 10
  	var AGENTS : Int = 20
    
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
//    MGM.run(problem)
//    DPOP.run(problem)
    Monitoring.sucess(id)
    
    System.exit(0)
       
}
