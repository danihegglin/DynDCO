/*
 *  @author Philip Stutz
 *
 *  Copyright 2010 University of Zurich
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

package com.signalcollect.messaging

import com.signalcollect.interfaces.WorkerApiFactory
import com.signalcollect.interfaces.MessageBus
import com.signalcollect.interfaces.VertexToWorkerMapper
import akka.actor.ActorSystem

class DefaultMessageBus[Id, Signal](
    val system: ActorSystem,
    val numberOfWorkers: Int,
    val numberOfNodes: Int,
    val mapper: VertexToWorkerMapper[Id],
    val sendCountIncrementorForRequests: MessageBus[_, _] => Unit,
    workerApiFactory: WorkerApiFactory[Id, Signal]) extends AbstractMessageBus[Id, Signal] {
  lazy val workerApi = workerApiFactory.createInstance(workerProxies, mapper)
}
