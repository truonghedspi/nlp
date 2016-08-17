package reordering;

import java.util.*;

public class AlignmentMatrix {
	private String[] mSourceSentence;
	private String[] mTargetSentence;
	private AlignType[][] mMatrix;
	
	private int mMaxRow= -1;
	private int mMaxCol = -1;
	
	public static final String wordSeparateSymbol = " ";
	public static final String alignmentSeparateSymbol = "-";
	
	private List<PairBlock> pairBlocks = new ArrayList<PairBlock>();
	
	public AlignmentMatrix(String sourceSentence, String targetSentence, String aligment) {
		mSourceSentence = sourceSentence.split(wordSeparateSymbol);
		mTargetSentence = targetSentence.split(wordSeparateSymbol);
		
		 mMaxRow = mTargetSentence.length;
		 mMaxCol = mSourceSentence.length; 
		 mMatrix = new AlignType[mMaxRow][mMaxCol];
		 initMatrix(mMaxRow, mMaxCol);
		 
		 String[] aligmentArray = aligment.split(wordSeparateSymbol);
		 
		 for (String subAlign : aligmentArray) {
			String[] align = subAlign.split(alignmentSeparateSymbol);
			int col = Integer.parseInt(align[0]);
			int row = Integer.parseInt(align[1]);
					
			mMatrix[row][col] = AlignType.ALIGN;
		}
		 
	}
	
	
	private void initMatrix(int maxRow, int maxCol) {
		for (int row = 0; row < maxRow; ++row) {
			for (int col = 0; col < maxCol; ++col) {
				mMatrix[row][col] = AlignType.NONE;
			}
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < mMaxRow; ++row) {
			for (int col = 0; col <mMaxCol; ++col) {
				if (mMatrix[row][col] == AlignType.ALIGN ) builder.append("1 ");
				else if (mMatrix[row][col] == AlignType.SEMI) builder.append("x ");
				else builder.append("0 ");
			}
			
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	
	
	public boolean isAlign(int row, int col) {
		if (mMatrix[row][col] == AlignType.ALIGN || mMatrix[row][col] == AlignType.SEMI)  {
			return true;
		}
		return false;
	}
	
	public int getMaxCol() {return mMaxCol;};
	public int getMaxRow() {return mMaxRow;};
	
	public void blockExtracting() {
		new Runnable() {
			public void run() {
				extract();
			}
		}.run();
	
	}
	
	private void print(String msg) {
		System.out.println(msg);
	}
	
	private void extract() {
		
		for (int sCurrent = 1; sCurrent < mSourceSentence.length; ++sCurrent) {
			if (hasAlign(sCurrent, sCurrent-1) == true) {
				print("current :" + sCurrent);
				Block blockB = getBlock(sCurrent, sCurrent);
				Block blockA = getBlock(sCurrent-1, sCurrent-1);
				if (blockA == null || blockB == null) continue;
				print("Block B:"+ blockB.toString());
				print("Block A:"+blockA.toString());
				Block blockG = blockA;
				
				int x = sCurrent - 2;
				while (x >= 0 && blockG.getTargetMin() > blockB.getTargetMax()) {
					

					blockG = getBlock(x, sCurrent-1);
					print("G new:"+ blockG.toString());
					if (blockG.isConsistent()) {
						blockA = blockG;
					}
					--x;
				}
				
				blockG = blockB;
				x = sCurrent+1;
				while (x < mSourceSentence.length &&
						blockA.getTargetMin() > blockB.getTargetMax() &&
						isReorderingConsistent(blockA, blockG) == false
						) {
					blockG = getBlock(sCurrent,x);
					print(blockG.toString());
					
					if (isReorderingConsistent(blockA,blockG) == true) {
						blockB = blockG;
					}
					
					++x;
				}
				addPairBlock(blockA, blockB);
			
			}
		}
			
		
		for (int i = 0; i < pairBlocks.size(); ++i) {
			System.out.println("Shit");
			PairBlock pariblock = pairBlocks.get(i);
			pariblock.toString();
		}

	}
	
	
	private void addPairBlock(Block prev, Block next) {
		pairBlocks.add(new PairBlock(prev, next));
	}
	
	//kiem tra 2 vi tri co xay ra align khong
	private boolean hasAlign(int current, int prev) {
		if (getMaxTarget(current) < getMinTarget(prev)) {
			return true;
		}
		
		return false;
	}
	
	private int getMinTarget(int col) {
		for (int row = 0 ; row < mMaxRow; ++row) {
			if (isAlign(row,col) == true ) return row;
		}
		
		return -1;
	}
	
	private int getMaxTarget(int col) {
		for (int row = mMaxRow-1; row >= 0; --row) {
			if (isAlign(row, col) == true) return row;
		}
		
		return -1;
	}
	
	private Block getBlock(int sMin, int sMax) {
		Block block = null;
		Block blockOnOneColumn = null;
		
		for (int col = sMin; col <= sMax; ++col) {
			blockOnOneColumn = getBlockOnOneColumn(col);
			block = getContainer(block, blockOnOneColumn);
		}
		
		return block;
		
	}
	
	private Block getBlockOnOneColumn(int col) {
		int tMin, tMax;
		
		tMin = getMinTarget(col);
		tMax = getMaxTarget(col);
		if (tMin == -1 || tMax == -1) return null;
		
		return new Block(tMin, tMax, col, col, this);
	}
	
	public boolean isReorderingConsistent(Block a, Block b) {
		
		Block container = getContainer(a,b);
		return container.isConsistent();
	}
	
	public Block getContainer(Block a, Block b) {
		if (a == null) return b;
		if (b == null) return a;
		
		int tMin, tMax, sMin, sMax;
		tMin = a.getTargetMin() < b.getTargetMin() ? a.getTargetMin(): b.getTargetMin();
		tMax = a.getTargetMax() > b.getTargetMax() ? a.getTargetMax(): b.getTargetMax();
		sMin = a.getSourceMin() < b.getSourceMin() ? a.getSourceMin(): b.getSourceMin();
		sMax = a.getSourceMax() > b.getSourceMax() ? a.getSourceMax(): b.getSourceMax(); 
		return new Block(tMin, tMax, sMin, sMax,this);
	}
	
	
	public void discontinousProcess() {
		for (int row = 0; row < mMaxRow; ++row) {
			for (int col = 0; col < mMaxCol-2; ++col) {
				
			}
		}
	}
	
	
}
