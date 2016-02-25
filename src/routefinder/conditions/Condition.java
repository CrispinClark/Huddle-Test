package routefinder.conditions;

import exceptions.NoRouteFoundException;
import routefinder.SatNavSystem;

public interface Condition {
	public int accept(ConditionVisitor visitor) throws NoRouteFoundException;
}
