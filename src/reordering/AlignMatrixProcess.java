package reordering;

public abstract class AlignMatrixProcess {
	private AlignmentMatrix mAlignMatrix;
	
	public AlignMatrixProcess(AlignmentMatrix matrix) {
		mAlignMatrix = matrix;
	}
	
	public abstract void process();
}
