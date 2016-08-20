package reordering;

public class PairBlock {
	private Block mBlockPrev;
	private Block mBlockNext;
	
	public PairBlock(Block prev, Block next) {
		mBlockPrev = prev;
		mBlockNext = next;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockA:\n" + mBlockPrev.toString());
		builder.append("\n");
		builder.append("BlockB:\n" + mBlockNext.toString());
		
		return builder.toString();
		
	}
	
	public Block getBlockPrev() {return mBlockPrev;}
	public Block getBlockNext() {return mBlockNext;}
	
	/*lay ra block gop boi 2 block */
	public Block getContainer() {
		int smin, smax, tmin, tmax;
		
		smin = mBlockPrev.getSourceMin();
		smax = mBlockNext.getSourceMax();
		tmin = mBlockNext.getSourceMin();
		tmax = mBlockPrev.getTargetMax();
		
		return new Block(tmin, tmax, smin, smax, mBlockNext.getAlignMatrix());
	}
	
	//kiem tra xem 2 cap pair block co bao gom nhau khong?
	public boolean isContain(PairBlock other) {
		return mBlockPrev.isContain(other.getBlockPrev());
	}
	
	
	public void swap() {
		Block next = getBlockNext();
		Block prev = getBlockPrev();
		
		next.moveLeft(prev.getSourceMax()-prev.getSourceMin()+1);
		prev.moveRight(next.getSourceMax()-next.getSourceMin()+1);
		
		/*swap*/
		String[] sourceSentence = next.getAlignMatrix().getSourceSentence();
		
		
		int i = 0;
		int distance = prev.getSourceMax()-prev.getSourceMin()+1;
		while (i < distance) {
			moveSourceSubStringLeft(sourceSentence, next.getSourceMin()-i, next.getSourceMax()-i);
			System.out.println("A:" + next.getSourceMin());
			System.out.println("B: " + next.getSourceMax());
			++i;
		}
		
		
	}
	
	
	/**
	 * Move substring on source sentence left
	 * Sub Sentence was limit by lower and upper position
	 * @param sourceSentence
	 * @param lower
	 * @param upper
	 */
	private void moveSourceSubStringLeft(String[] sourceSentence, int lower, int upper) {
		if (lower == 0) return;
		String temp = sourceSentence[lower-1];
		for (int i = lower-1; i <= upper-1; ++i) {
			System.out.println("Gan:"+sourceSentence[i]+"bang"+sourceSentence[i+1]);
			sourceSentence[i] = sourceSentence[i+1];
		}
		sourceSentence[upper] = temp;
	}
	
	

}
