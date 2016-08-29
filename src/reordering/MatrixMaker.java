package reordering;


import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

import java.util.*;

public class MatrixMaker {
	private String mSourceCorpusFileName;
	private String mTargetCorpusFileName;
	private String mAlignmentFileName;
	
	private String mSouceContent;
	private String mTargetContent;
	private String mAlignmentContent;
	
	private static volatile int completedMatrix = 0;
	private static int totalNumberMatrix;
	
	
	
	public static final String wordSeparateSymbol = " ";
	public static final String sentenceSeparateSymbol = "\n";
	public static final String mPosResultFileName = "~/result.txt";
	
	private List<AlignmentMatrix> matrixs = new ArrayList<AlignmentMatrix>();
	
	private static MatrixMaker instance = null;
	
	private MatrixMaker() {}
	
	public static synchronized MatrixMaker getInstance() {
		if (instance == null) instance = new MatrixMaker();
		return instance;
	}

	
	
	public synchronized void increaseNumberOfCompletedMatrix() {
		++completedMatrix;
		if (completedMatrix == totalNumberMatrix) {
			StringBuilder builder = new StringBuilder();
			for(AlignmentMatrix matrix: matrixs) {
				builder.append(matrix.getPosRules());
			}
			
			ContentWriter writer = new FileWriter();
			writer.write(builder.toString(), mPosResultFileName);
		}
	}
	
	public void make(String sourceFileName,String targetFileName, String alignmentFileName) {
		
		mSourceCorpusFileName = sourceFileName;
		mTargetCorpusFileName = targetFileName;
		mAlignmentFileName = alignmentFileName;
		
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
		totalNumberMatrix = sourceContentArray.length;
		
		makeMatrix(sourceContentArray,targetContentArray, alignContentArray);
	}
	
	
	private void makeMatrix(String[] source, String[] target, String[] align) {
		for (int i = 0 ; i< source.length; ++i) {
			matrixs.add(new AlignmentMatrix(source[i], target[i], align[i]));
		}
	}
	
	
	public List getMatrixs() {return matrixs;}
	
	

}
