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
		
		System.out.println(builder.toString());
		return builder.toString();
		
	}
	

}
