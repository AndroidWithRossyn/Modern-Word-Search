package generator;

public class Word implements Comparable<Word>{
	private String mWord;
	private int mRow, mCol;
	private Direction mDirection;

	

	public int compareTo(Word another) {
		return mWord.compareTo(another.getWord());
	}

	
	public int describeContents() {
		return 0;
	}

	

	public Word(String word, int row, int col, Direction direction) {
		super();
		mWord = word;
		mRow = row;
		mCol = col;
		mDirection = direction;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return mWord;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return mRow;
	}

	/**
	 * @return the col
	 */
	public int getCol() {
		return mCol;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return mDirection;
	}

	// public int getFirstPosition() {
	// return (mRow * mColumns) + mCol;
	// }

	@Override
	public String toString() {
		return "Word [mWord=" + mWord + ", mRow=" + mRow + ", mCol=" + mCol + ", mDirection=" + mDirection
				+ "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mCol;
		result = prime * result + ((mDirection == null) ? 0 : mDirection.hashCode());
		result = prime * result + mRow;
		result = prime * result + ((mWord == null) ? 0 : mWord.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (mCol != other.mCol)
			return false;
		if (mDirection != other.mDirection)
			return false;
		if (mRow != other.mRow)
			return false;
		if (mWord == null) {
			if (other.mWord != null)
				return false;
		} else if (!mWord.equals(other.mWord))
			return false;
		return true;
	}
}
