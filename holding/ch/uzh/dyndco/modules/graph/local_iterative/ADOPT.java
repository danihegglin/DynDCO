package modules.graph.local_iterative;

public class ADOPT {

	Initialize
	(1) threshold ← 0; CurrentContext ← {};
	(2) forall d ∈ Di
	, xl ∈ Children do
	(3) lb(d, xl) ← 0; t(d, xl) ← 0;
	(4) ub(d, xl) ← Inf; context(d, xl) ← {}; enddo;
	(5) di ← d that minimizes LB(d);
	(6) backTrack;
	when received (THRESHOLD, t, context)
	(7) if context compatible with CurrentContext:
	(8) threshold ← t;
	(9) maintainThresholdInvariant;
	(10) backTrack; endif;
	when received (TERMINATE, context)
	(11) record TERMINATE received from parent;
	(12) CurrentContext ← context;
	(13) backTrack;
	when received (VALUE, (xj ,dj ))
	(14) if TERMINATE not received from parent:
	(15) add (xj ,dj ) to CurrentContext;
	(16) forall d ∈ Di
	, xl ∈ Children do
	(17) if context(d, xl) incompatible with CurrentContext:
	(18) lb(d, xl) ← 0; t(d, xl) ← 0;
	(19) ub(d, xl) ← Inf; context(d, xl) ← {}; endif; enddo;
	(20) maintainThresholdInvariant;
	(21) backTrack; endif;
	when received (COST, xk, context, lb, ub)
	(22) d ← value of xi
	in context;
	(23) remove (xi
	,d) from context;
	(24) if TERMINATE not received from parent:
	(25) forall (xj ,dj ) ∈ context and xj is not my neighbor do
	(26) add (xj ,dj ) to CurrentContext;enddo;
	(27) forall d
	0 ∈ Di
	, xl ∈ Children do
	(28) if context(d
	0
	, xl) incompatible with CurrentContext:
	(29) lb(d
	0
	, xl) ← 0; t(d
	0
	, xl) ← 0;
	(30) ub(d
	0
	, xl) ← Inf; context(d
	0
	, xl) ← {};endif;enddo;endif;
	(31) if context compatible with CurrentContext:
	(32) lb(d, xk) ← lb;
	(33) ub(d, xk) ← ub;
	(34) context(d, xk) ← context;
	(35) maintainChildThresholdInvariant;
	(36) maintainThresholdInvariant; endif;
	(37) backTrack;
	procedure backTrack
	(38) if threshold == UB:
	(39) di ← d that minimizes UB(d);
	(40) else if LB(di) > threshold:
	(41) di ← d that minimizes LB(d);endif;
	(42) SEND (VALUE, (xi
	, di))
	(43) to each lower priority neighbor;
	(44) maintainAllocationInvariant;
	(45) if threshold == UB:
	(46) if TERMINATE received from parent
	(47) or xi
	is root:
	(48) SEND (TERMINATE,
	(49) CurrentContext ∪ {(xi
	, di)})
	(50) to each child;
	(51) Terminate execution; endif;endif;
	(52) SEND (COST, xi
	, CurrentContext, LB, UB)
	to parent;
	Figure 3: Procedures for receiving messages (Adopt algorithm). Definitions of terms
	LB(d),UB(d),LB, UB are given in the text.
	11 odi, Shen, Tambe & Yokoo
	procedure maintainThresholdInvariant
	(53) if threshold < LB:
	(54) threshold ← LB; endif;
	(55) if threshold > UB:
	(56) threshold ← UB; endif;
	%note: procedure assumes ThresholdInvariant is satisfied
	procedure maintainAllocationInvariant
	(57) while threshold > δ(di) + P
	xl∈Children t(di
	, xl) do
	(58) choose xl ∈ Children where ub(di
	, xl) > t(di
	, xl);
	(59) increment t(di
	, xl); enddo;
	(60) while threshold < δ(di) + P
	xl∈Children t(di
	, xl) do
	(61) choose xl ∈ Children where t(di
	, xl) > lb(di
	, xl);
	(62) decrement t(di
	, xl); enddo;
	(63) SEND (THRESHOLD, t(di
	, xl), CurrentContext )
	to each child xl
	;
	procedure maintainChildThresholdInvariant
	(64) forall d ∈ Di
	, xl ∈ Children do
	(65) while lb(d, xl) > t(d, xl) do
	(66) increment t(d, xl); enddo;endo;
	(67) forall d ∈ Di
	, xl ∈ Children do
	(68) while t(d, xl) > ub(d, xl) do
	(69) decrement t(d, xl); enddo;enddo;


}
