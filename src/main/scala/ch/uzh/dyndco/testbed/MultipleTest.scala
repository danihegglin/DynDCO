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
import ch.uzh.dyndco.stack.tests.TestMode
import ch.uzh.dyndco.stack.configuration.ConfigurationFactory
import ch.uzh.dyndco.algorithms.dpop.DPOP

object MultipleTest extends DeployableAlgorithm {
  
  def execute(parameters: Map[String, String], nodeActors: Array[ActorRef]){
	
    /**
     * Testconfigs
     */
    val ALGORITHM : String = parameters.apply("algorithm")
    val EXECUTION : String = parameters.apply("execution")
    val MODE : String = parameters.apply("mode")
    val PARAM : String = parameters.apply("param")
    val TIMESLOTS : Int = parameters.apply("timeslots").toInt
    val MEETINGS : Int = parameters.apply("meetings").toInt
    val AGENTS : Int = parameters.apply("agents").toInt
    val RUNS : Int = parameters.apply("runs").toInt
    val FACTOR : Int = parameters.apply("factor").toInt
    val MAX : Int = parameters.apply("max").toInt
  	
    /**
     * Build execution mode
     */
    var execConfig : ExecutionConfiguration[Any, Any] = null
    EXECUTION match {
      case "asynchronous" => execConfig 
        = ExecutionConfiguration.withExecutionMode(ExecutionMode.OptimizedAsynchronous)
      case "synchronous" => execConfig 
        = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
    }
    
    /**
     * Build configuration
     */
    var configuration = ConfigurationFactory.build(EXECUTION, MODE, PARAM)
    
    /**
     * Run tests
     */
    var timeslots : Int = TIMESLOTS
    var meetings : Int = MEETINGS
    var agents : Int = AGENTS
    
    while(agents < MAX){
      while(meetings < MAX){
        for(run <- 1 to RUNS){
          
          println("RUN " + run + " of " + RUNS + " (AGENTS: "+agents+", MEETINGS: "+meetings+")")
          
          val runID = IdFactory.build(ALGORITHM, EXECUTION, MODE, timeslots, meetings, agents, run)
          val problem = MeetingSchedulingFactory.build(timeslots,meetings,agents)
          
          Monitoring.start(runID)
          
          ALGORITHM match {
            case "maxsum" => MaxSum.run(problem, configuration)
            case "mgm" => MGM.run(problem, configuration)
            case "dpop" => DPOP.run(problem, configuration)
          }
          
          Monitoring.sucess(runID)
          
          // Clear memory
          Runtime.getRuntime().gc;
        }
        
        // Increase meetings
        FACTOR match {
          case 1 => meetings += FACTOR
          case _ => meetings *= FACTOR 
        }
      }
      
      // Increase Agents
      FACTOR match {
        case 1 => agents += FACTOR
        case _ => agents *= FACTOR
      }
      
      meetings = 1
    }
    
  }
	
}
