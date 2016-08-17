package reordering;


import provider.ContentProvider;
import provider.FileProvider;
import java.util.*;

public class MatrixMaker {
	private String mSourceCorpusFileName;
	private String mTargetCorpusFileName;
	private String mAlignmentFileName;
	
	private String mSouceContent;
	private String mTargetContent;
	private String mAlignmentContent;
	
	public static final String wordSeparateSymbol = " ";
	public static final String sentenceSeparateSymbol = "\n";
	
	private List<AlignmentMatrix> matrixs = new ArrayList<AlignmentMatrix>();
	
	public MatrixMaker(String sourceFileName,String targetFileName, String alignmentFileName) {
		mSourceCorpusFileName = sourceFileName;
		mTargetCorpusFileName = targetFileName;
		mAlignmentFileName = alignmentFileName;
	}
	
	public void make() {
		ContentProvider sourceProvider = new FileProvider(mSourceCorpusFileName);
		sourceProvider.execute();
		ContentProvider targetProvider = new FileProvider(mTargetCorpusFileName);
		targetProvider.execute();
		ContentProvider alignmentProvider = new FileProvider(mAlignmentFileName);
		alignmentProvider.execute();
		
		mSouceContent = sourceProvider.getContent();
		mTargetContent = targetProvider.getContent();
		mAlignmentContent = alignmentProvider.getContent();
		
		String[] sourceContentArray = mSouceContent.split(sentenceSeparateSymbol);
		String[] targetContentArray = mTargetContent.split(sentenceSeparateSymbol);
		String[] alignContentArray = mAlignmentContent.split(sentenceSeparateSymbol);

		
		makeMatrix(sourceContentArray,targetContentArray, alignContentArray);
		
	}
	
	
	private void makeMatrix(String[] source, String[] target, String[] align) {
		for (int i = 0 ; i< source.length; ++i) {
			matrixs.add(new AlignmentMatrix(source[i], target[i], align[i]));
		}
	}
	
	public List getMatrixs() {return matrixs;}

}
