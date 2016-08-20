package reordering;

import java.util.*;

import log.ConsoleLogging;
import log.Logable;

public class AlignmentMatrix {
	private String[] mSourceSentence;
	private String[] mTargetSentence;
	private AlignType[][] mMatrix;
	
	private int mMaxRow= -1;
	private int mMaxCol = -1;
	
	public static final String wordSeparateSymbol = " ";
	public static final String alignmentSeparateSymbol = "-";
	
	private List<PairBlock> pairBlocks = new ArrayList<PairBlock>();
	
	private Logable logWriter = ConsoleLogging.getInstace();
	
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
		builder.append("Source sentance:");
		for (int i = 0; i < mSourceSentence.length; ++i) {
			builder.append(mSourceSentence[i] + " ");
		}
		builder.append("\n");
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
				Block blockG = blockA;
				
				int x = sCurrent - 2;
				while (x >= 0 && blockG.getTargetMin() > blockB.getTargetMax()) {
					

					blockG = getBlock(x, sCurrent-1);
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
		int lowestAlignCol;
		int highestAlignCol;
		int lowestAlignRow;
		int highestAlignRow;
		
		/*handle when occur discontinous align on row*/
		for (int row = 0; row < mMaxRow; ++row) {
			for (int col = 0; col < mMaxCol-2; ++col) {
				if (isAlign(row,col) == true) {
					if (isAlign(row, col+1) == false) {
						highestAlignCol = getHighestAlignCol(row,row,col);
						logWriter.log("highestAlignCol:" + highestAlignCol);
						if (highestAlignCol > col) {
							lowestAlignRow = getLowestAlignRow(col, highestAlignCol, row);
							highestAlignRow = getHighestAlignRow(col,highestAlignCol,row);
							logWriter.log("lowestAlignRow:"+lowestAlignRow);
							logWriter.log("highestAlignRow:"+highestAlignRow);
							fillDiscontinous(lowestAlignRow, highestAlignRow, col, highestAlignCol);
						}
					}
				}
			}
		}
		
		
		/*Handle when discontinous align occur on col*/
		for (int col =0; col < mMaxCol; ++col) {
			for (int row = 0; row < mMaxRow-2;++row) {
				if (isAlign(row,col) == true) {
					if (isAlign(row+1, col) == false) {
						highestAlignRow = getHighestAlignRow(col,col,row);
						logWriter.log("highestAlignRow:"+highestAlignRow);
						if (highestAlignRow > row) {
							highestAlignCol = getHighestAlignCol(row,highestAlignRow,col);
							logWriter.log("highestAlignCol:"+highestAlignCol);
							lowestAlignCol = getLowestAlignCol(row,highestAlignRow,col);
							logWriter.log("lowestAlignCol"+lowestAlignCol);
							fillDiscontinous(row, highestAlignRow, lowestAlignCol, highestAlignCol);
		
						}
					}
				}
				
			}
		}
	}
	
	
	private void fillDiscontinous(int lowerRow, int higherRow, int lowerCol, int higherCol) {
		for (int row = lowerRow; row <= higherRow; ++row) {
			for (int col = lowerCol; col <= higherCol; ++col) {
				if (isAlign(row, col) == false) mMatrix[row][col] = AlignType.SEMI;
			}
		}
	}
	
	
	//tra ve hang thap nhat co align trong khoang tu lower den upper, bi gioi han boi row
	private int getLowestAlignRow(int lower, int upper, int row) {
		logWriter.log("getLowestAlignRow");
		int lowestRow = row;
		int r = 0;
	
		for (int col = lower; col <= upper; ++col) {
			while ((r <= row) && (isAlign(r, col)) == false) ++r;
			if (r <lowestRow) lowestRow = r;
			r = 0;
			continue;
		}
		
		return lowestRow;
	}
	
	//tra ve hang cao nhat co align trong khoang tu lower den upper, bi gioi han boi row
	
	private int getHighestAlignRow(int lower, int upper, int row) {
		logWriter.log("getHighestAlignRow");
		int highestRow = row;
		int r = mMaxRow-1;
		
		for (int col = lower; col <= upper; ++col) {
			while((r >= row) && (isAlign(r, col) == false)) --r;
			if (r > highestRow) highestRow = r;
			r = mMaxRow-1;
			continue;
		}
		return highestRow;
	}
	
