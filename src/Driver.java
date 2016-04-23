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
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		
		Map<PhraseFunc, PhraseGroup> map = new HashMap<PhraseFunc, PhraseGroup>();
		
		/*
		Stack<Integer> bracks = new Stack<Integer>();
		int nestCounter = 0;
		boolean justSeenClose = false;
		if (input.charAt(i) == '[') {
			nestCounter += 1;
			bracks.push(i);
		}
		else if (input.charAt(i) == ']') {
			nestCounter -= 1;
			justSeenClose = true;
		}
		*/
		
		String input = args[0];
		String[] groups = input.split("@");
		System.out.println("Input: " + input);
		System.out.println("Groups length: " + groups.length);
		
		for (int i = 0; i < groups.length; i++) {
			String curGroup = groups[i];
			System.out.println("Cur group: " + curGroup);
			
			int openBrackIndex = curGroup.indexOf('[');
			int closeBrackIndex = curGroup.indexOf(']');
			
			String insideBracks = curGroup.substring(openBrackIndex+1, closeBrackIndex);
			
			int openBraceOccur = curGroup.indexOf('{', closeBrackIndex);
			int closeBraceOccur = curGroup.indexOf('}', closeBrackIndex);
			String strPhraseFunc = curGroup.substring(openBraceOccur+1, closeBraceOccur);
			
			PhraseFunc phraseFunc = null;
			switch (strPhraseFunc) {
				case "S":
					phraseFunc = PhraseFunc.SUBJFUNC;
					break;
				case "O":
					phraseFunc = PhraseFunc.OBJFUNC;
					break;
				case "V":
					phraseFunc = PhraseFunc.VERBFUNC;
					break;
			}
			
			PhraseGroup pg = new PhraseGroup(phraseFunc, insideBracks);
			map.put(phraseFunc, pg);
		}
		
		for (PhraseFunc pf : map.keySet()) {
			PhraseGroup pg = map.get(pf);
			pg.printSelf();
		}
		
		/*
		SPhraseSpec sentence = nlgFactory.createClause();
		NPPhraseSpec subj = (NPPhraseSpec) map.get(PhraseFunc.SUBJFUNC).getPhrase();
		VPPhraseSpec verb = (VPPhraseSpec) map.get(PhraseFunc.VERBFUNC).getPhrase();
		NPPhraseSpec obj = (NPPhraseSpec) map.get(PhraseFunc.OBJFUNC).getPhrase();
		
		sentence.setSubject(subj);
		sentence.setVerb(verb);
		sentence.setObject(obj);
		
		String output = realiser.realiseSentence(sentence);
		System.out.println(output);
		*/

	}

}
