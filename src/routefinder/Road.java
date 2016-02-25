package routefinder;

public class Road {
	
	private String start, end;
	private int length;
	
	public Road(String start, String end, int length){
		this.start = start;
		this.end = end;
		this.length = length;
	}
	
	public String getStart(){
		return this.start;
	}
	
	public void setStart(String start){
		this.start = start;
	}
	
	public String getEnd(){
		return this.end;
	}
	
	public void setEnd(String end){
		this.end = end;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public void setEnd(int length){
		this.length = length;
	}
}
