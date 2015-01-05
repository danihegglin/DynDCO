package ch.uzh.dyndco.stack.tests

import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.ExecutionConfiguration
import ch.uzh.dyndco.stack.configuration.Configuration
import ch.uzh.dyndco.stack.configuration.Configuration

trait DCOAlgorithm {
  
  def run(
      problem : MeetingSchedulingProblem,
      configuration : Configuration
      )

}