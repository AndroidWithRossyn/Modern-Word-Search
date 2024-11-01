package parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
	
	private static Map<Integer, List<RawLevel>> mapping = new HashMap<>();
	public static boolean foundError;
	
	
	
	public static Map<Integer, List<RawLevel>> getLevelMapping(){
		return mapping;
	}

	
	public static void parse(String file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		String line = null;
		int lineNumber = 0;
		
		Set<Integer> gridSizes = new HashSet<>(); 
		int currentKey = 0;
		
		while((line = bufferedReader.readLine()) != null) {
			lineNumber++;
			line = line.trim();
			if(line.isEmpty() || line.charAt(0) == '#') continue;
			
			
			if(line.matches("\\d")) {
				currentKey = Integer.parseInt(line);
				if(!gridSizes.add(currentKey)) {
					foundError = true;
					System.err.println("Line " + lineNumber + ": Grid size " + currentKey + " is duplicate");
				}
				
				if(currentKey < 3 || currentKey > 8) {
					foundError = true;
					System.err.println("Line " + lineNumber + ": Grid size must be between 3 and 8, you entered: " + currentKey);
				}
				List<RawLevel> levels = new ArrayList<>();
				mapping.put(currentKey, levels);
				continue;
			}
			
			String[] parts = line.split("\\|");
			
			if(parts.length < 2 || parts.length > 2) { 
				foundError = true;
				System.err.println(" Line " + lineNumber + ": There must be 2 parts seperated by pipe on each line");
				continue;
			}
			
			
			RawLevel rawLevel = new RawLevel();
			
			rawLevel.setGridSize(currentKey);

			int wordsIndex = 0;
			int titleIndex = 1;


			String error = rawLevel.setWordsString(parts[wordsIndex]);
			if(error != null) {
				foundError = true;
				System.err.println("Line " + lineNumber + ": " + error);
			}
			rawLevel.setCategory(parts[titleIndex]);
			
			
			mapping.get(currentKey).add(rawLevel);
		}
	
		if(currentKey == 0) {
			foundError = true;
			System.err.println("Could not find any grid size number");
		}
		
		for(Integer size : mapping.keySet()) {
			if(mapping.get(size).size() == 0) {
				foundError = true;
				System.err.println("Grid size " + size + " has not levels associated");
			}
		}
		
		
		bufferedReader.close();
				
	}
	
	
	
	

}
