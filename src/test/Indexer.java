package test;

import java.util.ArrayList;
import java.util.Iterator;


import javax.imageio.ImageIO;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Indexer 
{
	public static void main(String[] args) throws IOException {
		
		// indexing
        // Getting all images from a directory and its sub directories.
        ArrayList<String> images = FileUtils.readFileLines(new File("/Users/remi/Documents/dev/galata/elcipse/mirflickr"), true);

        // Creating a CEDD document builder and indexing all files.
        GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);
        // Creating an Lucene IndexWriter
        IndexWriter iw = LuceneUtils.createIndexWriter("index", true, LuceneUtils.AnalyzerType.WhitespaceAnalyzer);

        // Iterating through images building the low level features
        for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            String tagsFilePath = imageFilePath.replace("im", "tags").replace("jpg", "txt").replace("mirflickr", "mirflickrmetadata/meta/tags"); 
            

            String tags = new String(Files.readAllBytes(Paths.get(tagsFilePath)));
            tags.replace("\n", " ");
            
            System.out.println("Indexing " + imageFilePath);
            try {
                BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                
                
    			document.add(new TextField("tags", tags, Field.Store.YES));
    			//document.add(new Field("tags", new FileReader("")));
    			System.out.println(tags);

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