package modules.graph.local_iterative;

public class DPOP {

	1: DPOP(X , D, R)
	Each agent Xi executes:
	2:
	3: Phase 1: pseudotree creation
	4: elect leader from all Xj ∈ X
	5: elected leader initiates pseudotree creation
	6: afterwards, Xi knows P(Xi), PP(Xi), C(Xi) and PC(Xi)
	7: Phase 2: UTIL message propagation
	8: if |Children(Xi)| == 0 (i.e. Xi
	is a leaf node) then
	9: UTILXi
	(P(Xi)) ← Compute utils(P(Xi), PP(Xi))
	10: Send message(P(Xi), UTILXi
	(P(Xi)))
	11: activate UTIL Message handler()
	12: Phase 3: VALUE message propagation
	13: activate VALUE Message handler()
	14: END ALGORITHM
	15:
	16: UTIL Message handler(Xk,UTILXk
	(Xi))
	17: store UTILXk
	(Xi)
	18: if UTIL messages from all children arrived then
	19: if Parent(Xi)==null (that means Xi
	is the root) then
	20: v
	∗
	i ← Choose optimal(null)
	21: Send V ALUE(Xi
	, v
	∗
	i
	) to all C(Xi)
	22: else
	23: UTILXi
	(P(Xi)) ← Compute utils(P(Xi), PP(Xi))
	24: Send message(P(Xi), UTILXi
	(P(Xi)))
	25: return
	26:
	27: VALUE Message handler(V ALUE
	Xi
	P (Xi)
	)
	28: add all Xk ← v
	∗
	k ∈ V ALUE
	Xi
	P (Xi)
	to agent view
	29: Xi ← v
	∗
	i = Choose optimal(agent view)
	30: Send V ALUE
	Xl
	Xi
	to all Xl ∈ C(Xi)
	31:
	32: Choose optimal(agent view)
	33:
	v
	∗
	i ← argmaxvi
	X
	Xl∈C(Xi)
	UTILXl
	(vi
	, agent view)
	34: return v
	∗
	i
	35:
	36: Compute utils(P(Xi), PP(Xi))
	37: for all combinations of values of Xk ∈ PP(Xi) do
	38: let Xj be Parent(Xi)
	39: similarly to DTREE, compute a vector UTILXi
	(Xj )
	of all {UtilXi
	(v
	∗
	i
	(vj ), vj )|vj ∈ Dom(Xj )}
	40: assemble a hypercube UTILXi
	(Xj ) out of all these
	vectors (totaling |PP(Xi)| + 1 dimensions).
	41: return UTILXi
	(Xj )

}
