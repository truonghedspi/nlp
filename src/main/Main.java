package main;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import processor.AvgLengthSentenceProcessor;
import processor.ContentProcessor;
import processor.DeleteShortSentenceProcessor;
import processor.RemovePosProcessor;
import processor.SplitTrainingData;
import processor.WordCounter;
import provider.ContentProvider;
import provider.FileProvider;
import reordering.AlignmentMatrix;
import reordering.Block;
import reordering.MatrixMaker;
import writer.ContentWriter;
import writer.FileWriter;




public class Main  { 
	//viet nhat
	public static final String sourceFileName = "/home/truong/nlp/reordering/corpus/pos.ja";
	public static final String targetFileName = "/home/truong/nlp/reordering/corpus/train.clean.vi";
	public static final String alignFileName = "/home/truong/nlp/reordering/ja-vi/working/train/model/aligned.grow-diag-final-and";
	
															
	public static void main(String[] args) {
		MatrixMaker.getInstance().make(sourceFileName, targetFileName, alignFileName);
		System.out.println(MatrixMaker.getInstance().getMatrixs().size());
		List<AlignmentMatrix> matrixs =MatrixMaker.getInstance().getMatrixs();
		AlignmentMatrix matrix;
		
		for (int i = 0; i < matrixs.size(); ++i) {
			matrix = matrixs.get(i);
			matrix.blockExtracting();
		}
		
		
		//matrixs.get(96).blockExtracting();
	}
	
}

