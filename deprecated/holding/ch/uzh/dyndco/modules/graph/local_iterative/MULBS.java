package modules.graph.local_iterative;

public class MULBS {

	procedure Initialize() {This function starts the agent}
	2: CurrentPartialSolution <- 0;
	3: CurrentValue <- 0;
	4: CurrentCost <- 00;
	5: X <- generateAndStoreLocalPartialSolutionsO; {X is any assignment possible because CurrentPartialSolution is empty.}
	Algorithm 2 MULBS - Message Processing
	1: procedure evaluate(P ARTIAL_SOLUTION, Solution)
	2: C <- CurrentPartialSolution USolution;
	3: addV ariableMinConJlict(C, NewSolution, NewCost); 4: if complete(NewSolution) then
	6: if leaJO then
	7: sendPartiaISolution(X); 8: end if
	9: end procedure
	10: procedure locaISearch(Threshold.
	5: 6: 7: 8: 9:
	11: 12: 13: 14:
	15:
	16: 17: 18: 19:
	20: 21: 22: 23: 24:
	25:
	26: 27: 28: 29: 30: 31:
	32: 33: 34: 35: 36:
	37: 38:
	39: 40: 41: 42: 43:
	44: 45:
	46: 47: 48: 49:
	50: 51: 52:
	i +- OJ
	NewThreshold <- Threshold;
	while (i < ILocalSolutionsl) /\ (NewThreshold Threshold) do
	16: C <- CurrentPartialSolution;
	17: Cost <- CurrentCost;
	18: if (Cost <= NewCost) V (C = NewSolution) then 19: RETURN:
	20: else
	old)
	if Threshold> 0 then
	NewSolution,
	New Thresh-
	C =mergeSolutions(LocaISolutions[i], CurrentPartialSolution) ; NewThreshold <- cost(C);
	if NewThreshold < Threshold then
	NewSolution <- C; updateCurrentPartialSolution(NewSolution, NewCost);
	21:
	22:
	23:
	24:
	25:
	26:
	27:
	28: 29: 30: 31: 32:
	33: 34: 35:
	36: 37:
	38:
	39:
	40:
	if NewCost < Cost then CurrentPartialSolution <- N ewSolution; CurrentCost <- NewCost;
	CurrentV alue
	agentV alue(CurrentPartialSolution);
	end if
	locaISearch(CurrentCost, NewPartial, NewPartiaICost); if NewPartialCost < Cost then
	CurrentPartialSolution <- 0;
	CurrentCost <- 00; sendTryLocaISolution(NewPartial, NewPartiaICost);
	else
	sendStoreSolution(CurrentPartialSolution,
	CurrentCost);
	end if end if
	end procedure
	procedure evaluate(TRLLOCAL_SOLUTION, Solution,
	Cost)
	if rootO then
	updateCurrentPartiaISolution(Solution, Cost); if Cost < CurrentCost then
	sendStoreSolution(Solution, Cost);
	end if
	i +- i +1; end while
	end if
	end procedure
	procedure updateCurrentPartiaISolution(NewSolution, New- Cost)
	if NewCost < CurrentCost then
	CurrentPartialSolution <- N ewSolution; CurrentCost <- NewCost;
	CurrentValue <- agentValue(NewSolution);
	end if
	end procedure
	procedure sendPartiaISolution(NewSolution) if rootO then
	RETURN:
	else
	send(P ARTIAL_SOLUTION, ParentO,
	NewSolution);
	end if
	end procedure
	procedure sendTryLocaISolution(Solution, Cost) if rootO then
	sendStoreSolution(Solution, Cost);
	else
	send(TRY_LOCAL_SOLUTION, ParentO,
	Solution, Cost);
	end if
	end procedure
	procedure sendStoreSolution(Solution,Cost) i<-O;
	while i < IChildrenOI do
	send(STORE_SOLUTION, Children[i], Solution, Cost);
	i <- i + 1;
	end while
	end procedure

}
