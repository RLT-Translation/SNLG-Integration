import java.util.HashMap;
import java.util.Map;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.*;

public class PhraseWord {
	
	private POSTag posType;
	private String core;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	
	public PhraseWord(String word, POSTag posType) {
		this.lexicon = Lexicon.getDefaultLexicon();
		this.nlgFactory = new NLGFactory(lexicon);
		this.core = word;
		this.posType = posType;
	}
	
	public void printSelf() {
		
	}
	
	public String getCore() {
		return this.core;
	}
	
	public String toString() {
		return this.core;
	}

}
