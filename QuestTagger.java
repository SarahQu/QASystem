/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.*;
import java.io.*;
import java.util.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.*;
/**
 *
 * @author Tejashree
 */
public class QuestTagger {
    
    public static void main(String args[]) throws IOException
    {
        MaxentTagger tagger = new MaxentTagger("taggers/english-bidirectional-distsim.tagger");
        String quest = "What is the name of the author?";
        String tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Whose dog did Ryan kidnap?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Who was Stella talking to?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Where is the dog?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "When did the dog return?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Which country was no participating in the game?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "When was he born?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "How did ack manage to escape?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Whom are you going to invite?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Why was Ryan mad?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Which president modified the legislation?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        quest = "Are you crazy?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
        String sent = "Dempsey was born in Nacogdoches, Texas, and, for much of his childhood, his family lived in a trailer park, where he and his siblings grew up playing soccer with Hispanic immigrants.";
        tagged = tagger.tagString(sent);
        System.out.println(tagged);
        
        quest = "Who sells the most greeting cards?";
        tagged = tagger.tagString(quest);
        System.out.println(tagged);
        
    }
    
}
