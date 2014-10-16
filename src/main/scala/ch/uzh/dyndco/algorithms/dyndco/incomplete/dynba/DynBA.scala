package ch.uzh.dyndco.algorithms.dyndco.incomplete.dynba

/**
 * siehe paper
 */

class DynBA {
  
//  function BA(in: X,D,C : set, in: max_iterations: integer): boolean
// 1 Initialize(X, D, C );
// 2 problem_conflict_value ¨ 1;
// 3 previous_problem_conflict_value ¨ 1;
// 4 ite ¨ 0;
// 5 while (problem_conflict_value > 0 and ite = max_iterations)
// 6 ite ¨ ite + 1;
// 7 for each xi
// e X do 
// 8 if (Variable_Conflict_Value(xi
//, X, C ) > 0 ) then
// 9 xi ¨ Revised_Value(xi, d(xi), X, C);
// 10 problem_conflict_value=Problem_Conflict_Value(X, C);
//11 if(problem_conflict_value = 0) then return true; 
//12 if(problem_conflict_value = previous_problem conflict_value) then
//13 Increase_Weights(X, C);
// 14 previous_problem_conflict_value = problem_conflict_value;
// 15 return false; 
//
//
//}
//
//procedure Initialize ( in: X, D, C : set)
// 1 for each xi
// e X do
// 2 if(xi=null) thenxi ¨ random_value(d(xi
// )); 
// 3 for eachw(ck
//(xi
//,xj
//)) e C do
// 4 w(ck
//(xi
//,xj
//) ) ¨ 1;
//
//function Revised_Value ( in: xi
// :variable, 
// in: d(xi) :domain, 
// in: X, C :set ): domain value
// 1 min_conflict ¨ Variable_ Conflict_Value(xi, X, C );
// 2 vmin_conflict ¨ value(xi );
// 3 for each vj e d(xi) do
// 4 xi ¨ vj
// 5 conflict ¨ Variable_ Conflict_Value(xi
//, X, C );
// 6 if(conflict = 0) return vj
//;
// 7 if (conflict< min_conflict) then
// 8 min_conflict = conflict;
// 9 vmin_conflict ¨ vj ;
// 10 return vmin_conflict ;
//
//  function Variable_Conflict_Value ( in: xi :variable,
// in: X, C :set ): integer
// 1 variable_conflict_value ¨ 0;
// 2 for each ck (xs
//, xt
//) e C and (xs
//, xt
// e X) and ((xs=xi
//) or (xt=xi
//)) do
// 3 if (ck (xs
//, xt
//) = false) then conflict_value ¨ conflict_value + w(ck
// (xs
//, xt
//));
// 4 return variable_conflict_value;
//
// function Problem_Conflict_Value ( in: X, C :set ): integer
// 1 problem_conflict_value ¨ 0;
// 2 for each ck (xs, xt) e C and xs, xt eX do
// 3 if (ck (xs
//, xt
//) = false) then
//problem_conflict_value ¨ problem_conflict_value + w(ck (xs, xt));
// 4 return problem_conflict_value;
//
// procedure Increase_Weights ( in: X, C : set) 
// 1 for eachck(xi,xj) e C and xs, xt eX do
// 2 if(ck
//(xi
//,xj
//) =false) then w (ck
// (xi
//, xj
//)) ¨ w (ck
// (xi
//, xj
//)) +1;
}