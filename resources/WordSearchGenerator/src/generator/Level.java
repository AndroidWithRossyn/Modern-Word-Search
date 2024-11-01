package generator;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Level {

	public int gridSize;
	public char[] board;
	public Set<String> solution;
	public String category;
	public List<Integer> unnessaryLocations;
	public int coinedWordIndex = -1;
	
	
	public void setCoinedWordIndex() {
		Random random = new Random(System.currentTimeMillis());
		coinedWordIndex = random.nextInt(solution.size());
	}	
	
}
