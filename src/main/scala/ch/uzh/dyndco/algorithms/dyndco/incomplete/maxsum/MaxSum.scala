package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import org.slf4j.LoggerFactory
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.Meeting

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
	var timeslots : Int = 28
	var agents : Int = 3
	var meetingsNum : Int = 2
	var hardConstraintProb : Double = 0.2
	
	/**
	 * Functions
	 */
	
	def buildMeetings(meetingsNum : Int) : Array[Meeting] = {
	  var meetings : Array[Meeting] = Array()
	  for(meeting <- 1 to meetingsNum){
	    meetings :+ (new Meeting(meeting))
	  }
	  meetings
	}
	
	def buildParticipations() : Set[Int] = {
	  
	   println("building participations")
	   var participationsAmount : Int = random.nextInt(meetingsNum) + 1
	   println("possible participations: " + participationsAmount)
	   var participations : Set[Int] = Set[Int]()
	   for(partAmount <- 1 to participationsAmount){
		   var done : Boolean = false
				   while(done == false){						  
					   var participation = random.nextInt(meetingsNum) + 1
							   println("participation: " + participation)
							   if(!participations.contains(participation)){
								   participations += participation
										   done = true
							   }
				   }
	   }
	  participations
	}
	
	def buildTimeslots() : List[Int] = {					
	  
						val availableTimeslots = List[Int](1,2,3,4,5) // FIXME
//						for(timeslot <- 1 to timeslots){
//						  availableTimeslots + timeslot
//						}	
						availableTimeslots
	}
	
	def buildPreferences(participations : Set[Int], availableTimeslots : List[Int]) : Map[Any,Int] = {
	  var preference : Map[Any,Int] = Map[Any,Int]()
		for(participation <- participations){
		  println("preference " + participation)
			var timeslot = random.nextInt(availableTimeslots.size)
			preference += (participation -> availableTimeslots.apply(timeslot))
			availableTimeslots.drop(timeslot)
		}
	  preference
	}
	
	def buildHardConstraints(availableTimeslots : List[Int]) : Set[Int] = {
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
	
	def buildSoftConstraints(availableTimeslots : List[Int]) : Set[Int] = {
			var soft: Set[Int] = Set()
			for(softConstraint <- availableTimeslots){
			  if(!softConstraint.isNaN()){
				  soft + availableTimeslots.apply(softConstraint -1)
				}
			}
			soft
	}
	
	def addParticipationsToMeetings(variableVertex : VariableVertex, participations : Set[Int]){
	  for(participation <- participations){
	    var meeting : Meeting = meetings(participation)
	    meeting.addParticipant(variableVertex)
	  }
	}

	     /**
	      * Build meetings
	      */
       println("=================== Preparing Meetings ===================")
			 var meetings : Array[Meeting] = buildMeetings(meetingsNum)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               [Meeting] = buildMeetings(meetingsNum)
			
			 println("=================== Preparing Agents ===================")

			/**
			 * Build agents and their participations, constraints
			 */
			val allParticipations : Map[VariableVertex, Set[Int]] = Map[VariableVertex, Set[Int]]() // vertex to participations
			for(agent <- 1 to agents){
			  
			  println("++++++++++++++++++++++++ " + agent + " ++++++++++++++++++++++++")
			  
			  // Build participations
			  println("------------------- Meeting Participations -------------")
        var participations : Set[Int] = buildParticipations()
			  
        // Build constraints
			  println("------------------- Constraints -------------")

			  // timeslots
			  println("building available timeslots")
			  val availableTimeslots : List[Int] = buildTimeslots();

			  // preferences
				println("preferences")
				var preference : Map[Any,Int] = buildPreferences(participations,availableTimeslots)

				// hard constraints
			  println("building hard constraints")
			  var hard : Set[Int] = buildHardConstraints(availableTimeslots)

			  // soft constraints
			  println("building softConstraint")
				var soft : Set[Int] = buildSoftConstraints(availableTimeslots)	

				// proposal
				println("building proposal")
				var constraints = new Proposal(agent,hard,soft,preference)

				// vertex
			  println("creating variablevertex")
				var varVertex = new VariableVertex(agent,constraints,timeslots)
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
			   var meeting : Meeting = allParticipations.get(variableVertex)
			   var participants : Int = meeting.participantsCount - 1 
			   
			   var functions : Set[FunctionVertex] = Set()
			   for(function <- 1 to participants){
			     var variableId : Any = variableVertex.id;
			     var functionId : Any = "v" + variableVertex.id + "f" + function
			     var functionVertex : FunctionVertex = new FunctionVertex(functionID)
			     functions + (functionVertex);
			     graph.addEdge(variableId, new StateForwarderEdge(functionId))
			     graph.addEdge(functionId, new StateForwarderEdge(functionId))
			   }
			   
			   meeting.addParticipantFunctions(variableVertex, functions)
			} 
			
			// Find function vertices which are aimed at oneself, build edges
			for(variableVertex : VariableVertex <- allParticipations.keys){
			   var meeting : Meeting = allParticipations.apply(variableVertex)
			   var targetFunctions : Set[FunctionVertex] = meeting.getOtherFunctions(variableVertex)
			   for(targetFunction <- targetFunctions){
			     graph.addEdge(variableVertex.id, new StateForwarderEdge(targetFunction.id))
			     graph.addEdge(targetFunction.id, new StateForwarderEdge(variableVertex.id))
			   }
			} 
				     
      /**
			 * Produce the graph
			 */
			val graph = GraphBuilder.withConsole(true,8091).build
						
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
