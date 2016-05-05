import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.*;

public class PhraseToken {
		
	private PhraseType phraseType;
	private String originalPhrase;
	private Map<Integer, PhraseWord> wordMap;
	private String justWords;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	
	
	public PhraseToken(PhraseType phraseType, String phrase) {
		this.originalPhrase = phrase;
		this.phraseType = phraseType;
		this.wordMap = new HashMap<Integer, PhraseWord>();
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
		
		String justWords = "";
		String[] tokens = phrase.split("_");
		
		for (int i = 0; i < tokens.length; i++) {
			String cur = tokens[i];
			
			int colonPos = cur.indexOf(':');
			String word = cur.substring(0, colonPos);
			int semiPos = cur.indexOf(';');
			String strPos = cur.substring(colonPos+1, semiPos);
			
			
			POSTag posType = null;
			switch(strPos) {
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
				case "CONJ":
					posType = POSTag.CONJ;
					break;
				case "DET":
					posType = POSTag.DET;
					break;
				case "PREP":
					posType = POSTag.PREP;
					break;
			}
			
			String features = cur.substring(semiPos);
			
			PhraseWord pw = new PhraseWord(word, posType, features);
			this.wordMap.put(i, pw);
			
			if (i < tokens.length - 1) {
				justWords += word + " ";
			}
			else {
				justWords += word;
			}
			
		}
		
		this.justWords = justWords;
	}
	
	public PhraseType getPhraseType() {
		return this.phraseType;
	}
	
	public void printSelf() {
		System.out.println("\t Phrase Type: " + this.phraseType);
		
		System.out.println("\t Phrase contents: " + this.justWords);
		for (Integer i : this.wordMap.keySet()) {
			PhraseWord pw = this.wordMap.get(i);
			pw.printSelf();
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public PhraseElement getPhraseElement() {
		PhraseElement pe = null;
		
		switch(this.phraseType) {
			case NPPT:
				NPPhraseSpec np = this.nlgFactory.createNounPhrase();
				np.setNoun(this.justWords);
				pe = np;
				break;
			case PPPT:
				PPPhraseSpec pp = this.nlgFactory.createPrepositionPhrase(this.justWords);
				pe = pp;
				break;
		}
		
		return pe;
	}
	
	
}
