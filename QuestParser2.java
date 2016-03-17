import java.io.StringReader;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.*;
// http://stackoverflow.com/questions/19429106/how-can-i-integrate-stanford-parser-software-in-my-java-program
class QuestParser {

    private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";        

    private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);

    public Tree parse(String str) {                
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }

    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
            tokenizerFactory.getTokenizer(
                new StringReader(str));    
        return tokenizer.tokenize();
    }

    public static void main(String[] args) { 
        String str1 = "What is the name of the founder who ditched her boyfriend?";
        String str = "He has also played for New England Revolution, Fulham and Tottenham Hotspur.";
        QuestParser parser = new QuestParser(); 
        Tree tree = parser.parse(str);  
        tree.pennPrint();
        List<String> nps = new ArrayList<>();
        nps = getNounPhrases(tree);
        for(int i = 0; i< nps.size(); i++)
            System.out.println(nps.get(i));
        
        List<Tree> leaves = tree.getLeaves();
        // Print words and Pos Tags
        for (Tree leaf : leaves) { 
            Tree parent = leaf.parent(tree);
            System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
        }
        System.out.println();               
    }
    
    //http://stackoverflow.com/questions/7914433/finding-noun-phrases-using-through-stanford-parser
    //http://stackoverflow.com/questions/12514621/extracting-the-text-from-output-parse-tree
  private static List<String> getNounPhrases(Tree parse) {
    List<String> result = new ArrayList<>();
    TregexPattern pattern = TregexPattern.compile("@VP");
    TregexMatcher matcher = pattern.matcher(parse);
    while(matcher.findNextMatchingNode())
    {
        Tree match = matcher.getMatch();
        String np = Sentence.listToString(match.yield());
        result.add(np);
    }
    return result;
}
}