import java.io.StringReader;
import java.util.*;
import java.io.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.*;
import edu.stanford.nlp.trees.GrammaticalRelation;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.trees.*;
// http://stackoverflow.com/questions/19429106/how-can-i-integrate-stanford-parser-software-in-my-java-program
class DependSentParser {

    
    private static class WordPos implements Comparable<WordPos> {
        private int indexPos;
        private String word;
     
        WordPos(String w, int ind)
        {
            indexPos = ind;
            word = w;
        }
        public int compareTo(WordPos inst)
		{
			if(this.indexPos<inst.indexPos)
				return -1;
			else if(this.indexPos>inst.indexPos)
				return 1;
			return 0;
		}
        
        public boolean equals(WordPos inst)
        {
            if(this.indexPos == inst.indexPos && this.word.equals(inst.word))
                return true;
            return false;
        }
    }
    
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

    public static void main(String[] args) throws IOException { 
        
        String str = "Dempsey was born in Nacogdoches, Texas, and, for much of his childhood, his family lived in a trailer park, where he and his siblings grew up playing soccer with Hispanic immigrants.";
        
        String str1 = "What is the name of the founder who ditched her boyfriend?";
        
        //String str = "Clinton Drew Dempsey (born March 9, 1983) is an American professional soccer player who plays for Seattle Sounders FC in Major League Soccer and has served as the captain of the United States national team.";
        QuestParser parser = new QuestParser(); 
        Tree tree = parser.parse(str);  
        //tree.pennPrint();
        List<String> nps = new ArrayList<>();
        nps = getNounPhrases(tree);
        for(int i = 0; i< nps.size(); i++){
            //System.out.println(nps.get(i));
            //bw.write(nps.get(i)+"\n");
        }
        
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        //System.out.println(tdl);
        createDepend(tdl);
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        //tp.printTree(tree);
        List<Tree> leaves = tree.getLeaves();
        // Print words and Pos Tags
        for (Tree leaf : leaves) { 
            Tree parent = leaf.parent(tree);
            //System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
        }
        System.out.println();
        
    }
    
    //http://stackoverflow.com/questions/7914433/finding-noun-phrases-using-through-stanford-parser
    //http://stackoverflow.com/questions/12514621/extracting-the-text-from-output-parse-tree
    private static List<String> getNounPhrases(Tree parse) {
        List<String> result = new ArrayList<>();
        TregexPattern pattern = TregexPattern.compile("@NP..@VP");
        TregexMatcher matcher = pattern.matcher(parse);
        while(matcher.findNextMatchingNode())
        {
            Tree match = matcher.getMatch();
            String np = Sentence.listToString(match.yield());
            result.add(np);
        }
        return result;
    }
    
    private static void createDepend(List<TypedDependency> tdl)
    {
        Map<String,Integer> subj_dep = new HashMap<>();
        for(int i = 0; i<tdl.size(); i++)
        {
            TypedDependency td_unit = tdl.get(i);
            String td_gramrel = td_unit.reln().getShortName();
            if(td_gramrel.contains("nsubj"))
            {
                String gov = td_unit.gov().originalText();
                //String g_ner = td_unit.gov().ner();
                System.out.println(gov);
                String dep = td_unit.dep().originalText();
                //String dep_ner = td_unit.dep().ner();
                System.out.println(dep);
                PriorityQueue<WordPos> result = new PriorityQueue<>();
                WordPos wp = new WordPos(gov,td_unit.gov().index());
                result.add(wp);
                findDependency(gov,tdl,result);
                findGovernor(dep,tdl,result);
                while (result.size() > 0) {
                    WordPos wpp = result.poll();
                    System.out.print(wpp.word+ " " + wpp.indexPos);
                }
                System.out.println();
                if(subj_dep.get(dep)==null)
                    subj_dep.put(dep, 1);
                else
                    subj_dep.put(dep, subj_dep.get(dep)+1);
            }
            //System.out.println(td_gramrel);
        }
    }
    
    public static void findDependency(String govWord, List<TypedDependency> tdl, PriorityQueue<WordPos> result) {
        
        for (TypedDependency td: tdl) {
            
            if (td.gov().word()!=null && td.gov().word().equals(govWord)) {
                WordPos wp = new WordPos(td.dep().word(),td.dep().index());
                result.add(wp);
                if(!result.contains(wp))
                    findDependency(td.dep().word(),tdl,result);
            }
        }
    }
    
    public static void findGovernor(String depWord, List<TypedDependency> tdl, PriorityQueue<WordPos> result) {
        
        for (TypedDependency td: tdl) {
            String td_gramrel = td.reln().getShortName();
            if(td_gramrel.contains("nsubj")) //&& td.dep().word().equals(depWord))
                continue;
            if (td.dep().word()!=null && td.dep().word().equals(depWord)) {
                WordPos wp = new WordPos(td.gov().word(),td.gov().index());
                result.add(wp);
                if(!result.contains(wp))
                    findDependency(td.gov().word(),tdl,result);
            }
        }
    }
}