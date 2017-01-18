package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.PorterStemmer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Utile {
	
	public static void main(String[] args) throws IOException, ImageProcessingException {
		String tags = new String(Files.readAllBytes(Paths.get("/Users/remi/Documents/dev/galata/elcipse/mirflickrmetadata/meta/tags/tags6169.txt")));
		System.out.println("lol" + tags);
		System.out.println(stemmIndex(tags.replaceAll("\\r\\n|\\r|\\n", " ")));
		
		
	}
	
	public static void extractTags() throws FileNotFoundException, UnsupportedEncodingException {
		String imageFilePath = "/Users/remi/Documents/dev/galata/elcipse/mirflickr2";
		
	    File folder = new File(imageFilePath);
	    File[] listOfFiles = folder.listFiles();
	    ArrayList<String> list = new ArrayList<String>();

	    for (int i = 0; i < listOfFiles.length; i++) {
	        String tagsFilePath = listOfFiles[i].toString().replace("im", "tags").replace("jpg", "txt").replace("mirflickr2", "mirflickrmetadata/meta/tags"); 
	        Scanner s = new Scanner(new File(tagsFilePath));
			while (s.hasNextLine()){
			    list.add(s.nextLine());
			}
			s.close();	        
	    }
	    
	    PrintWriter writer = new PrintWriter("resultTag.txt", "UTF-8");
	    ArrayList<String> listPresent = new ArrayList<String>();
	    for (String tag : list) {
	    	if (listPresent.contains(tag) == false) {
	    		listPresent.add(tag);
	    		int count = Collections.frequency(list, tag);
	    		writer.println(tag + " : " + count);
	    	}
	    }
	    writer.close();
	    
		
	}
	
	public static void reOrganize() throws IOException {
		Scanner s = new Scanner(new File("/Users/remi/Documents/dev/galata/elcipse/TestJana/result.txt"));
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		
		
		for (String path : list) {
			File source = new File(path);
			String tagsDestPath = path.replace("mirflickr", "mirflickr2");
			File dest = new File(tagsDestPath);
			FileUtils.copyFile(source, dest);
		}
	}
	
	public static String[] extractMetadata(String imagePath) throws IOException, ImageProcessingException {
		File jpegFile = new File(imagePath);

		String[] result = new String[4];
		Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
		
		String width = "";
		String height = ""; 
		String resolutionX = "";
		String resolutionY = "";
		
		for (Directory directory2 : metadata.getDirectories()) {
		    for (Tag tag : directory2.getTags()) {

		    	
		        if (tag.getTagName() == "Image Height") {
			        width = tag.toString();
		        } else if (tag.getTagName() == "Image Width") {
		        	 height = tag.toString();
		        } else if (tag.getTagName() == "X Resolution") {
		        	resolutionX = tag.toString();
		        } else if (tag.getTagName() == "Y Resolution") {
		        	resolutionY = tag.toString();
		        } 
		        	
		    }
		}
		result[0] = width;
		result[1] = height;
		result[2] = resolutionX;
		result[3] = resolutionY;
		return result;
		   
		
	}
	
	public static void stemm(String s) {
		 PorterStemmer stem = new PorterStemmer();
		 stem.setCurrent(s);
		 stem.stem();
		 System.out.println(stem.getCurrent());
		
	}
	
	public static String stemmIndex(String s) {
		PorterStemmer stem = new PorterStemmer();
		//String[] words = s;
		String[] words = s.split(" ");
		for(int i = 0; i < words.length; i++){
			stem.setCurrent(words[i]);
			stem.stem();
	        //System.out.println(stem.getCurrent());
			words[i] = stem.getCurrent();
		}
		String joinedString = StringUtils.join(words, " ");
		return joinedString;
		
	}

		  

}
