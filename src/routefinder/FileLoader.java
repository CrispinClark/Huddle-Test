package routefinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileLoader {
	
	public static ArrayList<Road> loadRoadsFromFile(String filename) 
			throws FileNotFoundException, IOException{
		
		ArrayList<Road> roads = new ArrayList<>();
		
		String jsonString = getStringFromFile(filename);
		
		JSONObject json = new JSONObject(jsonString);
		JSONArray roadsJSON = json.getJSONArray("roads");
		
		for (int i = 0; i < roadsJSON.length(); i++){
			JSONObject obj = roadsJSON.getJSONObject(i);
			
			String start = obj.getString("start");
			String end = obj.getString("end");
			int length = obj.getInt("length");
			
			roads.add(new Road(start, end, length));
		}
		
		return roads;
	}
	
	private static String getStringFromFile(String filename) 
			throws FileNotFoundException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		
		while (line != null){
			sb.append(line);
			line = br.readLine();
		}
		
		String jsonString = sb.toString();
		br.close();
		
		return jsonString;
	}
}
