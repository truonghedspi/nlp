package processor;

import java.util.Observable;

import processor.*;
import provider.ContentProvider;
import provider.FileProvider;

public class AvgLengthSentenceProcessor extends ContentProcessor 
				implements FileProvider.Observer{
	
	private Observer mListener = null;
	private String mFileName;
	private float avgLength = 0.0f;
	
	public AvgLengthSentenceProcessor(String fileName, Observer listener) {
		mListener = listener;
		mFileName = fileName;
	
	}
	
	public interface Observer {
		void onProcessorCompleted(float avgLength);
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		ContentProvider provider = new FileProvider(mFileName, this);
		provider.execute();
		
	}

	public void onGetContentSuccess(String content) {
		String[] lines = content.split("\n");
		for (String line : lines) {
			String[] wordsInLine = line.split(" ");
			avgLength += (wordsInLine.length / 1.0f)/lines.length;
		}
		
		if (mListener != null) {
			mListener.onProcessorCompleted(avgLength);
		}
		
		System.out.println("Length avg of file " + mFileName + " : " + avgLength);
		
	}
	
}
