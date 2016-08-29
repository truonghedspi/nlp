package main;

import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

public class PosTaggingMain {
	public static final String mecabOutputFileName = "/home/truong/nlp/reordering/corpus/train.tagged.ja";
	public static final String posFile = "/home/truong/nlp/reordering/corpus/pos.ja";
	public static void main(String[] args) {
		
		ContentProvider provider = new FileProvider(mecabOutputFileName);
		provider.execute();
		
		String content = provider.getContent(); 
		String[] sentences = content.split("\n");
		String sentence ;
		String[] wordSequence;
		String[] right;
		StringBuilder builder = new StringBuilder();
	
		for (int i = 0; i < sentences.length; ++i) {
			sentence = sentences[i];
			if (!sentence.equals("EOS")) {
				wordSequence = sentence.split("\\s+");
				if (wordSequence.length != 2) {
					System.out.println("Error while parsing:"+sentence);
					continue;
				}
				
				right = wordSequence[1].split(",");
				
				
				builder.append(right[0] + " ");
			} else {
				builder.append("\n");
			}
		}
		
		ContentWriter writer = new  FileWriter();
		writer.write(builder.toString(), posFile);
		
	}
}
