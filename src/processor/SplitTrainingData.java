package processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

public class SplitTrainingData extends ContentProcessor{
	public static final String TEST_FILE_NAME_EXTEND = ".test";
	public static final String DEV_FILE_NAME_EXTENDS = ".dev";
	public static final String TRAIN_FILE_NAME_EXTENDS = ".train";
	
	public static final float TRAIN_PERSENT = 0.7f;
	public static final float DEV_PERSENT = 0.2f;
	/*
	 * Train 70%
	 * Dev: 20%
	 * Test: 10%
	 */
	
	private String mSourceFileName  = null;
	private String mTargetFileName = null;
	
	public SplitTrainingData(String sourceFileName, String targetFileName) {
		mSourceFileName = sourceFileName;
		mTargetFileName = targetFileName; 
	}
	
	public void process() {
		
		/*
		 * Read file
		 * 
		 */
		ContentProvider sourceProvider = new FileProvider(mSourceFileName);
		sourceProvider.execute();
		ContentProvider targetProvider = new FileProvider(mTargetFileName);
		targetProvider.execute();
		
		String sourceContent = sourceProvider.getContent();
		String targetContent = targetProvider.getContent();
		
		
		
		List<String> sourceList = new ArrayList<String>(Arrays.asList(sourceContent.split("\n")));
		List<String> targetList = new ArrayList<String>(Arrays.asList(targetContent.split("\n")));
		System.out.println(sourceList.size() + "=" + targetList.size());
		
		int totalSentence = sourceList.size();
		
		int maxTrainSentence = (int)(totalSentence * TRAIN_PERSENT);
		int maxDevSentence = (int) (totalSentence * DEV_PERSENT);
		
		
		String[] train = getData(sourceList, targetList, maxTrainSentence);
		
		String[] dev = getData(sourceList, targetList, maxDevSentence);
		
		String sourceTestString = convertListStringToString(sourceList);
		String targetTestString = convertListStringToString(targetList);
		
		ContentWriter writer = new FileWriter();
		writer.write(train[0], mSourceFileName+ TRAIN_FILE_NAME_EXTENDS);
		writer.write(train[1], mTargetFileName+ TRAIN_FILE_NAME_EXTENDS);
		writer.write(dev[0], mSourceFileName+ DEV_FILE_NAME_EXTENDS);
		writer.write(dev[1], mTargetFileName+ DEV_FILE_NAME_EXTENDS);
		writer.write(sourceTestString, mSourceFileName+ TEST_FILE_NAME_EXTEND);
		writer.write(targetTestString, mTargetFileName+ TEST_FILE_NAME_EXTEND);
	}
	
	private String convertListStringToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0 ; i < list.size(); ++i) {
			builder.append(list.get(i));
			if (i < list.size() -1) {
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
	
	private String[] getData(List<String> sourceList,
			List<String> targetList, 
			int maxSentence) {
		StringBuilder sourceStringBuilder = new StringBuilder();
		StringBuilder targetStringBuilder = new StringBuilder();
		
		
		int currentSentence = 0;

		System.out.println(sourceList.size());
		while (currentSentence < maxSentence) {
			int index = ThreadLocalRandom.current().nextInt(0, sourceList.size());
			sourceStringBuilder.append(sourceList.get(index));
			targetStringBuilder.append(targetList.get(index));
			if (currentSentence < maxSentence -1 ) {
				sourceStringBuilder.append("\n");
				targetStringBuilder.append("\n");
			}
			
			sourceList.remove(index);
			targetList.remove(index);
			
			++currentSentence;
		}
		
		
		String[] result = new String[2];
		result[0] = sourceStringBuilder.toString();
		result[1] = targetStringBuilder.toString();
		return result;
		
	}
	
	
	
}
