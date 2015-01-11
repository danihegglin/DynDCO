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
  val density = parser.option[String](List("density"), "s","density")
  val algorithm = parser.option[String](List("algorithm"), "s","algorithm")
  val execution = parser.option[String](List("execution"), "s","execution")
  val mode = parser.option[String](List("mode"), "s","mode")
  val param = parser.option[String](List("param"), "s","param")
  val timeslots = parser.option[String](List("timeslots"), "s","timeslots")
  val meetings = parser.option[String](List("meetings"), "s","meetings")
  val agents = parser.option[String](List("agents"), "s","agents")
  val runs = parser.option[String](List("runs"), "s","runs")
  val factoragents = parser.option[String](List("factoragents"), "s","factoragents")
  val factormeetings = parser.option[String](List("factormeetings"), "s","factormeetings")
  val maxagents = parser.option[String](List("maxagents"), "s","max-agents")
  val maxmeetings = parser.option[String](List("maxmeetings"), "s","max-meetings")
  parser.parse(args)
  
  // build map
  var parameters = Map[String, String]()
  try {
    parameters += ("density" -> density.value.get)
    parameters += ("algorithm" -> algorithm.value.get)
    parameters += ("execution" -> execution.value.get)
    parameters += ("mode" -> mode.value.get)
    parameters += ("param" -> param.value.get)
    parameters += ("timeslots" -> timeslots.value.get)
    parameters += ("meetings" -> meetings.value.get)
    parameters += ("agents" -> agents.value.get)
    parameters += ("runs" -> runs.value.get)
    parameters += ("factoragents" -> factoragents.value.get)
    parameters += ("factormeetings" -> factormeetings.value.get)
    parameters += ("maxagents" -> maxagents.value.get)
    parameters += ("maxmeetings" -> maxmeetings.value.get)
  } 
  catch {
    case e: Exception  => println("Too few arguments")
  }

  MultipleTest.execute(parameters,null)
      
  System.exit(0)
	
}
