package processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

public class DeleteShortSentenceProcessor extends ContentProcessor{
	
	private String mSourceFileName;
	
	private String mTargetFileName;
	
	private String mSentenceMarker;
	
	private int mMinLength;
	
	
	public DeleteShortSentenceProcessor(String sourceFileName
			, String targetFileName
			, String sentenceMarker
			,int minLength) {
		mSourceFileName = sourceFileName;
		mTargetFileName = targetFileName;
		mSentenceMarker = sentenceMarker;
		mMinLength = minLength;
	}
	
	@Override
	public void process() {
		ContentProvider sourceProvider = new FileProvider(mSourceFileName);
		sourceProvider.execute();
		ContentProvider targetProvider = new FileProvider(mTargetFileName);
		targetProvider.execute();
		
		String sourceContent = sourceProvider.getContent();
		String targetContent = targetProvider.getContent();
	
		
		List<String> sourceSentences = new ArrayList<String>(Arrays.asList(sourceContent.split(mSentenceMarker)));
		List<String> targetSentences = new ArrayList<String>(Arrays.asList(targetContent.split(mSentenceMarker)));
		
		if (sourceSentences == null || targetSentences == null 
				|| sourceSentences.size() != targetSentences.size()) {
			return;
		}
		
		for (int i = 0; i < sourceSentences.size(); ++i) {
			if (sourceSentences.get(i).split(" ").length < mMinLength 
					|| targetSentences.get(i).split(" ").length < mMinLength) {
				sourceSentences.remove(i);
				targetSentences.remove(i);
				--i;
			}
		}
		
		//Write to file
		
		String sourceOutFileName = getOutFileName(mSourceFileName);
		String targetOutFileName = getOutFileName(mTargetFileName);
		
		StringBuilder targetStringBuilder = new StringBuilder();
		StringBuilder sourceStringBuilder = new StringBuilder();
		
		for (int i = 0; i < sourceSentences.size(); ++i) {
			targetStringBuilder.append(targetSentences.get(i));
			sourceStringBuilder.append(sourceSentences.get(i));
			if (i < sourceSentences.size()-1) {
				targetStringBuilder.append(mSentenceMarker);
				sourceStringBuilder.append(mSentenceMarker);
			} 
		}
		
		ContentWriter sourceWriter = new FileWriter();
		ContentWriter targetWriter = new FileWriter();
		sourceWriter.write(sourceStringBuilder.toString(),sourceOutFileName);
		targetWriter.write(targetStringBuilder.toString(), targetOutFileName);
		
	}

	private String getOutFileName(String inputFileName) {
		
		return inputFileName + ".out";
		
		/*
		if (inputFileName.indexOf(".") > 0) {
			return inputFileName.substring(0, inputFileName.lastIndexOf("."));
		}
		
		return null;
		*/
	}
}
