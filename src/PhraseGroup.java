import java.util.*;

public class PhraseGroup {

	private String phraseType;
	private ArrayList subcomponents;
	private int curListIndex;
	private int groupId;
	
	public PhraseGroup(String phraseType, int groupId) {
		this.phraseType = phraseType;
		this.curListIndex = 0;
		this.groupId = groupId;
	}
	
	public void addComponentChild(int childId) {
		this.subcomponents.add(this.curListIndex, childId);
		this.curListIndex += 1;
	}
	
	public void addComponentToken(String token) {
		this.subcomponents.add(this.curListIndex, token);
		this.curListIndex += 1;
	}
	
}
