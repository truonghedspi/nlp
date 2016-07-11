package processor;

import provider.ContentProvider;
import provider.FileProvider;

public class WordCounter extends ContentProcessor{
	private int mCoutLength1 = 0;
	private int mCountLength2 = 0;
	private int mCountLengthGreater3 = 0;
	
	private String mFileName ;
	
	private String mLineMarker;
	
	public WordCounter(String fileName, String lineMarker) {
		mFileName = fileName;
		mLineMarker = lineMarker;
	}
	

	@Override
	public void process() {
		ContentProvider provider = new FileProvider(mFileName);
		provider.execute();
		String content = provider.getContent();
		
		String[] lines = content.split(mLineMarker);
		
		for (String line : lines) {
			String[] wordsInLine = line.split(" ");
			switch (wordsInLine.length) {
			case 0:
				break;
			case 1:
				++mCoutLength1;
				break;
			case 2:
				++mCountLength2;
				break;
			default:
				++mCountLengthGreater3;
				break;
			}
		}
		
		
		//print result
		
		
		System.out.println("So cau co 1 tu: " + mCoutLength1);
		System.out.println("So cau co 2 tu: "+ mCountLength2);
		System.out.println("So cau co lon hon 3 tu: " + mCountLengthGreater3);
		System.out.println("Tong so cau:" + lines.length);
		
	}
	
}
