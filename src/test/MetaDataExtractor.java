package test;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


public class MetaDataExtractor {
	
	public static void main(String[] args) throws IOException, ImageProcessingException {
		
		File jpegFile = new File("/Users/remi/Documents/dev/galata/elcipse/mirflickr/im2322.jpg");

		Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

		
		for (Directory directory : metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		        System.out.println(tag);
		    }
		}
		
	}

}
