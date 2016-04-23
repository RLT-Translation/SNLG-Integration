import java.util.HashMap;
import java.util.Map;

public class PhraseWord {
	
	private Map<FeatureTag.FeatureGroup, FeatureTag> featureMap;
	private String originalWord;
	private POSTag posType;
	private String core;
	
	public PhraseWord(POSTag posType, String word) {
		this.posType = posType;
		this.originalWord = word;
		this.featureMap = new HashMap<FeatureTag.FeatureGroup, FeatureTag>();
		
		String[] comps = word.split("/");
		int compsLen = comps.length;
		// System.out.println("Comps length: " + comps.length);
		
		// The actual word
		this.core = comps[0];
		// System.out.println("CORE: " + this.core);
		
		String strTenseType = comps[1];
		// System.out.println("TENSE: " + strTenseType);
		if (!(strTenseType.equals("X"))) {
			FeatureTag tenseType = null;
			switch (strTenseType) {
				case "PAST":
					tenseType = FeatureTag.PAST;
					break;
				case "PRESENT":
					tenseType = FeatureTag.PRESENT;
					break;
				case "FUTURE":
					tenseType = FeatureTag.FUTURE;
					break;
			}
			this.featureMap.put(FeatureTag.FeatureGroup.TenseType, tenseType);
		}
		
		String strNumberType = comps[2];
		// System.out.println("NUMBER: " + strNumberType);
		if (!(strNumberType.equals("X"))) {
			FeatureTag numberType = null;
			switch (strNumberType) {
				case "SING":
					numberType = FeatureTag.SING;
					break;
				case "PLURAL":
					numberType = FeatureTag.PLURAL;
					break;
			}
			
			this.featureMap.put(FeatureTag.FeatureGroup.NumberType, numberType);
		}
	
		String strVerbModType = comps[3];
		// System.out.println("VERB MOD: " + strVerbModType);
		if (!(strVerbModType.equals("X"))) {
			FeatureTag verbModType = null;
			switch (strVerbModType) {
				case "PASSIVE":
					verbModType = FeatureTag.PASSIVE;
					break;
				case "PROGRESSIVE":
					verbModType = FeatureTag.PROGRESSIVE;
					break;
				case "PERFECT":
					verbModType = FeatureTag.PERFECT;
					break;
			}
			this.featureMap.put(FeatureTag.FeatureGroup.TenseType, verbModType);
		}
		
	}
	
	public void printSelf() {
		System.out.println("\t\t\t POS Type: " + this.posType);
		System.out.println("\t\t\t Core word: " + this.core);
		for (FeatureTag.FeatureGroup fg : this.featureMap.keySet()) {
			FeatureTag ft = this.featureMap.get(fg);
			System.out.println("\t\t\t\t FeatureGroupType: " + fg);
			System.out.println("\t\t\t\t FeatureVal: " + ft);
		}
	}
	
	public String getCore() {
		return this.core;
	}

}
