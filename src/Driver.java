import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import java.util.*;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		
		// Mix of Java and Python code to generate grammatical English sentences
		Map<Integer, String[]> map = new HashMap<Integer, String[]>();
		
		for (int i = 0; i < args.length; i++) {
			String[] tokens = args[i].split("/", 2);
			map.put(i, tokens);
		}


	}

}
