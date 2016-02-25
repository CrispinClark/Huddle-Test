package routefinder.conditions;

import exceptions.NoRouteFoundException;

public class NumberRoutesWithExactJunctionsCondition implements Condition {
private String start, end;
	
	private int max;
	
	public NumberRoutesWithExactJunctionsCondition(String start, String end, int max){
		this.start = start;
		this.end = end;
		this.max = max;
	}

	@Override
	public int accept(ConditionVisitor visitor) throws NoRouteFoundException {
		return visitor.visit(this);
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
