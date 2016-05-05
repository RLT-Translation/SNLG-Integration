import java.util.HashMap;
import java.util.Map;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.*;

public class PhraseWord {
	
	private POSTag posType;
	private String word;
	private Map<FeatureTag.FeatureGroup, FeatureTag> featureMap;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	
	public PhraseWord(String word, POSTag posType, String features) {
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
		this.word = word;
		this.posType = posType;
		this.featureMap = new HashMap<FeatureTag.FeatureGroup, FeatureTag>();
		
		
		String[] featureList = features.split("/");

		switch (posType) {
			case NOUN:
				String strNounType = featureList[0];
				FeatureTag nounType = null;
				switch(strNounType) {
					case "REG":
						nounType = FeatureTag.REG;
						break;
					case "PROP":
						nounType = FeatureTag.PROP;
						break;
					case "PRO":
						nounType = FeatureTag.PRO;
						break;
				}
				this.featureMap.put(FeatureTag.FeatureGroup.NounModType, nounType);
				
				String strNumType = featureList[1];
				FeatureTag numType = null;
				switch(strNumType) {
					case "SING":
						numType = FeatureTag.SING;
						break;
					case "PLURAL":
						numType = FeatureTag.PLURAL;
						break;
				}
				this.featureMap.put(FeatureTag.FeatureGroup.NumberType, numType);
				break;
			
			case VERB:
				String strVerbType = featureList[0];
				FeatureTag verbType = null;
				switch (strVerbType) {
					case "PLAIN":
						verbType = FeatureTag.PLAIN;
						break;
					case "PROGRESSIVE":
						verbType = FeatureTag.PROGRESSIVE;
						break;
				}
				this.featureMap.put(FeatureTag.FeatureGroup.VerbModType, verbType);
				
				String strTenseType = featureList[1];
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
				break;
				
			case ADJ:
			case ADV:
				String strAtype = featureList[0];
				FeatureTag aType = null;
				switch (strAtype) {
					case "UNMOD":
						aType = FeatureTag.UNMOD;
						break;
					case "COMP":
						aType = FeatureTag.COMP;
						break;
					case "SUP":
						aType = FeatureTag.SUP;
						break;
				}
				this.featureMap.put(FeatureTag.FeatureGroup.CompModType, aType);
				break;
			case DET:
			case CONJ:
			case PREP:
				break;
		}
		
	}
	
	
	public String getWord() {
		return this.word;
	}
	
	public POSTag getPOSType() {
		return this.posType;
	}
	
	@SuppressWarnings("incomplete-switch")
	public PhraseElement getPhraseElement() {
		
		PhraseElement pe = null;
		switch(this.posType) {
			case NOUN:
				NPPhraseSpec np = this.nlgFactory.createNounPhrase();
				np.setNoun(this.word);
				
				// hack
				np.setDeterminer("the");
				
				NumberAgreement number = null;
				switch(this.featureMap.get(FeatureTag.FeatureGroup.NumberType)) {
					case PLURAL:
						number = NumberAgreement.PLURAL;
						break;
					case SING:
						number = NumberAgreement.SINGULAR;
				}
				np.setFeature(Feature.NUMBER, number);
				pe = np;
				break;
			case VERB:
				VPPhraseSpec vp = this.nlgFactory.createVerbPhrase();
				vp.setVerb(this.word);
				
				Tense tense = null;
				switch(this.featureMap.get(FeatureTag.FeatureGroup.TenseType)) {
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
				
				switch(this.featureMap.get(FeatureTag.FeatureGroup.VerbModType)) {
					case PROGRESSIVE:
						vp.setFeature(Feature.PROGRESSIVE, Boolean.TRUE);
						break;
				}
				pe = vp;
				break;
				
			case ADJ:
				AdjPhraseSpec adp = this.nlgFactory.createAdjectivePhrase();
				adp.setAdjective(this.word);
				
				switch(this.featureMap.get(FeatureTag.FeatureGroup.CompModType)) {
					case COMP:
						adp.setFeature(Feature.IS_COMPARATIVE, Boolean.TRUE);
						break;
					case SUP:
						adp.setFeature(Feature.IS_SUPERLATIVE, Boolean.TRUE);
						break;
				}
				pe = adp;
				break;
			case ADV:
				AdvPhraseSpec avp = this.nlgFactory.createAdverbPhrase();
				avp.setAdverb(this.word);
				
				switch(this.featureMap.get(FeatureTag.FeatureGroup.CompModType)) {
					case COMP:
						avp.setFeature(Feature.IS_COMPARATIVE, Boolean.TRUE);
						break;
					case SUP:
						avp.setFeature(Feature.IS_SUPERLATIVE, Boolean.TRUE);
						break;
				}
				pe = avp;
				break;
			case PREP:
				PPPhraseSpec pp = this.nlgFactory.createPrepositionPhrase();
				pp.setPreposition(this.word);
				pe = pp;
				break;
		}
		
		return pe;
	}
	
	public void printSelf() {
		System.out.println("\t\tWord type: " + this.posType);
		System.out.println("\t\tWord: " + this.word);
		
		for (FeatureTag.FeatureGroup fg : this.featureMap.keySet()) {
			FeatureTag ft = this.featureMap.get(fg);
			System.out.println("\t\t\tFeature group type: " + fg);
			System.out.println("\t\t\t\tFeature value: " + ft);
		}
	}
	
}
