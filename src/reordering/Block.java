package reordering;

public class Block implements Cloneable{
	private AlignmentMatrix mMatrix;
	private int mTargetMin, mTargetMax, mSourceMax,mSourceMin;
	
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
	
	@Override
	public Block clone() {
		try {
			return (Block)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public int getTargetMin() {return mTargetMin;}
	public int getTargetMax() {return mTargetMax;}
	public int getSourceMin() {return mSourceMin;}
	public int getSourceMax() {return mSourceMax;}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("smin:" + mSourceMin + "|");
		builder.append("smax:" + mSourceMax + "|");
		builder.append("tmin:" + mTargetMin + "|");
		builder.append("tmax:" + mTargetMax + "|");
		return builder.toString();
	}
		
}
