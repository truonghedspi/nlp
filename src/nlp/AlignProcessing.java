package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import provider.ContentProvider;
import provider.FileProvider;

public class AlignProcessing {
	private String mPosSourceFileName;
	
	private List<ReorderingRule> rules;
	
	/*
	 * Doc moi dong trong aligned
	 *	doc moi dong trong pos file
	 *  dua ra luat va dem so lan
	 *  Luat co dang : dayPOS|Thu tu|so lan
	 */ 
	public AlignProcessing() {
		init();
	}
	
	public void init() {
		rules = new ArrayList<ReorderingRule>();
	}
	
	public void readFile(String alignedFile, String posFile) {
		ContentProvider providerAligned = new FileProvider(alignedFile);
		ContentProvider providerPos = new FileProvider(posFile);
		
		providerAligned.execute();
		providerPos.execute();
		
		String[] alignedString = providerAligned.getContent().split("\n");
		String[] posString = providerPos.getContent().split("\n");
		
		for (String string : posString) {
			System.out.println(string);
		}
		
		for (String string : alignedString) {
			System.out.println(string);
		}
		
	}
	
	private void getRulesWithAlignedSequence(Map <Integer, Integer> alignedMap, List<String> posSequence) {
		Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>(alignedMap) ;
		ReorderingRule rule = new ReorderingRule();
		
		for (Integer key: treeMap.keySet()) {
			rule.addPos(posSequence.get(key.intValue()), treeMap.get(key).intValue());
		}
		
		if (isRuleExisted(rule) == false) {
			rules.add(rule);
		}
	}
	
	
	private boolean isRuleExisted(ReorderingRule rule) {
		for (ReorderingRule ruleElement : rules) {
			if (ruleElement.equals(rule)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void posRuleExtracting()  {
		String[] posSequence = null;
		String[] aliSed = null;
		
		/*
		 * Load cau tu tagged aligned corpus
		 * Luu moi cau vao S
		 * voi moi cau s(i) thuoc S
		 * 		voi moi chunk c(i) thuoc C trong S
		 * 			voi moi tu w(i) thuoc W trong c(i) i = 1...k
		 * 				neu (k > 1) {
		 *					trich xuat pos(i) cho tu w(i)	
		 * 					posSeq += pos(i)
		 * 					trich xuat vi tri giong hang a(i) cho tu w(i)
		 * 					aliSeq += a(i)
		 * 
		 * 				{
		 * 			rule = posSeq + "#" + aliSeq
		 * 		
		 *		write rule 			
		 */
		
	}
	
	public void alignmentExtracting(List<Integer> alignmentPostionArray){
		/*
		 * foreach (Integer ap: in aligmentPostionArray) {
		 * 		if (ap(i) == ap(i-1)) {
		 * 			a(i-1) = i-1 + i + "\" + api
		 * 
		 * 		} else {
		 * 			a(i) = i + \ + ap(i)
		 * 
		 * 		}
		 * }
		 * 
		 * foreach (Integer a(i): in A) {
		 * 		if (a(i) != null) {
		 * 			rule = rule + a(i)	
		 * 		}
		 * 
		 * }
		 * 
		 */
	}
	
	
}
