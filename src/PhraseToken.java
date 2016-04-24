import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.*;

public class PhraseToken {
		
	private PhraseType phraseType;
	private String originalToken;
	private String core;
	private Map<FeatureTag.FeatureGroup, FeatureTag> featureMap;
	private Map<POSTag, PhraseWord> phraseWordMap;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	
	
	public PhraseToken(PhraseType phraseType, String token) {
		this.featureMap = new HashMap<FeatureTag.FeatureGroup, FeatureTag>();
		this.originalToken = token;
		this.phraseType = phraseType;
		this.core = null;
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
		
		int colonPos = token.indexOf(':');
		String onlyFeatures = token.substring(colonPos+1);
		
		String[] features = onlyFeatures.split("/");
		// int compsLen = features.length;
		// System.out.println("Comps length: " + comps.length);
		
		String strTenseType = features[1];
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
		
		String strNumberType = features[2];
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
	
		String strVerbModType = features[3];
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
			this.featureMap.put(FeatureTag.FeatureGroup.VerbModType, verbModType);
		}
		
		
		String strWordComps = token.substring(0, colonPos);
		
		String plainWords = "";
		
		String[] wordComps = strWordComps.split(" ");
		for (int i = 0; i < wordComps.length; i++) {
			String curWord = wordComps[i];
			
			int semicolonPos = curWord.indexOf(';');
			String strPosType = curWord.substring(semicolonPos+1);
			POSTag posType = null;
			switch (strPosType) {
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
		
		String word = curWord.substring(0, semicolonPos);
		if (i < wordComps.length - 1) {
			plainWords += word + " ";
		}
		else {
			plainWords += word;
		}
		
		PhraseWord pw = new PhraseWord(word, posType);
		this.phraseWordMap.put(posType, pw);
		
		}
		this.core = plainWords;
	}
	
	public String toString() {
		return this.originalToken;
	}
	
	public void printSelf() {
		System.out.println("\t Phrase Type: " + this.phraseType);
		
		System.out.println("\t\t Core word: " + this.core);
		for (FeatureTag.FeatureGroup fg : this.featureMap.keySet()) {
			FeatureTag ft = this.featureMap.get(fg);
			System.out.println("\t\t\t FeatureGroupType: " + fg);
			System.out.println("\t\t\t FeatureVal: " + ft);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public PhraseElement getPhraseElement() {
		PhraseElement pe = null;
		switch (this.phraseType) {
			case NPPT:
				NPPhraseSpec np = nlgFactory.createNounPhrase();
				if (this.phraseWordMap.containsKey(POSTag.CONJ)) {
					np.setNoun(this.core);
				}
				else {
					np.setNoun(this.phraseWordMap.get(POSTag.NOUN));
				}
				if (this.featureMap.containsKey(FeatureTag.FeatureGroup.NumberType)) {
					NumberAgreement number = null;
					switch (this.featureMap.get(FeatureTag.FeatureGroup.NumberType)) {
						case PLURAL:
							number = NumberAgreement.PLURAL;
							break;
						case SING:
							number = NumberAgreement.SINGULAR;
							break;
					}
					np.setFeature(Feature.NUMBER, number);
				}
				pe = np;
				break;
			case VPPT:
				VPPhraseSpec vp = nlgFactory.createVerbPhrase();
				vp.setVerb(this.phraseWordMap.get(POSTag.VERB));
				if (this.featureMap.containsKey(FeatureTag.FeatureGroup.TenseType)) {
					Tense tense = null;
					switch (this.featureMap.get(FeatureTag.FeatureGroup.TenseType)) {
						case PRESENT:
							tense = Tense.PRESENT;
							break;
						case PAST:
							tense = Tense.PAST;
							break;
						case FUTURE:
							tense = Tense.FUTURE;
							break;
					}
					vp.setFeature(Feature.TENSE, tense);
				}
				if (this.featureMap.containsKey(FeatureTag.FeatureGroup.VerbModType)) {
					switch (this.featureMap.get(FeatureTag.FeatureGroup.VerbModType)) {
						case PROGRESSIVE:
							vp.setFeature(Feature.PROGRESSIVE, Boolean.TRUE);
							break;
						case PASSIVE:
							vp.setFeature(Feature.PASSIVE, Boolean.TRUE);
							break;
						case PERFECT:
							vp.setFeature(Feature.PASSIVE, Boolean.TRUE);
							break;
					}
				}
				pe = vp;
				break;
			case PPPT:
				PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
				NPPhraseSpec npart = nlgFactory.createNounPhrase();
				npart.setNoun(this.phraseWordMap.get(POSTag.NOUN));
				pp.addComplement(npart);
				pp.setPreposition(this.phraseWordMap.get(POSTag.PREP));
				pe = pp;
				break;
			case ADJPT:
				AdjPhraseSpec adjp = nlgFactory.createAdjectivePhrase();
				adjp.setAdjective(this.core);
				pe = adjp;
				break;
			case ADVPT:
				AdvPhraseSpec advp = nlgFactory.createAdverbPhrase();
				advp.setAdverb(this.core);
				pe = advp;
				break;
		}
		return pe;
	}
	
	
}
