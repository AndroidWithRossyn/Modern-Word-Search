package generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Generator {
	
	private int mRows = 8;
	private int mColumns = 8;
	private int[] mRandomIndexes;
	private String[] mWordList;
	private final Direction[] mDirections = Direction.values();
	private boolean[][] mLock;
	private char[][] mBoard;
	private Set<String> mSolution;
	private char[] alphabetChars;
	private Level level;
	private int tries = 1000;
	
	
	public Generator(int size, String[] wordList, String alphabet, int tries) {
		mColumns = size;
		mRows = size;
		mWordList = wordList;
		alphabetChars = alphabet.toCharArray(); 
		mBoard = new char[mRows][mColumns];
		mLock = new boolean[mRows][mColumns];
		this.tries = tries;
	}
	
	
	
	
	 
	
	
	
	
	
	
	
	
	private void resetBoard() {
		mSolution = new LinkedHashSet<>();
		

		for (int i = 0; i < mLock.length; i++) {
			for (int j = 0; j < mLock[i].length; j++) {
				mLock[i][j] = false;
			}
		}

		Random random = new Random();
		for (int i = 0; i < mBoard.length; i++) {
			for (int j = 0; j < mBoard[i].length; j++) {
				
				mBoard[i][j] = alphabetChars[random.nextInt(alphabetChars.length)];
			}
		}


	}
	
	
	
	
	public Level getLevel() {
		
		return level;
	}
	
	
	
	
	public void generate() {
		
		while(tries > 0) {
			tries--;
			
			resetBoard();
			generateBoard();

			
			if(level == null) {
				level = new Level();
				level.gridSize = mRows;
				level.board = twoDTo1D(mBoard);
				//level.unnessaryLocations = setUnnecessaryChars(level.board);
				level.solution = mSolution;
			}
			

			if(mSolution.size() > level.solution.size()) {
				level.board = twoDTo1D(mBoard);
				level.solution = mSolution;
				//level.unnessaryLocations = setUnnecessaryChars(level.board);
			}
			

			if(mSolution.size() == mWordList.length) break;
		}
	}
	
	
	
	
	
	private List<Integer> setUnnecessaryChars(char[] board) {

		
		LinkedHashSet<Character> used = new LinkedHashSet<>();
		for(Character c : board) used.add(c);
		
		LinkedHashSet<Character> alphabet = new LinkedHashSet<>();
		for(Character c : alphabetChars) alphabet.add(c);
		
		alphabet.removeAll(used);
		
		
		ArrayList<Character> list = new ArrayList<>();
		list.addAll(alphabet);
		Collections.shuffle(list);
		
		List<Integer> positions = new ArrayList<Integer>();
		
		Random random = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < board.length; i++) {
			if(board[i] == '@') {
				board[i] = list.get(random.nextInt(list.size()));
				positions.add(i);
			}
			
		}
		
		
		
		System.out.println("unused: " + alphabet);
		System.out.println("pos: " + positions);
		
		return positions;
	}
	
	
	
	
	
	private char[] twoDTo1D(char[][] input) {
		char[] result = new char[input.length * input[0].length];
		
		int index = 0;
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[i].length; j++) {
				result[index++] = input[i][j];
			}
		}
		return result;
	}
	
	
	
	private void generateBoard() {
		mRandomIndexes = new int[mRows * mColumns];

		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < mRandomIndexes.length; i++) {
			mRandomIndexes[i] = i;
		}

		for (int i = mRandomIndexes.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			int realIndex = mRandomIndexes[i];
			mRandomIndexes[i] = mRandomIndexes[randIndex];
			mRandomIndexes[randIndex] = realIndex;
		}

	
		for (String word : mWordList) {
			addWord(word);
		}

	}
	
	
	
	
	private Word addWord(String word) {
		if (word.length() > mColumns && word.length() > mRows) {
			return null;
		}

		Random rand = new Random();
		for (int i = mDirections.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			Direction direction = mDirections[i];
			mDirections[i] = mDirections[randIndex];
			mDirections[randIndex] = direction;
		}

		Direction bestDirection = null;
		int bestRow = -1;
		int bestCol = -1;
		int bestScore = -1;
		for (int index : mRandomIndexes) {
			int row = index / mColumns;
			int col = index % mColumns;
			for (Direction direction : mDirections) {
				int score = canFit(word, direction, row, col);
				if (score > bestScore) {
					bestRow = row;
					bestCol = col;
					bestDirection = direction;
					bestScore = score;
				}
			}
		}
		if (bestScore >= 0) {
			Word result = new Word(word, bestRow, bestCol, bestDirection);
			placeWord(result);
			return result;
		}

		return null;
	}
	
	
	
	
	
	private int canFit(String word, Direction direction, int row, int col) {
		if (getAvailableSpace(direction, row, col) < word.length()) {
			return -1;
		}

		int score = 0;
		int curRow = row;
		int curCol = col;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);

			if (mLock[curRow][curCol] && mBoard[curRow][curCol] != c) {
				return -1;
			} else if (mLock[curRow][curCol]) {
				score++;
			}

			if (direction.isUp()) {
				curRow -= 1;
			} else if (direction.isDown()) {
				curRow += 1;
			}

			if (direction.isLeft()) {
				curCol -= 1;
			} else if (direction.isRight()) {
				curCol += 1;
			}

		}

		return score;
	}
	
	
	
	
	
	private int getAvailableSpace(Direction direction, int row, int col) {
		switch (direction) {
		case DOWN:
			return mRows - row;
		case DOWN_LEFT:
			return Math.min(mRows - row, col);
		case DOWN_RIGHT:
			return Math.min(mRows - row, mColumns - col);
		case LEFT:
			return col;
		case RIGHT:
			return mColumns - col;
		case UP:
			return row;
		case UP_LEFT:
			return Math.min(row, col);
		case UP_RIGHT:
			return Math.min(row, mColumns - col);

		}

		return 0;
	}
	
	
	
	
	private void placeWord(Word word) {
		int curRow = word.getRow();
		int curCol = word.getCol();
		final String wordStr = word.getWord();
		final Direction direction = word.getDirection();
		for (int i = 0; i < wordStr.length(); i++) {
			char c = wordStr.charAt(i);
			//System.out.println(curRow+", "+curCol+", "+c);
			mBoard[curRow][curCol] = c;
			mLock[curRow][curCol] = true;

			if (direction.isUp()) {
				curRow -= 1;
			} else if (direction.isDown()) {
				curRow += 1;
			}

			if (direction.isLeft()) {
				curCol -= 1;
			} else if (direction.isRight()) {
				curCol += 1;
			}
		}

		mSolution.add(word.getWord());
	}
	
	
	
}
