DynDCO is a system for benchmarking of distributed constraint optimization problems in static and dynamic environments. This framework is built on top of Signal/Collect (https://github.com/uzh/signal-collect) and is currently focused around meeting scheduling.

SingleTest
--------------------------
Density: Define the density of existing constraint / all possible constraints in a percentage.

`--density DOUBLE`

Algorithm: Define the algorithm that should be tested. Currently available are maxsum, mgm and dpop.

`--algorithm STRING`

Execution: Define the run mode of the Signal/Collect graph processing (synchronous, asynchronous)

`--execution STRING`

Mode: Define the environment. normal is a static environment. dynamicConstraints allows for constraint changes and dynamicVariables allows variable changes.

`--mode STRING`

Param: Need to be indicated if one of the two dynamic environments have been chosen. The parameters should be separated by commas. For the dynamicConstraints mode, the parameters are run frequency (single,multiple), interval (in milliseconds) and percentage of constraints changed. For the dynamicVariables mode, one can indicate run frequency (single,multiple), interval (in milliseconds), probability to create a new neighbourhood, probability to create a new agent, probability to add an agent instead of removing, number of additions/removals.

`--param  STRING,STRING,STRING,..`

Timeslots: Indicates the number of available timeslots in the schedules of the agents

`--timeslots INT`

Meetings: Indicates the number of meetings that should be held

`--meetings INT`

Agents: Indicates the number of agents in the graph

`--agents 30`


MultiTest
-------------------------
In the MultiTest, one can add additional parameters to control the tests that should be conducted.

Runs: Defines how often the same configuration is run
--runs INT

FactorAgents: Defines how much the number of agents increases in the next round. The numbers are additive.
--factoragents INT

FactorMeetings: Defines how much the number of meetings increases in the next round. The numbers are additive.
--factormeetings INT

MaxAgents: Defines the maximum of agents that should be tested. The MultiTest stops after the limit is reached.
--maxagents INT

MaxMeetings: Defines the maximum of meetings that should be tested. The MultiTest stops after the limit is reached.
--maxmeetings INT


