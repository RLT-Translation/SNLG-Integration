import java.util.*;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;


public class PhraseGroup {

	private PhraseFunc phraseFunc;
	private String originalStr;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Map<PhraseType, Map<Integer, PhraseToken>> tokenMap;
	
	public PhraseGroup(PhraseFunc phraseFunc, String originalStr) {
		this.phraseFunc = phraseFunc;
		this.originalStr = originalStr;
		this.tokenMap = new HashMap<PhraseType, Map<Integer, PhraseToken>>();
		
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
		
		String[] tokens = this.originalStr.split(",");
		for (int j = 0; j < tokens.length; j++) {
			String curTok = tokens[j];
			int openCarrot = curTok.indexOf('<');
			int closeCarrot = curTok.indexOf('>');
			String insideCarrots = curTok.substring(openCarrot+1, closeCarrot);
			
			int openBrace = curTok.indexOf('{');
			int closeBrace = curTok.indexOf('}');
			String strphraseType = curTok.substring(openBrace+1, closeBrace);
			
			PhraseType phraseType = null;
			switch (strphraseType) {
				case "NPPT":
					phraseType = PhraseType.NPPT;
					break;
				case "VPPT":
					phraseType = PhraseType.VPPT;
					break;
				case "PPPT":
					phraseType = PhraseType.PPPT;
					break;
				case "ADVPT":
					phraseType = PhraseType.ADVPT;
					break;
				case"ADJPT":
					phraseType = PhraseType.ADJPT;
					break;

			}
			
			if (!(this.tokenMap.containsKey(phraseType))) {
				this.tokenMap.put(phraseType, new HashMap<Integer, PhraseToken>());
			}
			PhraseToken pt = new PhraseToken(phraseType, insideCarrots);
			int keynum = this.tokenMap.get(phraseType).size();
			this.tokenMap.get(phraseType).put(keynum, pt);
		}
	}
	
	public NPPhraseSpec createSubject() {
		NPPhraseSpec np = nlgFactory.createNounPhrase();
		return np;
	}
	
	public VPPhraseSpec createVerb() {
		VPPhraseSpec vp = nlgFactory.createVerbPhrase();
		return vp;
	}
	
	public NPPhraseSpec createObject() {
		NPPhraseSpec np = nlgFactory.createNounPhrase();
		return np;
	}
	
	public void printPhraseGroup() {
		System.out.println("Phrase Function: " + this.phraseFunc);
		System.out.println("Original string: " + this.originalStr);
	}
	
	public PhraseElement getPhrase() {
		if (this.phraseFunc == PhraseFunc.SUBJFUNC) {
			return this.createSubject();
		}
		else if (this.phraseFunc == PhraseFunc.VERBFUNC) {
			return this.createVerb();
		}
		else {
			return this.createObject();
		}
	}
	
	public void printSelf() {
		System.out.println("Phrase group function: " + this.phraseFunc);
		for (PhraseType pt : this.tokenMap.keySet()) {
			Map<Integer, PhraseToken> innerMap = this.tokenMap.get(pt);
			for (Integer index : innerMap.keySet()) {
				System.out.println("\t Number Token: " + index);
				PhraseToken phraseTok = innerMap.get(index);
				phraseTok.printSelf();
			}
		}
	}
	
}
