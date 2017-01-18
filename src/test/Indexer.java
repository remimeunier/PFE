package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.builders.LocalDocumentBuilder;
import net.semanticmetadata.lire.classifiers.Cluster;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.local.opencvfeatures.CvSiftExtractor;
import net.semanticmetadata.lire.imageanalysis.features.local.opencvfeatures.CvSurfExtractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Indexer 
{
	
	
	
	public static void main(String[] args) throws IOException {
		
		// indexing
        // Getting all images from a directory and its sub directories.
        ArrayList<String> images = FileUtils.readFileLines(new File("/Users/remi/Documents/dev/galata/elcipse/mirflickr2"), true);

        // Creating a CEDD document builder
        GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);
        //add FCTH and AutoclorCorrelogram
        globalDocumentBuilder.addExtractor(FCTH.class);
        globalDocumentBuilder.addExtractor(AutoColorCorrelogram.class);

        
        // Creating an Lucene IndexWriter
        IndexWriter iw = LuceneUtils.createIndexWriter("index", true, LuceneUtils.AnalyzerType.WhitespaceAnalyzer);
        
        //analyzer
       // Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
        //analyzerPerField.put("tag", new StandardAnalyzer()); //KeywordAnalyzer
       // PerFieldAnalyzerWrapper aWrapper =new PerFieldAnalyzerWrapper(null, analyzerPerField);
       // Version matchVersion = Version.LUCENE_5_2_1;
       // Analyzer analyzer = new StandardAnalyzer();
        //analyzer.setVersion(matchVersion);
        
        String[] metadata = null;
        // Iterating through images building the low level features
        for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            String tagsFilePath = imageFilePath.replace("im", "tags").replace("jpg", "txt").replace("mirflickr2", "mirflickrmetadata/meta/tagssegmented"); 
            
            String tags = new String(Files.readAllBytes(Paths.get(tagsFilePath)));
            //tags.replace("\n", " ");
            
            System.out.println("Indexing " + imageFilePath);
            try {
            	//create doc + Fields visual data
                BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                              
                //Tags
    			document.add(new TextField("tags", Utile.stemmIndex(tags), Field.Store.YES));
    			System.out.println(Utile.stemmIndex(tags));
    			
    			//metadata
    			metadata = Utile.extractMetadata(imageFilePath);
    			document.add(new TextField("width", metadata[0], Field.Store.YES));
    			document.add(new TextField("height", metadata[1], Field.Store.YES));
    			document.add(new TextField("resolutionX", metadata[2], Field.Store.YES));
    			document.add(new TextField("resolutionY", metadata[3], Field.Store.YES));

    			
                iw.addDocument(document);
            } catch (Exception e) {
                System.err.println("Error reading image or indexing it.");
                e.printStackTrace();
            }
        }
        // closing the IndexWriter
        LuceneUtils.closeWriter(iw);
        System.out.println("Finished indexing.");
	
	}
	


}