package parser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RawLevel {
	
	private int gridSize;
	private String category;
	private Set<String> words;

	
	
	public void setGridSize(int size) {
		gridSize = size;
	}
	
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	public String getCategory() {
		return category;
	}
	
	
	public String setWordsString(String wordsString) {
		String[] parts = wordsString.split(",");
		List<String> faultWords = null;
		words = new LinkedHashSet<>();
		
		for(String word : parts) {
			if(word.length() > gridSize) {
				if(faultWords == null) faultWords = new ArrayList<>();
				faultWords.add("This word will not fit into the grid: " + word + " (grid size: " + gridSize + ")");
			}else if(word.isEmpty() || word.isBlank()) {
				if(faultWords == null) faultWords = new ArrayList<>();
				faultWords.add("Empty word here");
			}else if(!isUpper(word)) {
				if(faultWords == null) faultWords = new ArrayList<>();
				faultWords.add("Word (" + word + ") has lowercase character(s), uppercase is more suitable");
			}else {
				boolean exists = words.add(word);
				if(!exists) {
					if(faultWords == null) faultWords = new ArrayList<>();
					faultWords.add("The word " + word + " is duplicate");
				}
			}
			
		}
		
		if(faultWords != null) return faultWords.toString();
		else return null;
		
	}
	
	
	
	private boolean isUpper(String word) {
		for(int i = 0; i < word.length(); i++) {
			if(!Character.isUpperCase(word.charAt(i))) return false;
		}
		return true;
	}
	
	
	
	public Set<String> getWords(){
		return words;
	}
	



	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		String comma = "";
		
		for(String word : words) {
			sb.append(comma);
			sb.append(word);
			if(comma.isEmpty()) comma = ",";
		}
		
		sb.append("=");
		sb.append("=");
		sb.append(category);
		
		return sb.toString();
	}
	
}
