package ch.uzh.dyndco.stack

import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.ExecutionConfiguration
import ch.uzh.dyndco.stack.TestMode

trait DCOAlgorithm {
  
  def run(
      problem : MeetingSchedulingProblem,
      configuration : Configuration
      )

}