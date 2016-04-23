import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhraseToken {
		
	private PhraseType phraseType;
	private String originalToken;
	private Map<POSTag, Map<Integer, PhraseWord>> wordMap;
	
	
	public PhraseToken(PhraseType phraseType, String token) {
		this.originalToken = token;
		this.phraseType = phraseType;
		this.wordMap = new HashMap<POSTag, Map<Integer,PhraseWord>>();
		
		String[] parts = token.split("_");
		// System.out.println("PARTS INSIDE BRACKETS: " + parts.length);
		for (int i = 0; i < parts.length; i++) {
			String curWord = parts[i];
			
			// System.out.println("CURRENT WORD: " + curWord);
			int dotPos = curWord.indexOf(':');
			String isoWord = curWord.substring(0, dotPos);
			
			String strposType = curWord.substring(dotPos+1);
			
			POSTag posType = null;
			switch (strposType) {
				case "NOUN":
					posType = POSTag.NOUN;
					break;
				case "VERB":
					posType = POSTag.VERB;
					break;
				case "ADJ":
					posType = POSTag.ADJ;
					break;
				case "ADV":
					posType = POSTag.ADV;
					break;
				case "PREP":
					posType = POSTag.PREP;
					break;
				case "CONJ":
					posType = POSTag.CONJ;
					break;
			}
			// System.out.println("POSTTPE: " + posType);
			
			if (!(this.wordMap.containsKey(posType))) {
				this.wordMap.put(posType, new HashMap<Integer, PhraseWord>());
			}
			PhraseWord pw = new PhraseWord(posType, isoWord);
			int keynum = this.wordMap.get(posType).size();
			this.wordMap.get(posType).put(keynum, pw);
			
			// System.out.println("CORE WORD IN MAP: "  + pw.getCore());
			// System.out.println("WORD MAP SIZE: " + this.wordMap.size());
		}
	}
	
	public String toString() {
		return this.originalToken;
	}
	
	public void printSelf() {
		System.out.println("\t Phrase Type: " + this.phraseType);
		int counter = 0;
		
		for (POSTag pt : this.wordMap.keySet()) {
			Map<Integer, PhraseWord> innerMap = this.wordMap.get(pt);
			for (Integer index : innerMap.keySet()) {
				System.out.println("\t\t NUMBER WORD: " + index);
				PhraseWord pw = innerMap.get(index);
				pw.printSelf();
			}
		}
	}
	
	
}
