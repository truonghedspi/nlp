package processor;

import java.util.List;

import provider.ContentProvider;
import provider.FileProvider;
import writer.ContentWriter;
import writer.FileWriter;

public class RemovePosProcessor extends ContentProcessor{
	private List<String> mFileNames ;
	public static final String regex = "/.";
	
	public RemovePosProcessor(List<String> fileNames) {
		mFileNames = fileNames;
	}
	
	@Override
	public void process() {
		for (int i = 0; i < mFileNames.size(); ++i) {
			removePosInFile(mFileNames.get(i));
		}
	}
	
	private void removePosInFile(String fileName) {
		ContentWriter writer = new FileWriter();
		ContentProvider provider = new FileProvider(fileName);
		provider.execute();
		String content = provider.getContent();
		
		content = content.replaceAll(regex, "");
		
		writer.write(content, fileName);
		
	}
}
