package routefinder.conditions;

import exceptions.NoRouteFoundException;

public interface ConditionVisitor {
	
	public int visit(NumberRoutesWithMaxJunctionsCondition condition) 
			throws NoRouteFoundException;
	
	public int visit(NumberRoutesWithExactJunctionsCondition condition)
			throws NoRouteFoundException;
}	
