package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map

class MeetingProposal (
    blocks_ : Map[Int, Int], 
    free_ : Map[Int, Int], 
    proposed_ : Map[Int, Int]
    ) {
  
    var blocks = blocks_
    var free = free_
    var proposed = proposed_
}
