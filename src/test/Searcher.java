package test;


import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.filters.RerankFilter;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.PrintWriter;


public class Searcher {
    public static void main(String[] args) throws IOException, ParseException {
        // Checking if arg[0] is there and if it is an image.
        BufferedImage img = null;
        
        int searchType = 1; //1 = tags, 2 = images, 3 = tags, then rerank with lire, 4 = querry sentence
        
        String querryStr = "tags:Pinguin Glace";
        String imageFilePath = "/Users/remi/Documents/dev/galata/elcipse/mirflickr/im14614.jpg";



        IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get("index")));

       	if (searchType == 1) {
       		System.out.println("1");
       		searchTags(ir, querryStr);
       	}
       	else if (searchType == 2) {
       		System.out.println("2");
       		File f = new File(imageFilePath);
            ImageSearcher searcher = new GenericFastImageSearcher(30, CEDD.class);
//          ImageSearcher searcher = new GenericFastImageSearcher(30, AutoColorCorrelogram.class);
            if (f.exists()) {
                try {
                    img = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            SearchImage(ir, searcher, img);
       	} else if (searchType == 3) {
       		System.out.println("3");
       		SearchTagsAndRerankLire(ir, querryStr, imageFilePath);
       	} else if (searchType == 4) {
       		System.out.println("4");
       		
       		IndexSearcher indexSearcher = new IndexSearcher(ir);
            QueryParser queryParser = new QueryParser("tags",  new StandardAnalyzer());  
            queryParser.setDefaultOperator(QueryParser.Operator.OR);
            Query query = queryParser.parse("a wooman with tattoos smoke a cigarette");
            TopDocs topDocs = indexSearcher.search(query, 20);
            
            displayResultLucen(topDocs, indexSearcher);
       	} 
        
        
        // searching with a Lucene document instance ...      
     //   ImageSearchHits hits = searcher.search(ir.document(8), ir);
     //   System.out.println("dsdsds");

    }
    
    public static void searchTags(IndexReader ir, String querryStr) throws IOException, ParseException {
        //search with tags
       IndexSearcher indexSearcher = new IndexSearcher(ir);
       QueryParser queryParser = new QueryParser("tags",  new StandardAnalyzer());  
       Query query = queryParser.parse(querryStr);
       TopDocs topDocs = indexSearcher.search(query, 1214);
       

       displayResultLucen(topDocs, indexSearcher);
       //PrintResult(topDocs, indexSearcher);

    }
    
    public static void displayResultLucen(TopDocs topDocs, IndexSearcher indexSearcher) throws IOException {
    	System.out.println("blabla");
        System.out.println("Total hits : " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {           
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Score : " +scoreDoc.score + "path " + document.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0]);
        }
    }
    
    
    public static void SearchImage(IndexReader ir, ImageSearcher searcher, BufferedImage img) throws IOException {
    	ImageSearchHits hits = searcher.search(img, ir);  
    	displayResultLire(hits, ir);
            
    }
    
    public static void displayResultLire(ImageSearchHits hits, IndexReader ir) throws IOException {
        for (int i = 0; i < hits.length(); i++) {
            String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
            System.out.println(hits.score(i) + ": \t" + fileName);
        }
    }
        
    public static void SearchTagsAndRerankLire(IndexReader ir, String querryStr, String imageFilePath) throws IOException, ParseException {
    	IndexSearcher indexSearcher = new IndexSearcher(ir);
        QueryParser queryParser = new QueryParser("contents",  new StandardAnalyzer());  
        Query query = queryParser.parse(querryStr);
        TopDocs topDocs = indexSearcher.search(query, 20);
        
        System.out.println("Résultat with tags only : ");
        displayResultLucen(topDocs, indexSearcher);
        
        GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);
        BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
        Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
        
        RerankFilter filter = new RerankFilter(CEDD.class, DocumentBuilder.FIELD_NAME_CEDD);
        ImageSearchHits hits = filter.filter(topDocs, ir, document);
        
        System.out.println("Résultat Rerank with LIRE : ");
        displayResultLire(hits, ir);
    
    }
    
    public static void PrintResult(TopDocs topDocs, IndexSearcher indexSearcher) throws IOException {

        try{
            PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {           
                Document document = indexSearcher.doc(scoreDoc.doc);
                writer.println(document.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0]);
            }
            writer.close();
        } catch (Exception e) {
           // do something
        }
    }

    
}