	//tra ve cot cao nhat trong khoang hang` tu lower den upper, 
	
	private int getLowestAlignCol(int lower, int upper, int col) {
		logWriter.log("getLowestAlignCol");
		int lowestCol = col;
		int c = 0;
		
		for (int row = lower; row <= upper; ++row) {
			while((c <= col) && (isAlign(row,c)) == false) ++c;
			if (c < lowestCol) lowestCol = c;
			c = 0;
			continue;
		}
		return lowestCol;
	}
	
	//tra ve cot thap nhat trong khoang hang` tu lower den upper, 
	private int getHighestAlignCol(int lower, int upper, int col) {
		logWriter.log("getHighestAlignCol");
		int highestCol = col;
		int c = mMaxCol - 1;
		for (int row = lower; row <= upper; ++row) {
			while((c >= col) && (isAlign(row,c) == false)) --c;
			if (c > highestCol) highestCol =  c;
			
			c = mMaxCol - 1;
			continue;
		}
		return highestCol;
	}
	
	public List normalizePairBlock() {
		List<PairBlock> temp = new ArrayList(pairBlocks);
		List<PairBlock> res = new ArrayList<PairBlock>();
		List<PairBlock> removeList = new ArrayList<PairBlock>();
		
		if (pairBlocks.size() == 1) return pairBlocks;
		PairBlock currentPair, otherPair;
		
		for (int i = temp.size()-1; i > 0; --i) {
			currentPair = temp.get(i);
			for (int j = i-1; j >= 0; --j) {
				otherPair = temp.get(j);
				if (currentPair.isContain(otherPair)) {
					removeList.add(otherPair);
				}
			}
		}
		
		for (PairBlock pairBlock : removeList) {
			temp.remove(pairBlock);
		}
		return temp;
	}
	
	
	public void printPairsBlock(List<PairBlock> pairBlocks) {
		for (PairBlock pairBlock : pairBlocks) {
			logWriter.log(pairBlock.toString());
		}
	}
	
	
	private void swap(PairBlock pair) {
		/*
		AlignType[][] matrix = cloneMatrix();
		Block next = pair.getBlockNext();
		Block prev = pair.getBlockPrev();
		
		setUnAlign(pair);
		for (int row = next.getTargetMin(); row <= next.getTargetMax(); ++row) {
			for (int col = prev.getSourceMin(); 
					col <= prev.getSourceMin() + next.getSourceMax()-next.getSourceMin();
					++col) {
				mMatrix[row][col] = matrix[row][col+next.getSourceMax()-next.getSourceMin()];
			}
		}
		*/
	}
	
	
	
	private AlignType[][] cloneMatrix() {
		AlignType[][] matrix = new AlignType[mMaxRow][mMaxCol];
		for (int row = 0; row < mMaxRow; ++row) {
			for (int col = 0; col < mMaxCol; ++col) {
				matrix[row][col] = mMatrix[row][col];
			}
		}
		return matrix;
	}
	
	
	
	public void setMatrixCellValue(int row, int col, AlignType type) {
		mMatrix[row][col] = type;
	}
	
	public void swapCellValue(int row1, int col1, int row2, int col2) {
		AlignType temp = mMatrix[row1][col1];
		
		setMatrixCellValue(row1,col1,mMatrix[row2][col2]);
		setMatrixCellValue(row2,col2,temp);
	}
	
	/**
	 * use to swap 2 positions of source sentence
	 * @param p1:position 1
	 * @param p2:position 2
	 */
	public void swapSourceSentence(int p1, int p2) {
		
	}
	
	public void reordering() {
		/*swap in matrix*/
		for (PairBlock pair: pairBlocks) {
			pair.swap();
		}
		/*swap in source sentence*/
		//TODO:implement swap POS TAG
	}
	
	//GETTER
	public AlignType[][] getMaxtrix() {return mMatrix;}
	public String[] getSourceSentence() {return mSourceSentence;}
	
}
 