package reordering;

import java.text.RuleBasedCollator;
import java.util.*;

import log.ConsoleLogging;
import log.Logable;

public class AlignmentMatrix {
	private String[] mSourceSentence;
	private String[] mSourceSentenceIndex;
	private String[] mTargetSentence;
	private StringBuilder posRules;
	private AlignType[][] mMatrix;
	
	private int mMaxRow= -1;
	private int mMaxCol = -1;
	
	public static final String wordSeparateSymbol = " ";
	public static final String alignmentSeparateSymbol = "-";
	
	private List<PairBlock> pairBlocks = new ArrayList<PairBlock>();
	private List<Block> reorderingDimension = new ArrayList<Block>();
	
	private Logable logWriter = ConsoleLogging.getInstace();
	
	public AlignmentMatrix(String sourceSentence, String targetSentence, String aligment) {
		mSourceSentence = sourceSentence.split(wordSeparateSymbol);
		initSourceIndexArray();
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
		 
		 posRules = new StringBuilder();
	}

	
	private void initSourceIndexArray() {
		mSourceSentenceIndex = new String[mSourceSentence.length];
		for (int i = 0 ; i < mSourceSentence.length; ++i) {
			mSourceSentenceIndex[i] = i + "";
			System.out.println(mSourceSentence[i]);
			
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
		builder.append("Source sentance index:");
		for (int i = 0; i < mSourceSentenceIndex.length; ++i) {
			builder.append(mSourceSentenceIndex[i] + " ");
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
		
		builder.append("Block Pair:\n");
		for(PairBlock pair : pairBlocks) {
			builder.append(pair.toString());
			builder.append("\n");
		}
		
		builder.append("Block Demension:\n");
		for(Block block : reorderingDimension) {
			builder.append(block.toString());
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
	
	public int getMaxCol() {return mMaxCol;}
	public int getMaxRow() {return mMaxRow;}
	public String[] getSourceIndex() {
		return mSourceSentenceIndex;
	}

	
	public void blockExtracting() {
		new Runnable() {
			public void run() {
				discontinousProcess();
				extract();
				normalizePairBlock();
				print(AlignmentMatrix.this.toString());
				reordering();
				print(AlignmentMatrix.this.toString());
				extractPosRules();
				System.out.println(posRules.toString());
			}
		}.run();
	
	}
	
	private void print(String msg) {
		System.out.println(msg);
	}
	
	private void extract() {
		
		for (int sCurrent = 1; sCurrent < mSourceSentence.length; ++sCurrent) {
			if (hasAlign(sCurrent, sCurrent-1) == true) {
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
						if (highestAlignCol > col) {
							lowestAlignRow = getLowestAlignRow(col, highestAlignCol, row);
							highestAlignRow = getHighestAlignRow(col,highestAlignCol,row);
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
						if (highestAlignRow > row) {
							highestAlignCol = getHighestAlignCol(row,highestAlignRow,col);
							lowestAlignCol = getLowestAlignCol(row,highestAlignRow,col);
							fillDiscontinous(row, highestAlignRow, lowestAlignCol, highestAlignCol);
		
						}
					}
				}
				
			}
		}
		
		
		/*
		 * 
		 * 
		 
		for (int row = 0; row < mMaxRow; ++row) {
			for (int col = 0; col < mMaxCol-2; ++col) {
				if (isAlign(row,col) == true) {
					if (isAlign(row, col+1) == false) {
						highestAlignCol = getHighestAlignCol(row,row,col);
						if (highestAlignCol > col) {
							lowestAlignRow = getLowestAlignRow(col, highestAlignCol, row);
							highestAlignRow = getHighestAlignRow(col,highestAlignCol,row);
							fillDiscontinous(lowestAlignRow, highestAlignRow, col, highestAlignCol);
						}
					}
				}
			}
		}
		
		
		for (int col =0; col < mMaxCol; ++col) {
			for (int row = 0; row < mMaxRow-2;++row) {
				if (isAlign(row,col) == true) {
					if (isAlign(row+1, col) == false) {
						highestAlignRow = getHighestAlignRow(col,col,row);
						if (highestAlignRow > row) {
							highestAlignCol = getHighestAlignCol(row,highestAlignRow,col);
							lowestAlignCol = getLowestAlignCol(row,highestAlignRow,col);
							fillDiscontinous(row, highestAlignRow, lowestAlignCol, highestAlignCol);
		
						}
					}
				}
				
			}
		}
		 * 
		 * 
		 */
	}
	
	private void handleDiscontinousOnRow(int lowerRow, int upperRow) {
		int highestAlignCol, lowestAlignRow, highestAlignRow;
		
		for (int row = lowerRow; row <= upperRow; ++row) {
			for (int col = 0; col < mMaxCol-2; ++col) {
				if (isAlign(row,col) == true) {
					if (isAlign(row, col+1) == false) {
						highestAlignCol = getHighestAlignCol(row,row,col);
						if (highestAlignCol > col) {
							lowestAlignRow = getLowestAlignRow(col, highestAlignCol, row);
							highestAlignRow = getHighestAlignRow(col,highestAlignCol,row);
							fillDiscontinous(lowestAlignRow, highestAlignRow, col, highestAlignCol);
							handleDiscontinousOnRow(lowerRow, upperRow);						}
					}
				}
			}
		}
	} 

	
	private void handleDiscontinousOnCol(int lowerCol, int upperCol) {
		int highestAlignRow, highestAlignCol, lowestAlignCol;
		
		for (int col =lowerCol; col <= upperCol; ++col) {
			for (int row = 0; row < mMaxRow-2;++row) {
				if (isAlign(row,col) == true) {
					if (isAlign(row+1, col) == false) {
						highestAlignRow = getHighestAlignRow(col,col,row);
						if (highestAlignRow > row) {
							highestAlignCol = getHighestAlignCol(row,highestAlignRow,col);
							lowestAlignCol = getLowestAlignCol(row,highestAlignRow,col);
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
	
	public void normalizePairBlock() {
		List<PairBlock> temp = new ArrayList(pairBlocks);
		List<PairBlock> res = new ArrayList<PairBlock>();
		List<Block> containers = new ArrayList<Block>(); 
		List<Block> ignorList = new ArrayList<Block>();
		
		Block curBlock, otherBlock;
		
		for (int i = 0 ; i < temp.size(); ++i) {
			containers.add(temp.get(i).getContainer());
		}
		
		for (int i = 0 ; i < containers.size(); ++i) {
			curBlock = containers.get(i);
			for (int j = 0; j < containers.size(); ++j) {
				if (i== j) continue;
				otherBlock = containers.get(j);
				if (curBlock.isContain(otherBlock)) {
					ignorList.add(otherBlock);
				}
			}
		}
		
		for (int i = 0; i < containers.size(); ++i) {
			if (ignorList.contains(containers.get(i)) == false) {
				reorderingDimension.add(containers.get(i));
			}
		}
	}
	
	
	public void printPairsBlock(List<PairBlock> pairBlocks) {
		for (PairBlock pairBlock : pairBlocks) {
			logWriter.log(pairBlock.toString());
		}
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
	
	
	public void reordering() {
		for (PairBlock pair: pairBlocks) {
			pair.swap();
		}
				
	}
	
	public void getPosReorderingRules() {
		
	}
	
	//GETTER
	public AlignType[][] getMaxtrix() {return mMatrix;}
	public String[] getSourceSentence() {return mSourceSentence;}
	
	
	public void moveLeftBlocksOnContainer(Block container, int distance) {
		if (distance <=0 ) return;
		
		Block next, prev;
		for (PairBlock pair: pairBlocks) {
			next = pair.getBlockNext();
			prev = pair.getBlockPrev();
			if (container.isContain(next) && container != next) {
				next.setSourceMax(next.getSourceMax()-distance);
				next.setSourceMin(next.getSourceMin()-distance);
			} 
			
			if (container.isContain(prev) && container != prev) {
				prev.setSourceMax(prev.getSourceMax()-distance);
				prev.setSourceMin(prev.getSourceMin()-distance);
			}
		}
	}
	
	
	public void moveRightBlocksOnContainer(Block container, int distance) {
		Block next, prev;
		for (PairBlock pair: pairBlocks) {
			next = pair.getBlockNext();
			prev = pair.getBlockPrev();
			if (container.isContain(next) && container != next) {
				next.setSourceMax(next.getSourceMax()+distance);
				next.setSourceMin(next.getSourceMin()+distance);
			} 
			
			if (container.isContain(prev) && container != prev) {
				prev.setSourceMax(prev.getSourceMax()+distance);
				prev.setSourceMin(prev.getSourceMin()+distance);
			}
		}
	}
	
	private void extractPosRules() {
		
		int lower,upper ;
		StringBuilder tempBuilder = new StringBuilder();
		
		for(Block block: reorderingDimension) {
			 lower = block.getSourceMin();
			 upper = block.getSourceMax();
			 for (int i = lower ; i <= upper; ++i) {
				 posRules.append(mSourceSentence[i] + " ");
				 tempBuilder.append((Integer.parseInt(mSourceSentenceIndex[i])  - lower) + " ");
			 }
			 posRules.append(":");
			 posRules.append(tempBuilder);
			 posRules.append("\n");
			 tempBuilder.delete(0, tempBuilder.length());
		}
		
		
		
		
	}
	
}
 