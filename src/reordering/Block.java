package reordering;

import java.awt.Graphics;

public class Block implements Cloneable {
	private AlignmentMatrix mMatrix;
	private int mTargetMin, mTargetMax, mSourceMax,mSourceMin;
	public static final int BLOCK_SIZE = 30;
	
	public Block(int tMin, int tMax, int sMin,int sMax, AlignmentMatrix matrix) {
		mTargetMin = tMin;
		mTargetMax = tMax;
		mSourceMax = sMax;
		mSourceMin = sMin; 
		mMatrix = matrix;
	}
	
	public boolean isConsistent() {
		
		for (int row = mTargetMin; row <= mTargetMax; ++row) {
			for (int col = mSourceMax+1; col < mMatrix.getMaxCol(); ++col) {
				if (mMatrix.isAlign(row, col) == true) return false;
			}
			
			for (int col = mSourceMin-1; col >= 0; --col) {
				if (mMatrix.isAlign(row, col) == true) return false;
			}
		}
		return true;
	}
	
	public boolean isOverlap(Block b) {
	    //return x(sMin) < r.x + r.width(b.sMax) && x + width(sMax) > r.x(b.sMin) && y(tMin) < r.y + r.height(b.tMax) && y + height(tMax) > r.y(b.tMin);
		 return mSourceMin <= b.getSourceMax() && mSourceMax >= b.getSourceMin() && mTargetMin <= b.getTargetMax() && mTargetMax >= b.getTargetMin();
	}
	
	@Override
	public Block clone() {
		try {
			return (Block)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	//Getter
	public int getTargetMin() {return mTargetMin;}
	public int getTargetMax() {return mTargetMax;}
	public int getSourceMin() {return mSourceMin;}
	public int getSourceMax() {return mSourceMax;}
	public AlignmentMatrix getAlignMatrix() {return mMatrix;}
	
	
	//Setter
	public void setTargetMin(int tmin) {mTargetMin = tmin;}
	public void setTargetMax(int tmax) {mTargetMax = tmax;}
	public void setSourceMin(int smin) {mSourceMin = smin;}
	public void setSourceMax(int smax) {mSourceMax = smax;}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("smin:" + mSourceMin + "|");
		builder.append("smax:" + mSourceMax + "|");
		builder.append("tmin:" + mTargetMin + "|");
		builder.append("tmax:" + mTargetMax + "|");
		return builder.toString();
	}
	
	public boolean isContain(Block block) {
		if (mSourceMax >= block.getSourceMax() 
				&& mSourceMin <= block.getSourceMin()
				&& mTargetMin <= block.getTargetMin()
				&& mTargetMax >= block.getTargetMax()
				) return true;
		return false;
	}
	
	public void setUnAlign() {
		
		for (int row = mTargetMin; row <= mTargetMax; ++row) {
			for (int col = mSourceMin; col <= mSourceMax; ++col) {
				mMatrix.setMatrixCellValue(row, col, AlignType.NONE);
			}
		}
	}
	
	public void moveLeft(int distance) {
		/*Swap value on align matrix*/
		for (int row = mTargetMin; row <= mTargetMax; ++row) {
			for (int col = mSourceMin; col <= mSourceMax; ++col) {
				mMatrix.swapCellValue(row, col, row, col-distance);
			}
		}
		
	}
	
	public void moveRight(int distance) {
		for (int row = mTargetMin; row <= mTargetMax; ++row) {
			for (int col = mSourceMax; col >= mSourceMin; --col) {
				mMatrix.swapCellValue(row, col, row, col+distance);
			}
		}
	
	}
	
	public static Block getContainer(Block b1, Block b2) {
		int smin, smax, tmin, tmax;
		
		smin = b1.getSourceMin() < b2.getSourceMin() ?
				b1.getSourceMin(): b2.getSourceMin();
		smax = b1.getSourceMax() > b2.getSourceMax() ? 
				b1.getSourceMax() : b2.getSourceMax();
		tmin = b1.getTargetMin() < b2.getTargetMin() ?
				b1.getTargetMin(): b2.getTargetMin();
		tmax = b1.getTargetMax() > b2.getTargetMax() ?
				b1.getTargetMax() : b2.getTargetMax();
		
		return new Block(tmin, tmax, smin, smax, b1.getAlignMatrix());
	}

}


