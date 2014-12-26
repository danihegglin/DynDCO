package ch.uzh.dyndco.dynamic

class MeetingSchedulingVertex (
    id: Any, 
    graph: Graph[Any,Any],
    ) extends DataGraphVertex(id, initialState) {
  
  /**
   * Configuration
   */
    final var HARD_UTILITY_N : Double = 0
    final var SOFT_UTILITY_N : Double = 0.75
    final var PREF_UTILITY_N : Double = 1
    
   /**
    * Constraints
    */
    var constraints = constraints_
    final var originalConstraints = constraints
    
  /**
   * Calculate Utilities for current Best Value Assignment
   */
  def calculateLocalUtility(bestValueAssignment : Int): Double = {
    var utility : Double = PREF_UTILITY_N
    if(originalConstraints.hard.contains(bestValueAssignment)){
      utility = HARD_UTILITY_N
    }
    else if(originalConstraints.soft.contains(bestValueAssignment)){
      utility = SOFT_UTILITY_N
    }
    utility
  }
  
  /**
   * Calculate Utilities from Constraints
   */
  def calculateOriginUtilities() : Map[Any, Map[Int, Double]] = {
     var utilValueMap = Map[Int, Double]()
      for (value <- 1 to valueSpace){
        if(constraints.hard.contains(value)){
          utilValueMap += (value -> HARD_UTILITY_N)
        }
        else if (constraints.soft.contains(value)){
          utilValueMap += (value -> SOFT_UTILITY_N)
        }
        else if (constraints.preference.values.toList.contains(value)){
          utilValueMap += (value -> PREF_UTILITY_N)
        }
      }
     
     var finalUtilities = Map[Any, Map[Int, Double]]()
     finalUtilities += (id -> utilValueMap)
     finalUtilities
  }

}