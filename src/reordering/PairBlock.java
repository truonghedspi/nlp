package reordering;

import log.ConsoleLogging;

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
		
		smin = mBlockNext.getSourceMin() < mBlockPrev.getSourceMin() ?
				mBlockNext.getSourceMin(): mBlockPrev.getSourceMin();
		smax = mBlockNext.getSourceMax() > mBlockPrev.getSourceMax() ? 
				mBlockNext.getSourceMax() : mBlockPrev.getSourceMax();
		tmin = mBlockNext.getTargetMin() < mBlockPrev.getTargetMin() ?
				mBlockNext.getTargetMin(): mBlockPrev.getTargetMin();
		tmax = mBlockNext.getTargetMax() > mBlockPrev.getTargetMax() ?
				mBlockNext.getTargetMax() : mBlockPrev.getTargetMax();
		
		return new Block(tmin, tmax, smin, smax, mBlockNext.getAlignMatrix());
	}
	
	//kiem tra xem 2 cap pair block co bao gom nhau khong?
	public boolean isContain(PairBlock other) {
		return getContainer().isContain(other.getContainer());
	}
	
	
	public void swap() {
		Block next = getBlockNext();
		Block prev = getBlockPrev();
		int nextDistance, prevDistance;
		
		
		System.out.println("swap:\n");
		System.out.println(next.toString());
		System.out.println(prev.toString());
		
		nextDistance = prev.getSourceMax()-prev.getSourceMin()+1;
		prevDistance = next.getSourceMax()-next.getSourceMin()+1;
		
		System.out.println("next distance:"+nextDistance);
		System.out.println("prev distance:"+prevDistance);
		next.moveLeft(nextDistance);
		prev.moveRight(prevDistance);
	
		
		/*swap*/
		/*
		String[] sourceSentence = next.getAlignMatrix().getSourceSentence();
		
		
		int i = 0;
		int distance = prev.getSourceMax()-prev.getSourceMin()+1;
		while (i < distance) {
			moveSourceSubStringLeft(sourceSentence, next.getSourceMin()-i, next.getSourceMax()-i);
			++i;
		}
		
		*/
		int distance = prev.getSourceMax()-prev.getSourceMin()+1;
		moveSubString(next.getAlignMatrix().getSourceIndex(), distance,next );
		AlignmentMatrix matrix = next.getAlignMatrix();
		
		matrix.moveLeftBlocksOnContainer(next, nextDistance);
		next.setSourceMax(next.getSourceMax()-nextDistance);
		next.setSourceMin(next.getSourceMin()-nextDistance);
		matrix.moveRightBlocksOnContainer(prev, prevDistance);
		prev.setSourceMax(prev.getSourceMax()+prevDistance);
		prev.setSourceMin(prev.getSourceMin()+prevDistance);

	}
	
	private void moveSubString(String[] strArr, int distance, Block next) {
		int i = 0;
		while (i < distance) {
			moveSourceSubStringLeft(strArr, next.getSourceMin()-i, next.getSourceMax()-i);
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
			sourceSentence[i] = sourceSentence[i+1];
		}
		sourceSentence[upper] = temp;
	}
}
