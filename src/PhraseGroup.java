import java.util.*;

public class PhraseGroup {
	
	public enum PhraseType {
	    NP, VP, PP
	}

	private PhraseType phraseType;
	private ArrayList subcomponents;
	private int curListIndex;
	private int groupId;
	
	public PhraseGroup(String phraseType, int groupId) {
		switch (phraseType) {
		case "NP":
			this.phraseType = PhraseType.NP;
			break;
		case "VP":
			this.phraseType = PhraseType.VP;
			break;
		case "PP":
			this.phraseType = PhraseType.PP;
		}
		this.curListIndex = 0;
		this.groupId = groupId;
	}
	
	public void addComponentChild(int childId) {
		this.subcomponents.add(this.curListIndex, childId);
		this.curListIndex += 1;
	}
	
	public void addComponentToken(String token) {
		PhraseToken pt = new PhraseToken(token);
		this.subcomponents.add(this.curListIndex, token);
		this.curListIndex += 1;
	}
	
	public void printPhraseGroup() {
		System.out.println("Phrase Type: " + this.phraseType);
		System.out.println("GroupID: " + groupId);
		for (int i = 0; i < this.subcomponents.size(); i++) {
			if (this.subcomponents.get(i) instanceof Integer) {
				System.out.println(this.subcomponents.get(i) + ", ");
			}
			else {
				System.out.println(this.subcomponents.get(i).toString() + ", ");
			}
		}
	}
	
}
