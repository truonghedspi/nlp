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
		AlignmentMatrix matrix = new AlignmentMatrix("A B C D", "X Y Z","0-0 1-2 2-0 3-1");
		matrix.blockExtracting();
		
	}
	
}
