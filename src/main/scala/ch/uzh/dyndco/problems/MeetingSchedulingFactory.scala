package ch.uzh.dyndco.problems

import scala.collection.mutable.MutableList
import ch.uzh.dyndco.algorithms.maxsum.Meeting
import scala.util.Random
import collection.mutable.Set
import collection.mutable.Map

object MeetingSchedulingFactory {
  
  /**
 	 * Configuration
	 */
  //	var HARD_CONSTRAINT_PROB : Double = 0.2 // args(3)
  
  /**
   * Tools
   */
  var random : Random = new Random
  
  /**
   * Top-level build function
   */
  
  var TIMESLOTS : Int = 0
  var MEETINGS : Int = 0
  var AGENTS : Int = 0
  
  def build(TIMESLOTS_ : Int, MEETINGS_ : Int, AGENTS_ : Int) : MeetingSchedulingProblem = {
    
    TIMESLOTS = TIMESLOTS_
    MEETINGS = MEETINGS_
    AGENTS = AGENTS_
    
    var meetings : MutableList[Meeting] = buildMeetings()
    var allParticipations : Map[Int, Set[Int]] = buildAllParticipations()
    var allConstraints : Map[Int, Constraints] = buildAllConstraints(allParticipations)

    new MeetingSchedulingProblem(
        meetings,allParticipations,allConstraints,
        TIMESLOTS, MEETINGS, AGENTS) 
  }
  
  /**
   * Mid-level build functions
   */
  private def buildAllParticipations() : Map[Int, Set[Int]] = {
    val allParticipations : Map[Int, Set[Int]] = Map[Int, Set[Int]]()
    for(agent <- 1 to AGENTS){
        
        // Build participations
        var participations : Set[Int] = buildParticipations()
        
        allParticipations += (agent -> participations)
    }
    
    allParticipations
  }
  
  private def buildAllConstraints(allParticipations : Map[Int, Set[Int]]) : Map[Int, Constraints] = {
    val allConstraints : Map[Int, Constraints] = Map[Int, Constraints]()
    for(agent <- 1 to AGENTS){
      
      // Build participations
        var participations : Set[Int] = allParticipations.apply(agent)
        
        // Build constraints
        val availableTimeslots : MutableList[Int] = buildTimeslots();

        // preferences
       var preferences : Map[Int,Int] = buildPreferences(participations,availableTimeslots)
       
       // initialized used set
       var used : Set[Int] = Set()
       for(preference <- preferences.values){
         used += preference
       }

        // hard constraints
        var hardConstraints : Set[Int] = buildHardConstraints(availableTimeslots, used)
        
        // extend used set
        for(hardConstraint <- hardConstraints){
          used += hardConstraint
        }

        // soft constraints
        var softConstraints : Set[Int] = buildSoftConstraints(availableTimeslots, used)
        
        // Constraint pack
        var constraints = new Constraints(agent,hardConstraints,softConstraints,preferences)
        
        // add to constraint & participations collections
        allConstraints += (agent -> constraints)
    }
    allConstraints
  }

  /**
   * Detail build functions
   */
  
  private def buildMeetings() : MutableList[Meeting] = {
	  var meetings = MutableList[Meeting]()
	  for(meeting <- 1 to MEETINGS){
	    meetings += (new Meeting(meeting))
	  }
	  meetings
	}
	
	private def buildParticipations() : Set[Int] = {
	   var participationsAmount : Int = random.nextInt(MEETINGS) + 1
	   var participations : Set[Int] = Set[Int]()
	   for(partAmount <- 1 to participationsAmount){
		   var done : Boolean = false
				   while(done == false){						  
					   var participation = random.nextInt(MEETINGS) + 1
							   if(!participations.contains(participation)){
								   participations += participation
										   done = true
							   }
				   }
	   }
	  participations
	}
	
	private def buildTimeslots() : MutableList[Int] = {					
	  var availableTimeslots = MutableList[Int]()
	  for(timeslot <- 1 to TIMESLOTS){
	    availableTimeslots += timeslot
	  }	
		availableTimeslots
	}
	
	private def buildPreferences(participations : Set[Int], availableTimeslots : MutableList[Int]) : Map[Int,Int] = {
	  var preference : Map[Int,Int] = Map[Int,Int]()
		for(participation <- participations){
			var timeslot = random.nextInt(availableTimeslots.size)
			preference += (participation -> availableTimeslots.apply(timeslot))
		}
	  preference
	}
	
	private def buildHardConstraints(availableTimeslots : MutableList[Int], used : Set[Int]) : Set[Int] = {
			var available = random.nextInt(availableTimeslots.size) + 1
			var numOfHardConstraints : Int = available / 3 // FIXME
			var hardConstraints: Set[Int] = Set()
			for(hardConstraint <- 1 to numOfHardConstraints){
				var timeslot = -1
				while (timeslot < 0){
					var proposedTimeslot : Int = random.nextInt(availableTimeslots.size) + 1
					if(!used.contains(proposedTimeslot)){
						timeslot = proposedTimeslot
					}
				}
				hardConstraints += timeslot
			}
			hardConstraints
	}
	
	private def buildSoftConstraints(availableTimeslots : MutableList[Int], used : Set[Int]) : Set[Int] = {
			var softConstraints: Set[Int] = Set()
			for(availableTimeslot <- availableTimeslots){
			  if(!used.contains(availableTimeslot)){
				  softConstraints += availableTimeslot
				}
			}
			softConstraints
	}
  
}