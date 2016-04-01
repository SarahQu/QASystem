/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.*;
import java.io.*;
import java.util.*;
import opennlp.tools.sentdetect.*;
import opennlp.tools.util.InvalidFormatException;

/**
 *
 * @author Tejashree
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InvalidFormatException {
        // TODO code application logic here
        String path = "C:/Users/Tejashree/Desktop/Study/spring'16/NLP/projectNLP/data/set4/a1.txt";   //args[0];
        FileReader fileReader = new FileReader(path);

        BufferedReader br = new BufferedReader(fileReader);

        String s = null;
        File setupFile = new File("openNLPmodels/en-sent.bin");
        
	InputStream sentenceIs = new FileInputStream(setupFile.toString());
        SentenceModel sentenceModel = new SentenceModel(sentenceIs);
        
        String fname = path.split(".txt")[0]+"Separated.txt";
        FileWriter fileWr = new FileWriter(fname);
        BufferedWriter bw = new BufferedWriter(fileWr);
        
        SentenceDetectorME sdme = new SentenceDetectorME(sentenceModel);
        while((s=br.readLine())!=null)
        {
            String sent[] = sdme.sentDetect(s);
            for(int i = 0; i<sent.length; i++)
            {
                System.out.println(sent[i]+"\n");
                bw.write(sent[i]+"\n");
            }
        }
        
    }
    
}
