package routefinder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import exceptions.NoRouteFoundException;
import routefinder.conditions.ConditionVisitor;
import routefinder.conditions.NumberRoutesWithExactJunctionsCondition;
import routefinder.conditions.NumberRoutesWithMaxJunctionsCondition;

public class SatNavSystem implements ConditionVisitor{
	
	ArrayList<Road> roads;
	ArrayList<String> junctions;
	
	HashMap<String, ArrayList<Road>> roadsFromMap;
	
	public SatNavSystem(String roadFilename) 
			throws IOException, FileNotFoundException{
		roads = FileLoader.loadRoadsFromFile(roadFilename);
		System.out.println(roads.size() + " loaded into system.");
		
		roadsFromMap = generateRoadsFromMap(roads);
		
		junctions = new ArrayList<>();
		for (Road road : roads){
			if(!junctions.contains(road.getStart())){
				junctions.add(road.getStart());
			}
			if(!junctions.contains(road.getEnd())){
				junctions.add(road.getEnd());
			}
		}
		System.out.println("Number of junctions " + junctions.size());
	}
	
	//Generate a roads from map so that there is quick access to all the roads
	// that begin at a given junction
	private HashMap<String, ArrayList<Road>> generateRoadsFromMap(ArrayList<Road> roads){
		HashMap<String, ArrayList<Road>> map = new HashMap<>();
		
		for (Road road : roads){
			String start = road.getStart();
			
			if (!map.containsKey(start)){
				map.put(start, new ArrayList<>());
			}
			
			map.get(start).add(road);
		}
		
		return map;
	}
	
	private int distanceBetweenJunctions(String start, String end) throws NoRouteFoundException{
		ArrayList<Road> roadsFrom = roadsFromMap.get(start);
		
		Iterator<Road> it = roadsFrom.iterator();
		
		while (it.hasNext()){
			Road road = it.next();
			if (road.getEnd().equals(end)){
				return road.getLength(); 
			}
		}
		
		throw new NoRouteFoundException("NO ROUTE FOUND");
	}
	
	public int distanceThroughJunctions(String[] junctions) throws NoRouteFoundException{
		int totalDistance = 0;
		
		for (int i = 0; i < junctions.length - 1; i++){
			String j1 = junctions[i];
			String j2 = junctions[i+1];
			
			totalDistance += distanceBetweenJunctions(j1, j2);
		}
		
		return totalDistance;
	}
	
	//  Uses a modification of Djikstra's algorithm to find the shortest path from one road to 
	//  another
	public int shortestDistanceBetween(String start, String end){
		HashMap<Road, Integer> minDistances = new HashMap<>();
		
		ArrayList<Road> unsettled = new ArrayList<>();
		
		for (Road road : roads){
			minDistances.put(road, Integer.MAX_VALUE);
		}
		
		for (Road road : roadsFromMap.get(start)){
			minDistances.put(road, road.getLength());
			unsettled.add(road);
		}
		
		while (!unsettled.isEmpty()){
			Road currentShortest = null; 
			int currentMax = Integer.MAX_VALUE;
			
			for (Road road : unsettled){
				if (minDistances.get(road) < currentMax){
					currentShortest = road;
				}
			}
			
			unsettled.remove(currentShortest);
			
			for (Road road : roadsFromMap.get(currentShortest.getEnd())){
				int newDistance = road.getLength();
				newDistance = minDistances.get(currentShortest) + newDistance;
				
				if (minDistances.get(road) > newDistance){
					minDistances.put(road, newDistance);
					unsettled.add(road);
				}
			}
		}
		
		int returnValue = Integer.MAX_VALUE;
		
		for (Road road : minDistances.keySet()){
			if (road.getEnd().equals(end) && minDistances.get(road) < returnValue){
				returnValue = minDistances.get(road);
			}
		}
		return returnValue;
	}
	
	public int numberOfTripsWithinDistance(String start, String end, int distance){
		int numberOfTrips = 0;
		
		Stack<Road> roadsToTry = new Stack<>();
		Stack<Integer> currentScores = new Stack<>();
		
		for (Road road : roadsFromMap.get(start)){
			roadsToTry.add(road);
			currentScores.add(road.getLength());
		}
		
		while (!roadsToTry.isEmpty()){
			Road road = roadsToTry.pop();
			int score = currentScores.pop();
			
			if (road.getEnd().equals(end)){
				numberOfTrips++;
			}
			
			for (Road newRoad : roadsFromMap.get(road.getEnd())){
				int newScore = score + newRoad.getLength();
				if (newScore < distance){
					roadsToTry.push(newRoad);
					currentScores.push(newScore);
				}
			}
		}
		
		return numberOfTrips;
	}
	
	public int visit(NumberRoutesWithMaxJunctionsCondition condition) throws NoRouteFoundException{
		int numberOfRoutes = 0;
		
		String start = condition.getStart();
		String end = condition.getEnd();
		int maxJunctions = condition.getMax();
		
		LinkedList<Road> roadsToTry = new LinkedList<>();
		roadsToTry.addAll(roadsFromMap.get(start));
		
		int junctionsHit = 0;
		int numberAtCurrentDepth = roadsToTry.size(); 
		
		while (junctionsHit < maxJunctions && !roadsToTry.isEmpty() ){
			Road road = roadsToTry.pop();
			
			if (road.getEnd().equals(end)){
				numberOfRoutes++;
			}
			else{
				roadsToTry.addAll(roadsFromMap.get(road.getEnd()));
			}
			
			numberAtCurrentDepth--;
			if (numberAtCurrentDepth == 0){
				junctionsHit++;
				numberAtCurrentDepth = roadsToTry.size();
			}
			
			System.out.println(roadsToTry.size() + " " + numberOfRoutes);
		}
		
		if (numberOfRoutes == 0){
			throw new NoRouteFoundException("NO ROUTE FOUND");
		}
		
		else{
			return numberOfRoutes;
		}
	}

	@Override
	public int visit(NumberRoutesWithExactJunctionsCondition condition) throws NoRouteFoundException {
		int numberOfRoutes = 0;
		
		String start = condition.getStart();
		String end = condition.getEnd();
		int maxJunctions = condition.getMax();
		
		LinkedList<Road> roadsToTry = new LinkedList<>();
		roadsToTry.addAll(roadsFromMap.get(start));
		
		int junctionsHit = 0;
		int numberAtCurrentDepth = roadsToTry.size(); 
		
		while (junctionsHit < maxJunctions && !roadsToTry.isEmpty() ){
			Road road = roadsToTry.pop();
			
			roadsToTry.addAll(roadsFromMap.get(road.getEnd()));
			
			numberAtCurrentDepth--;
			if (numberAtCurrentDepth == 0){
				junctionsHit++;
				numberAtCurrentDepth = roadsToTry.size();
			}
			
			System.out.println(roadsToTry.size() + " " + numberOfRoutes);
		}
		
		for (Road road : roadsToTry){
			if (road.getEnd().equals(end)){
				numberOfRoutes++;
			}
		}
		
		if (numberOfRoutes == 0){
			throw new NoRouteFoundException("NO ROUTE FOUND");
		}
		
		else{
			return numberOfRoutes;
		}
	}
	
}
