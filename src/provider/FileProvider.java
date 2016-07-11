package provider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileProvider extends ContentProvider{
	
	private String mFileName;
		
	private final Object lock = new Object();
	private BackgroundThread mThread;
	
	public FileProvider(String fileName) {
		mFileName = fileName;
		super.mContent = null;
		mThread = new BackgroundThread();
	}
	
	public FileProvider() {
		mContent = null;
		mThread = new BackgroundThread();
	}
	
	public void setFileName(String fileName) {
		mFileName = fileName;
	}

	public synchronized void execute() {
		mThread.start();
	}
	
	@Override
	public synchronized String getContent() {
		synchronized(lock) {
			while(!mThread.isCompleted) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return super.getContent();
		}
	}
	
	/*
	 * background thread for get content from provider
	 */
	public  class BackgroundThread extends Thread {
		private boolean isCompleted = false;
		
		
		@Override
		public void run() {
			synchronized (lock) {
				Reader reader = null;
				BufferedReader fin = null;
				isCompleted = false;
				try {
					 reader = new InputStreamReader(
						         new FileInputStream(mFileName),"UTF-8");
					 fin = new BufferedReader(reader);
						   
				     String line;
				     StringBuilder buffer = new StringBuilder();
				 
					 while ((line=fin.readLine())!=null) {
						 buffer.append(line).append("\n"); 
					 }
					 
					 fin.close();
					 if (buffer.length() == 0) {
						 return;
					 }
					 mContent = buffer.toString();
					 isCompleted = true;
					 lock.notifyAll();
					 
				} catch (IOException e){
					e.printStackTrace();
					
				} finally {
					if (fin != null) {
						try {
							fin.close();
						}catch(IOException e) {
							e.printStackTrace();;
						}
					}
				}
			}
			
		}
		
	}

}
