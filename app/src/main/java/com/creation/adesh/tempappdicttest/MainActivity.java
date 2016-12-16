package com.creation.adesh.tempappdicttest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.inputmethod.latin.dicttool.Dicttool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    String dictFileName = "hinglish.dict";
    String newDictFileName = "hinglish_new.dict";
    String generatedXmlName = "hinglish_generated.xml";
    String transformedXmlName = "hinglish_transformed.xml";
    String dictPath = baseDir + File.separator + dictFileName;
    String newDictPath = baseDir + File.separator +newDictFileName;
    String convertedXmlPath = baseDir + File.separator + generatedXmlName;
    String transformedXmlPath = baseDir + File.separator + transformedXmlName;
    String [] initialArguments = {"makedict","-s",dictPath, "-x", convertedXmlPath};
    String[] finalArguments = {"makedict","-s",transformedXmlPath, "-d", newDictPath};

    public void generateXML(View view){
        Dicttool.main(initialArguments);
        Toast.makeText(this, "XML generated!", Toast.LENGTH_SHORT).show();
    }
    public void transformXML(View view){
        transformFormat(convertedXmlPath, transformedXmlPath);
        Toast.makeText(this, "XML transformed!", Toast.LENGTH_SHORT).show();
    }
    public void writeDict(View view){
        Dicttool.main(finalArguments);
        Toast.makeText(this, "Updated dictionary!", Toast.LENGTH_SHORT).show();
    }
    public static void transformFormat(String inPath, String outPath){

        BufferedReader br = null;
        String sCurrentLine;
        BufferedWriter bw = null;
        try {

            br = new BufferedReader(new FileReader(inPath));
            bw = new BufferedWriter(new FileWriter(outPath));
            bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<wordlist>\n");
            br.readLine();
            br.readLine();
            int i=0;
            while ((sCurrentLine = br.readLine()) != null) {

                if(sCurrentLine.contains("</wordlist>"))
                    break;
                String wordElement = sCurrentLine.split("f=")[0];
                int startWord= wordElement.indexOf('"');
                int endWord= wordElement.lastIndexOf('"');
                String word = wordElement.substring(startWord+1,endWord);
                String fElement = sCurrentLine.split("f=")[1];
                int startF= fElement.indexOf('"');
                int endF= fElement.lastIndexOf('"');
                String f =fElement.substring(startF+1,endF);
                i++;
                if(i==10)
                {
                    f = "250"; word="dabba";
                }
                String outputLine = "    <w f=\""+f+"\">"+word+"</w>";
                bw.write(outputLine+'\n');
            }
            bw.write("</wordlist>\n");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();
                if(bw != null)
                    bw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
