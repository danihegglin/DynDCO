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

import com.signalcollect.dcop.MaxSumMessage
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffert com.signalcollect.GraphEditor

abstract class MaxSumVertex(id: MaxSumId, initialState: Int) extends DataGraphVertex(id, initialState) {

  override def scoreSignal = 1.0

  override def scoreCollect = 1.0
  
  var receivedMessages: HashMap[MaxSumId, MaxSumMessage] = null

  def initializeReceivedMessages = {
    var map: HashMap[MaxSumId, MaxSumMessage] = HashMap()
    getNeighborIds.foreach { currentId =>
      val dummyMessage = new MaxSumMessage(currentId, id, ArrayBuffer.fill(ProblemConstants.numOfColors)(0.0))
      map += (currentId -> dummyMessage)
    }
    receivedMessages = map
  }

  def getNeighborIds: ArrayBuffer[MaxSumId] = {
    var resultSet: ArrayBuffer[MaxSumId] = ArrayBuffer.fill(outgoingEdges.keys.size)(null)
    var index = 0
    outgoingEdges.keys.foreach { key =>
      resultSet(index) = key.asInstanceOf[MaxSumId]
      index = index + 1
    }
    resultSet

  }

  def getNumOfConflicts(): Int = {
    0
  }

}