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
import ch.uzh.dyndco.stack.tests.TestMode
import ch.uzh.dyndco.stack.configuration.Configuration
import ch.uzh.dyndco.stack.configuration.ConfigurationFactory

object SingleTest extends DeployableAlgorithm {
  
  def execute(parameters: Map[String, String], nodeActors: Array[ActorRef]){
  
      /**
    	 * Parameters
    	 */
      val DENSITY : Double = parameters.apply("density").toDouble
      val ALGORITHM : String = parameters.apply("algorithm")
      val EXECUTION : String = parameters.apply("execution")
      val MODE : String = parameters.apply("mode")
      val PARAM : String = parameters.apply("param")
    	val TIMESLOTS : Int = parameters.apply("timeslots").toInt
      val MEETINGS : Int = parameters.apply("meetings").toInt
    	val AGENTS : Int = parameters.apply("agents").toInt
      
      /**
       * Build id
       */
      val id = IdFactory.build(ALGORITHM, EXECUTION, MODE, PARAM, DENSITY, TIMESLOTS, MEETINGS, AGENTS, 1)
  
      /**
       * Build problem
       */
      val problem = MeetingSchedulingFactory.build(TIMESLOTS, MEETINGS, AGENTS, DENSITY)
      
      /**
       * Build configuration
       */  
      var configuration = ConfigurationFactory.build(EXECUTION, MODE, PARAM)
                        
      /**
       * Run graph
       */
      Monitoring.start(id)
      
      ALGORITHM match {
        case "maxsum" => MaxSum.run(problem, configuration)
        case "mgm" => MGM.run(problem, configuration)
        case "dpop" => DPOP.run(problem, configuration)
      }
      
      Monitoring.sucess(id)
      
      Thread sleep 10000
      System.exit(0)
    
  }
       
}
