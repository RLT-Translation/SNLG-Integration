import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import java.util.*;

public class Driver {
	
	public static int getGroupId(String inlet, int startIndex) {
		int openParenOccur = inlet.indexOf('(', startIndex);
		int closeParenOccur = inlet.indexOf(')', startIndex);
		String groupIdstr = inlet.substring(openParenOccur+1, closeParenOccur);
		return Integer.parseInt(groupIdstr);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		
		// Mix of Java and Python code to generate grammatical English sentences
		Map<Integer, ArrayList> map = new HashMap<Integer, ArrayList>();
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
				
				int groupId = getGroupId(input, openBrackIndex);
				
				PhraseGroup pg = new PhraseGroup(tokenType, groupId);
				
				String[] tokens = insideBracks.split(" ");
				for (int j = 0; j < tokens.length; j++) {
					if (tokens[j].charAt(0) == '[') {
						int innerGroupId = getGroupId(tokens[j], 0);
						pg.addComponentChild(innerGroupId);
					}
					else {
						pg.addComponentToken(tokens[i]);
					}
				}
				
				justSeenClose = false;

				i += (closeBraceOccur - openBraceOccur);
			}
		}


	}

}
