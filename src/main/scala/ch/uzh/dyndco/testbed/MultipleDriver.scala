package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef
import org.clapper.argot._
import ArgotConverters._

object MultipleDriver extends App {
  
  // parse parameters
  val parser = new ArgotParser("DynDCO: Multiple")
  val algorithm = parser.option[String](List("algorithm"), "s","algorithm")
  val execution = parser.option[String](List("execution"), "s","execution")
  val mode = parser.option[String](List("mode"), "s","mode")
  val param = parser.option[String](List("param"), "s","param")
  val timeslots = parser.option[String](List("timeslots"), "s","timeslots")
  val meetings = parser.option[String](List("meetings"), "s","meetings")
  val agents = parser.option[String](List("agents"), "s","agents")
  val runs = parser.option[String](List("runs"), "s","runs")
  val factor = parser.option[String](List("factor"), "s","factor")
  val max = parser.option[String](List("max"), "s","max")
  parser.parse(args)
  
  // build map
  var parameters = Map[String, String]()
  try {
    parameters += ("algorithm" -> algorithm.value.get)
    parameters += ("execution" -> execution.value.get)
    parameters += ("mode" -> mode.value.get)
    parameters += ("param" -> param.value.get)
    parameters += ("timeslots" -> timeslots.value.get)
    parameters += ("meetings" -> meetings.value.get)
    parameters += ("agents" -> agents.value.get)
    parameters += ("runs" -> runs.value.get)
    parameters += ("factor" -> factor.value.get)
    parameters += ("max" -> max.value.get)
  } 
  catch {
    case e: Exception  => println("Too few arguments")
  }

  MultipleTest.execute(parameters,null)
      
  System.exit(0)
	
}
