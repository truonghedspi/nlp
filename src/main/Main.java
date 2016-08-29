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
import reordering.Block;
import reordering.MatrixMaker;
import writer.ContentWriter;
import writer.FileWriter;




public class Main  { 

	//viet nhat

	public static final String sourceFileName = "/home/truong/nlp/reordering/corpus/train.clean.vi";
	public static final String targetFileName = "/home/truong/nlp/reordering/corpus/train.clean.ja";
	public static final String alignFileName = "/home/truong/nlp/reordering/vi-ja/working/train/model/aligned.grow-diag-final-and";
	
															
	public static void main(String[] args) {
		/*
		AlignmentMatrix matrix = new AlignmentMatrix("0 1 2 3", "0 1 2 3","0-2 1-0 2-3 3-1");
		matrix.blockExtracting();
		
		*/
		
		/*
		Rectangle r1 = new Rectangle(0,0,2,2);
		Rectangle r2 = new Rectangle(2,2,2,2);
		
		System.out.println(r1.isOverlap(r2));

		*/
		
		Block b1 = new Block(1,2,0,2, null);
		Block b2 = new Block(0,1,1,2, null);
		
		System.out.println(b1.isOverlap(b2));
		
	}
	
}

