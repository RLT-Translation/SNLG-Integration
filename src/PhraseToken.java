import java.util.ArrayList;
import java.util.Map;

public class PhraseToken {
		
	private String val;
	private Map<String, TokenTag> map;
	private ArrayList<String> extraAttrs;
	private String originalStr;
	
	public PhraseToken(String token) {
		originalStr = token;
		String[] parts = token.split("/");
		this.val = parts[0];
		
		String strTokenType = parts[1];
		TokenTag tokenType = null;
		switch (strTokenType) {
			case "SUBJ":
				tokenType = TokenTag.SUBJ;
				break;
			case "OBJ":
				tokenType = TokenTag.OBJ;
				break;
			case "VERB":
				tokenType = TokenTag.VERB;
				break;
			case "PREP":
				tokenType = TokenTag.PREP;
				break;
			case"CONJ":
				tokenType = TokenTag.CONJ;
				break;
		}
		this.map.put("TokenType", tokenType);
		
		String strTenseType = parts[2];
		TokenTag tenseType = null;
		switch (strTenseType) {
			case "PAST":
				tenseType = TokenTag.PAST;
				break;
			case "PRESENT":
				tenseType = TokenTag.PRESENT;
				break;
			case "FUTURE":
				tenseType = TokenTag.FUTURE;
				break;
		}
		this.map.put("TenseType", tenseType);
		
		String strNumberType = parts[3];
		TokenTag numberType = null;
		switch (strNumberType) {
			case "SING":
				numberType = TokenTag.SING;
			case "PLURAL":
				numberType = TokenTag.PLURAL;
				break;
		}
		this.map.put("NumberType", numberType);
		
		for (int i = 4; i < parts.length; i++) {
			extraAttrs.add(parts[i]);
		}
		
	}
	
	public String toString() {
		return this.originalStr;
	}
}
