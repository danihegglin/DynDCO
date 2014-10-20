
  /*
 *  @author Genc Mazlami
 *
 *  Copyright 2013 University of Zurich
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.old

import scala.collection.mutable.ArrayBuffer
import scala.math._

/**
 * This class represents the container type for the messages being exchanged by the Max-Sum 
 * algorithm.
 * 
 *  @param s: an instance of MaxSumId that uniquely identifies the sender of the message
 *  @param t: an instance of MaxSumId that uniquely identifies the target/receiver of this message 
 */
class MaxSumMessage(s : MaxSumId, t: MaxSumId, v : ArrayBuffer[Double]) extends Serializable{

  // MaxSumId of the source vertex of this message
  val source : MaxSumId = s
  
  // MaxSumId of the target vertex of this message
  val target : MaxSumId = t
  
  // The contained preference values in this message
  val value : ArrayBuffer[Double] = v
 
  /**
   * checks if this MaxSumMessage instance has the equal ArrayBuffer value as "other"
   */
  def valueEquals(other : MaxSumMessage) = {
    var equal : Boolean = true
    for(i <- 0 to value.length - 1){
      if(value(i) != other.value(i)){
        equal = false
      }
    }
    equal
  }

  /**
   * overrides the default equality method
   */
  override def equals(other : Any) : Boolean = {
    other match {
      case x: MaxSumMessage => (valueEquals(x) && (x.source == source) && (x.target == target))
      case _ => false
    }
  }
}