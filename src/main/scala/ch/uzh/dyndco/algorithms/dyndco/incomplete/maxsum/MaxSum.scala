package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import org.slf4j.LoggerFactory
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList

object MaxSum extends App {

	/**
	 * Logger
	 */
	//   val log = Logger(LoggerFactory.getLogger("name"))
  
  /**
   * Tools
   */
  var random : Random = new Random

	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 28
	var AGENTS : Int = 3
	var MEETINGS_NUM : Int = 2
	var HARD_CONSTRAINT_PROB : Double = 0.2
	
	/**
	 * Functions
	 */
	
	def buildMeetings(meetingsNum : Int) : MutableList[Meeting] = {
	  var meetings = MutableList[Meeting]()
	  for(meeting <- 1 to meetingsNum){
	    meetings += (new Meeting(meeting))
	  }
	  meetings
	}
	
	def buildParticipations() : Set[Int] = {
	  
	   println("building participations")
	   var participationsAmount : Int = random.nextInt(MEETINGS_NUM) + 1
	   println("possible participations: " + participationsAmount)
	   var participations : Set[Int] = Set[Int]()
	   for(partAmount <- 1 to participationsAmount){
		   var done : Boolean = false
				   while(done == false){						  
					   var participation = random.nextInt(MEETINGS_NUM)
							   println("participation: " + participation)
							   if(!participations.contains(participation)){
								   participations += participation
										   done = true
							   }
				   }
	   }
	  participations
	}
	
	def buildTimeslots() : MutableList[Int] = {					
	  var availableTimeslots = MutableList[Int]()
	  for(timeslot <- 1 to TIMESLOTS){
	    availableTimeslots += timeslot
	  }	
		availableTimeslots
	}
	
	def buildPreferences(participations : Set[Int], availableTimeslots : MutableList[Int]) : Map[Any,Int] = {
	  var preference : Map[Any,Int] = Map[Any,Int]()
		for(participation <- participations){
		  println("preference " + participation)
			var timeslot = random.nextInt(availableTimeslots.size)
			preference += (participation -> availableTimeslots.apply(timeslot))
			availableTimeslots.drop(timeslot)
		}
	  preference
	}
	
	def buildHardConstraints(availableTimeslots : MutableList[Int]) : Set[Int] = {
			var available = random.nextInt(availableTimeslots.size) + 1
			var numOfHardConstraints : Int = available / 3 // FIXME
			var hard: Set[Int] = Set()
			for(hardConstraint <- 1 to numOfHardConstraints){
				var timeslot = random.nextInt(availableTimeslots.size) + 1
				hard += availableTimeslots.apply(timeslot -1)
				availableTimeslots.drop(timeslot)
			}
			hard
	}
	
	def buildSoftConstraints(availableTimeslots : MutableList[Int]) : Set[Int] = {
			var soft: Set[Int] = Set()
			for(softConstraint <- availableTimeslots){
			  if(!softConstraint.isNaN()){
				  soft + availableTimeslots.apply(softConstraint -1)
				}
			}
			soft
	}
	
	def addParticipationsToMeetings(variableVertex : VariableVertex, participations : Set[Int]){
	  println("meetings pre: " + participations.size)
	  for(participation <- participations){
	    var meeting : Meeting = meetings(participation)
	    meeting.addParticipant(variableVertex)
	    meetings += meeting
	  }
	  println("meetings post: " + participations.size)
	}
	
				val graph = GraphBuilder.withConsole(true,8091).build

	     /**
	      * Build meetings
	      */
       println("=================== Preparing Meetings ===================")
			 var meetings : MutableList[Meeting] = buildMeetings(MEETINGS_NUM)
			 println("meetings num: " + MEETINGS_NUM)
			 println("built meetings: " + meetings.size)
			 
			 println("=================== Preparing Agents ===================")

