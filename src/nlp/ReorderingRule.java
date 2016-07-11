package nlp;

import java.util.HashMap;

public class ReorderingRule {
	private HashMap<String, Integer> mPosSequence;
	
	private float mFrequence;
	
	public ReorderingRule() {
		init();
	}
	
	private void init() {
		mPosSequence = new HashMap();
		mFrequence = 0;
	}
	
	public HashMap<String, Integer> getPosSequence() {
		return mPosSequence;
	}
	
	public void setFrequence(float frequence) {
		mFrequence = frequence;
	}
	
	public float getFrequence() {
		return mFrequence;
	}
	
	public void addPos(String pos, int position) {
		mPosSequence.put(pos, new Integer(position));
	}
	
	
	public boolean equals(ReorderingRule rule) {
		if (rule == null) {
			return false;
		}
		
		return rule.mPosSequence.keySet().equals(mPosSequence.keySet());
			
	} 
}
