package nlp;

import java.io.File;
import java.util.Arrays;

import processor.ContentProcessor;
import processor.DeleteShortSentenceProcessor;
import processor.RemovePosProcessor;
import processor.SplitTrainingData;
import processor.WordCounter;
import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

public class Main { 
	public static final String sourceFileName = "/home/truong/nlp/vi-ja/reordering/GOME.tagged.vi.out";
	public static final String targetFileName = "/home/truong/nlp/vi-ja/reordering/GOME.tagged.ja.out";
	public static final String[] fileNames = {
		"/home/truong/nlp/vi-ja/reordering/corpus/GOME.tagged.vi.out.dev",
		"/home/truong/nlp/vi-ja/reordering/corpus/GOME.tagged.vi.out.test",
		"/home/truong/nlp/vi-ja/reordering/corpus/GOME.tagged.vi.out.train",
	};
	
	public static void main(String[] args) {
		
		ContentProcessor processor = new RemovePosProcessor(Arrays.asList(fileNames));
		processor.process();


	}
	
}