			/**
			 * Build agents and their participations, constraints
			 */
			val allParticipations : Map[VariableVertex, Set[Int]] = Map[VariableVertex, Set[Int]]() // vertex to participations
			for(agent <- 1 to AGENTS){
			  
			  println("++++++++++++++++++++++++ " + agent + " ++++++++++++++++++++++++")
			  
			  // Build participations
			  println("------------------- Meeting Participations -------------")
        var participations : Set[Int] = buildParticipations()
        println("participations: " + participations)
			  
        // Build constraints
			  println("------------------- Constraints -------------")

			  // timeslots
			  println("building available timeslots")
			  val availableTimeslots : MutableList[Int] = buildTimeslots();
			  println("availableTimeslots: " + availableTimeslots.size)

			  // preferences
				println("preferences")
				var preference : Map[Any,Int] = buildPreferences(participations,availableTimeslots)
				println("preferences: " + preference)

				// hard constraints
			  println("building hard constraints")
			  var hard : Set[Int] = buildHardConstraints(availableTimeslots)
			  println("hardconstraints: " + hard)

			  // soft constraints
			  println("building softConstraint")
				var soft : Set[Int] = buildSoftConstraints(availableTimeslots)
				println("softconstraint: " + soft)

				// proposal
				println("building proposal")
				var constraints = new Proposal(agent,hard,soft,preference)

				// vertex
			  println("creating variablevertex")
				var varVertex = new VariableVertex(agent,constraints,TIMESLOTS)
				graph.addVertex(varVertex)
				
				// add participation to meeting objects
				addParticipationsToMeetings(varVertex, participations)
				
				// add to participations collection
				allParticipations += (varVertex -> participations)
			}
						
		  /**
			 *  process participations
			 */
       
      // Establish FunctionVertex for every participation and every participant and build edges
			for(variableVertex : VariableVertex <- allParticipations.keys){
			  
			  println("processing variable vertex: " + variableVertex.id)
			  
			   var meetingIds : Set[Int] = allParticipations.apply(variableVertex)
			   for(meetingId <- meetingIds){
			     
			     println("building meeting edges " + meetingIds.size)
			     
			     // Get Meeting Information
			     var meeting : Meeting = meetings.apply(meetingId)
  			   var participants : Int = meeting.participants.size
  			   
  			   println("number of participants" + participants)
  			   
  			   // Process Functions
  			   var functions : Set[FunctionVertex] = Set()
  			   for(function <- 1 to participants){
  			     
  			     println("function: " + function)
  			     var variableId : Any = variableVertex.id;
  			     var functionId : Any = "v" + variableVertex.id + "f" + function
  			     var functionVertex : FunctionVertex = new FunctionVertex(functionId, null, TIMESLOTS)
  			     functions + (functionVertex);
  			     graph.addVertex(functionVertex)
  			     graph.addEdge(variableId, new StateForwarderEdge(functionId))
  			     graph.addEdge(functionId, new StateForwarderEdge(functionId))
  			   }
  			   
  			   meeting.addParticipantFunctions(variableVertex, functions)
			   }
			} 
			
			// Find function vertices which are aimed at oneself, build edges
			for(variableVertex : VariableVertex <- allParticipations.keys){
			    var meetingIds : Set[Int] = allParticipations.apply(variableVertex)
			    for(meetingId <- meetingIds){
			      
			      // Get Meeting
			     var meeting : Meeting = meetings.apply(meetingId)
			      
			     // Get other Functions
			      var targetFunctions : Set[FunctionVertex] = meeting.getOtherFunctions(variableVertex)
			      for(targetFunction <- targetFunctions){
			         graph.addEdge(variableVertex.id, new StateForwarderEdge(targetFunction.id))
			         graph.addEdge(targetFunction.id, new StateForwarderEdge(variableVertex.id))
			      }
			    }
			} 
				     
      /**
			 * Start the graph
			 */
						
			// start
			println("Start graph")
			val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
			val stats = graph.execute(execConfig)
					
			// show run info
			println(stats)
			graph.foreachVertex(println(_))
					
			// show results
			for(variableVertex <- allParticipations.keys){
			  println("----------" + variableVertex.id + "---------------")
				variableVertex.show()
			}

			// shutdown graph
			graph.shutdown

}
