import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import java.util.*;

public class Driver {
	
	public static String getGroupIdstr(String inlet, int startIndex) {
		int openParenOccur = inlet.indexOf('(', startIndex);
		int closeParenOccur = inlet.indexOf(')', startIndex);
		String groupIdstr = inlet.substring(openParenOccur+1, closeParenOccur);
		return groupIdstr;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		
		// Mix of Java and Python code to generate grammatical English sentences
		Map<Integer, PhraseGroup> map = new HashMap<Integer, PhraseGroup>();
		Stack<Integer> bracks = new Stack<Integer>();
		
		int nestCounter = 0;
		boolean justSeenClose = false;
		String input = args[0];
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '[') {
				nestCounter += 1;
				bracks.push(i);
			}
			else if (input.charAt(i) == ']') {
				nestCounter -= 1;
				justSeenClose = true;
			}
			
			if (justSeenClose) {
				int openBraceOccur = input.indexOf('{', i);
				int closeBraceOccur = input.indexOf('}', i);
				String tokenType = input.substring(openBraceOccur+1, closeBraceOccur);
				
				int openBrackIndex = bracks.pop();
				String insideBracks = input.substring(openBrackIndex, i-1);
				
				String groupIdstr = getGroupIdstr(input, openBrackIndex);
				int groupId = Integer.parseInt(groupIdstr);
				
				PhraseGroup pg = new PhraseGroup(tokenType, groupId);
				
				String[] tokens = insideBracks.split(" ");
				for (int j = 0; j < tokens.length; j++) {
					if (tokens[j].charAt(0) == '[') {
						int innerGroupId = Integer.parseInt(getGroupIdstr(tokens[j], 0));
						pg.addComponentChild(innerGroupId);
					}
					else {
						pg.addComponentToken(tokens[i]);
					}
				}
				map.put(groupId, pg);
				
				// Replace contents of brackets with [(groupId)]
				String firstPart = input.substring(openBrackIndex, openBrackIndex+3+groupIdstr.length());
				String secondPart = input.substring(i, input.length());
				input = firstPart + secondPart;
				
				justSeenClose = false;

				i += (closeBraceOccur - openBraceOccur);
			}
		}


	}

}
