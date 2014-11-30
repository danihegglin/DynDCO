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
//  val log = Logger(LoggerFactory.getLogger("name"))
  
  /**
   * Tools
   */
  var random : Random = new Random

	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 5 // args(0)
	var AGENTS : Int = 3 // args(1)
	var MEETINGS_NUM : Int = 1 // args(2)
	var HARD_CONSTRAINT_PROB : Double = 0.2 // args(3)
	
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
	  
//	   println("building participations")
	   var participationsAmount : Int = random.nextInt(MEETINGS_NUM) + 1
//	   println("possible participations: " + participationsAmount)
	   var participations : Set[Int] = Set[Int]()
	   for(partAmount <- 1 to participationsAmount){
		   var done : Boolean = false
				   while(done == false){						  
					   var participation = random.nextInt(MEETINGS_NUM)
//							   println("participation: " + participation)
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
	
	def buildPreferences(participations : Set[Int], availableTimeslots : MutableList[Int]) : Map[Int,Int] = {
	  var preference : Map[Int,Int] = Map[Int,Int]()
		for(participation <- participations){
//		  println("preference " + participation)
			var timeslot = random.nextInt(availableTimeslots.size)
			preference += (participation -> availableTimeslots.apply(timeslot))
		}
	  preference
	}
	
	def buildHardConstraints(availableTimeslots : MutableList[Int], used : Set[Int]) : Set[Int] = {
			var available = random.nextInt(availableTimeslots.size) + 1
			var numOfHardConstraints : Int = available / 3 // FIXME
			var hardConstraints: Set[Int] = Set()
			for(hardConstraint <- 1 to numOfHardConstraints){
				var timeslot = -1
				while (timeslot < 0){
					var proposedTimeslot : Int = random.nextInt(availableTimeslots.size)
					if(!used.contains(proposedTimeslot)){
						timeslot = proposedTimeslot
					}
				}
				hardConstraints += timeslot
			}
			hardConstraints
	}
	
	def buildSoftConstraints(availableTimeslots : MutableList[Int], used : Set[Int]) : Set[Int] = {
			var softConstraints: Set[Int] = Set()
			for(availableTimeslot <- availableTimeslots){
			  if(!used.contains(availableTimeslot)){
				  softConstraints += availableTimeslot
				}
			}
			softConstraints
	}
	
	def buildVerticesAndEdges(agent : Int, constraints : Proposal) = {
	  
		// variable vertex
//		println("creating variable vertex")
		var variableId : Any = "v" + agent
		var varVertex = new VariableVertex(variableId,constraints,TIMESLOTS)
		variableVertices += (agent -> varVertex)
		graph.addVertex(varVertex)
				
		// function vertex
//		println("creating function vertex")
		var functionId : Any = "f" + agent
		var funcVertex = new FunctionVertex(functionId, constraints, TIMESLOTS)
		functionVertices += (agent -> funcVertex)
		graph.addVertex(funcVertex)
			  	
		// build edges
		graph.addEdge(variableId, new StateForwarderEdge(functionId))
		graph.addEdge(functionId, new StateForwarderEdge(variableId))
	}
	
	def addParticipationsToMeetings(agent : Int, participations : Set[Int]){
//	  println("meetings pre: " + participations.size)
	  for(participation <- participations){
	    var meeting : Meeting = meetings(participation)
	    meeting.addParticipant(agent)
	    meetings += meeting
	  }
//	  println("meetings post: " + participations.size)
	}
	
		/**
		 * Initializae Graph
		 */
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
			 * Build agents and their participations, constraints, VariableVertex/FunctionVertex
			 */
			val variableVertices : Map[Int, VariableVertex] = Map()
			val functionVertices : Map[Int, FunctionVertex] = Map()
			val participationsIndex : Map[Int, Set[Int]] = Map[Int, Set[Int]]() // vertex to participations
			
			
			for(agent <- 1 to AGENTS){
			  
			  println("--- " + agent + " ---")
			  
			  // Build participations
//			  println("------------------- Meeting Participations -------------")
        var participations : Set[Int] = buildParticipations()
//        println("participations: " + participations)
			  
        // Build constraints
//			  println("------------------- Constraints -------------")

			  // timeslots
//			  println("building available timeslots")
			  val availableTimeslots : MutableList[Int] = buildTimeslots();
//			  println("availableTimeslots: " + availableTimeslots.size)

			  // preferences
//			 println("preferences")
			 var preferences : Map[Int,Int] = buildPreferences(participations,availableTimeslots)
			 println("preferences: " + preferences)
			 
			 // initialized used set
			 var used : Set[Int] = Set()
			 for(preference <- preferences.values){
			   used += preference
			 }

				// hard constraints
//			  println("building hard constraints")
			  var hardConstraints : Set[Int] = buildHardConstraints(availableTimeslots, used)
			  println("hardconstraints: " + hardConstraints)
			  
			  // extend used set
			  for(hardConstraint <- hardConstraints){
				  used += hardConstraint
			  }

			  // soft constraints
//			  println("building softConstraint")
				var softConstraints : Set[Int] = buildSoftConstraints(availableTimeslots, used)
				println("softconstraint: " + softConstraints)

				// proposal
//				println("building proposal")
				var constraints = new Proposal(agent,hardConstraints,softConstraints,preferences)

			  	// vertices & edges
			  	buildVerticesAndEdges(agent, constraints)
				
				// add participation to meeting objects
				addParticipationsToMeetings(agent, participations)
				
				// add to participations collection
				participationsIndex += (agent -> participations)
			}
						
		  /**
		   *  process participations
		   */
       
      // Establish FunctionVertex for every participation and every participant and build edges
			for(agent : Int <- participationsIndex.keys){
			  
//			  println("processing agent: " + agent)
			  val connectedAgents = Set[Int]()
			  
//			  	println("processing meetings")
			   var meetingIds : Set[Int] = participationsIndex.apply(agent)
			   for(meetingId <- meetingIds){
			     
//			     println("building meeting edges " + meetingIds.size)
			     
			     // Get Meeting Information
			     var meeting : Meeting = meetings.apply(meetingId)
  			   var participants : Int = meeting.participants.size
  			   
//  			   println("number of participants" + participants)
  			   
  			   // Process Functions
//  			   var functions : Set[FunctionVertex] = Set()
  			   for(participant <- 1 to participants){
  			     
  			     if(connectedAgents.contains(participant)){
//  			       println("participant already added")
  			     }
  			     else {
//	  			     println("participant: " + participant)
	  			     var agentVariableId : Any = "v" + agent
	  			     var particpantFunctionId : Any = "f" + participant
	  			     graph.addEdge(agentVariableId, new StateForwarderEdge(particpantFunctionId))
	  			     graph.addEdge(particpantFunctionId, new StateForwarderEdge(agentVariableId))
  			   	}
  			   }
			  }
			} 
			
			/**
			 * Start the graph
			 */
						
			// start
			println("--------------------------------")
			println("Start graph")
			println("--------------------------------")
			val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
			val stats = graph.execute(execConfig)
					
			// show run info
			println(stats)
			graph.foreachVertex(println(_))
					
			// show results
			for(variableVertex <- variableVertices.values){
			  println("----------" + variableVertex.id + "---------------")
				variableVertex.show()
			}

			// shutdown graph
			graph.shutdown
}
