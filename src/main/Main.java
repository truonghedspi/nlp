package main;

import java.io.File;
import java.util.Arrays;

import processor.AvgLengthSentenceProcessor;
import processor.ContentProcessor;
import processor.DeleteShortSentenceProcessor;
import processor.RemovePosProcessor;
import processor.SplitTrainingData;
import processor.WordCounter;
import provider.ContentProvider;
import provider.FileProvider;
import reordering.AlignmentMatrix;
import reordering.MatrixMaker;
import writer.ContentWriter;
import writer.FileWriter;

public class Main  { 
	//viet nhat
	
	public static final String sourceFileName = "/home/truong/nlp/reordering/corpus/train.clean.vi";
	public static final String targetFileName = "/home/truong/nlp/reordering/corpus/train.clean.ja";
	public static final String alignFileName = "/home/truong/nlp/reordering/vi-ja/working/train/model/aligned.grow-diag-final-and";
	
															
	public static void main(String[] args) {
		AlignmentMatrix matrix = new AlignmentMatrix("0 1 2 3", "0 1 2 3 4","0-2 0-4 1-3 2-0 3-1");
		System.out.println(matrix.toString());
		matrix.discontinousProcess();
		
		System.out.println(matrix.toString());
		matrix.blockExtracting();
		System.out.println("Block after normalize");
		matrix.printPairsBlock(matrix.normalizePairBlock());
		matrix.reordering();
		System.out.println(matrix.toString());
		//matrix.
		
	}
	
}
