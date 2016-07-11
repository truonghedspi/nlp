package writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class FileWriter extends ContentWriter{
	
	public FileWriter() {
		
	}
		
	@Override
	public void write(String content, String fileName) {
		new WriteFileBackgroundThread(content, fileName).start();
	}
	
	public  class WriteFileBackgroundThread extends Thread {
		
		private String mContent;
		
		private String mFileName;
		
		public WriteFileBackgroundThread(String content) {
			mContent = content;
		}
		
		public WriteFileBackgroundThread(String content, String fileName) {
			mContent = content;
			mFileName = fileName;
		}
		
		@Override
		public void run() {
			Writer out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(
					    new FileOutputStream(this.mFileName), "UTF-8"));
				out.write(mContent);
				
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		


		}
		
		
		
	
	}

}
