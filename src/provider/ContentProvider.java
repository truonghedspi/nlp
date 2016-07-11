package provider;

public abstract class ContentProvider {
	protected String mContent;
	

	/**
	 * get content from provider
	 * 
	 */
	public abstract void execute(); 
	
	public String getContent() {
		return mContent;
	}
	
}
