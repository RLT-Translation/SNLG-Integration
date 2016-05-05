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
	private Map<Integer, PhraseToken> tokenMap;
	private Map<Integer, PhraseWord> wordMap;
	private int numPhrases;
	
	
	public PhraseGroup(PhraseFunc phraseFunc, String originalStr) {
		this.phraseFunc = phraseFunc;
		this.originalStr = originalStr;
		this.tokenMap = new HashMap<Integer, PhraseToken>();
		this.wordMap = new HashMap<Integer, PhraseWord>();
		this.numPhrases = 0;
	
		
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
				
		// Modified string to be devoid of phrases
		String modStr = originalStr;
		
		// First look for any phrases
		int startingCarrot = originalStr.indexOf('<');
		int endingCarrot = originalStr.indexOf('>');
		int numdec = 0;
		
		while (startingCarrot >= 0 && endingCarrot >= 0) {
			this.numPhrases += 1;
			
			int vertIndex = originalStr.indexOf('|', startingCarrot);
			String phraseIndexStr = originalStr.substring(startingCarrot+1, vertIndex);
			int phraseIndex = Integer.parseInt(phraseIndexStr);
			
			// Plus 2 for the carrot and vertical bar
			String insideCarrots = originalStr.substring(startingCarrot+2+phraseIndexStr.length(), endingCarrot);
			
			numdec += countUnderscores(insideCarrots);
			
			int openBrace = originalStr.indexOf('{', endingCarrot);
			int closeBrace = originalStr.indexOf('}', endingCarrot);
			String strPhraseType = originalStr.substring(openBrace+1, closeBrace);
			
			PhraseType phraseType = null;
			switch(strPhraseType) {
				case "NPPT":
					phraseType = PhraseType.NPPT;
					break;
				case "PPPT":
					phraseType = PhraseType.PPPT;
					break;
			}
			
			PhraseToken pt = new PhraseToken(phraseType, insideCarrots);
			this.tokenMap.put(phraseIndex-numdec, pt);
			
			modStr = modStr.substring(0, startingCarrot) + "=" + modStr.substring(closeBrace+2);
			
			startingCarrot = modStr.indexOf('<');
			endingCarrot = modStr.indexOf('>');
		}
		
		
		// Deal with everything else
		// inside brackets has already been removed
		String[] tokens = modStr.split("_");
		for (int j = 0; j < tokens.length; j++) {
			String cur = tokens[j];
			
			if (cur.equals("=")) {
				continue;
			}
			
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
			this.wordMap.put(j, pw);
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
	
	@SuppressWarnings("incomplete-switch")
	public PhraseElement getPhraseSpec() {
		PhraseElement pe = null;
		
		int elemCount = this.tokenMap.size() + this.wordMap.size();
		
		// TODO: complement
		// Switch on the function of this phrase group
		switch (this.phraseFunc) {
			case SUBJFUNC:
			case OBJFUNC:
			case INDOBJFUNC:
				NPPhraseSpec np = nlgFactory.createNounPhrase();
				
				// TODO: deal with finding prepositions
				// Maybe add pre and post modifiers instead of complements
				boolean isNounSet = false;
				for (int i = 0; i < elemCount; i++) {
					if (this.tokenMap.containsKey(i)) {
						PhraseToken pt = this.tokenMap.get(i);
						switch(pt.getPhraseType()) {
							case NPPT:
								NPPhraseSpec npptPiece = (NPPhraseSpec) pt.getPhraseElement();
								if (!isNounSet) {
									np.setNoun(npptPiece);
									isNounSet = true;
								}
								else {
									np.addComplement(npptPiece);
								}
								break;
							case PPPT:
								PPPhraseSpec ppptPiece = (PPPhraseSpec) pt.getPhraseElement();
								np.addComplement(ppptPiece);
								break;
						}
					}
					else if (this.wordMap.containsKey(i)) {
						PhraseWord pw = this.wordMap.get(i);
						switch (pw.getPOSType()) {
							case NOUN:
								NPPhraseSpec npPiece = (NPPhraseSpec) pw.getPhraseElement();
								if (!isNounSet) {
									np.setNoun(npPiece);
									isNounSet = true;
								}
								else {
									np.addComplement(npPiece);
								}
								break;
							case ADJ:
								AdjPhraseSpec adPiece = (AdjPhraseSpec) pw.getPhraseElement();
								np.addModifier(adPiece);
								break;
						}
					}
				}
				pe = np;
				break;
			case VERBFUNC:
				
				VPPhraseSpec vp = nlgFactory.createVerbPhrase();
				boolean isVerbSet = false;
				for (int i = 0; i < elemCount; i++) {
					if (this.tokenMap.containsKey(i)) {
						continue; // nothing to do here
					}
					else if (this.wordMap.containsKey(i)) {
						PhraseWord pw = this.wordMap.get(i);
						switch (pw.getPOSType()) {
							case VERB:
								VPPhraseSpec vpPiece = (VPPhraseSpec) pw.getPhraseElement();
								if (!isVerbSet) {
									vp.setVerb(vpPiece);
									isVerbSet = true;
								}
								else {
									vp.addComplement(vpPiece);
								}
							break;
						case ADV:
							AdvPhraseSpec avPiece = (AdvPhraseSpec) pw.getPhraseElement();
							vp.addModifier(avPiece);
							break;
						}
					}
				}
				pe = vp;
				break;
		}
		
		return pe;
	}
	
	
	public PhraseFunc getPhraseFunc() {
		return this.phraseFunc;
	}
	public void printSelf() {
		System.out.println("Phrase group function: " + this.phraseFunc);
		int elemCount = this.tokenMap.size() + this.wordMap.size();
		
		for (int i = 0; i < elemCount; i++) {
			if (this.tokenMap.containsKey(i)) {
				PhraseToken phraseTok = this.tokenMap.get(i);
				phraseTok.printSelf();
			}
			else if (this.wordMap.containsKey(i)) {
				PhraseWord pw = this.wordMap.get(i);
				pw.printSelf();
			}
			
		}
	}
	
	private int countUnderscores(String input) {
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '_') {
				count += 1;
			}
		}
		return count;
	}
	
}
