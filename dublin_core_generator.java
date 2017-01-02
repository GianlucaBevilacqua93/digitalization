package it;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Dublincore {

	// generatore del file XML seguendo lo standard Dublin Core
	public static void main(String[] args) throws ImageProcessingException, IOException, ParseException {	
		
		Vector<Vector<Object>> v = getExif();
		
		try
	     {
	          FileOutputStream prova = new FileOutputStream("C:\\Users\\Gianluca\\Desktop\\metadata.xml");
	          PrintStream scrivi = new PrintStream(prova);
	          
	          scrivi.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	          scrivi.println("<metadata");
	          scrivi.println("\txmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
	          scrivi.println("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance/\">");
	          
	          // È necessario il PHP per discriminare le tipologie di utenti che possono accedere
	          String URI = "http://apuliadigital.altervista.org/repository/thumbnails/";
	          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	          
	          for(int i = 0; i < v.size(); i++) {
	        	  scrivi.println("\t<image>"); 
	        	  scrivi.println("\t\t<content>");
	        	  scrivi.println("\t\t\t<dc:description>Photograph taken with Canon IXUS 155 camera</dc:description>");
	        	  scrivi.println("\t\t\t<dc:subject>Flora and Fauna</dc:subject>");
	        	  scrivi.println("\t\t\t<dc:format>Image/jpeg</dc:format>");
	        	  scrivi.println("\t\t\t<dc:coverage></dc:coverage>");
	        	  scrivi.println("\t\t\t<dc:title></dc:title>");
	        	  scrivi.println("\t\t</content>");
	        	  scrivi.println("\t\t<intellectualproperty>");
	        	  scrivi.println("\t\t\t<dc:creator>Marika Montanari, University of Bari</dc:creator>");
	        	  scrivi.println("\t\t\t<dc:publisher>Bevilacqua Gianluca Gennaro, University of Bari</dc:publisher>");
	        	  scrivi.println("\t\t\t<dc:rights>Digital Image Copyright (c) 2016. University of Bari. All Rights Reserved. For permission to use, contact: Marika Montanari, University of Bari. All Rights Reserved.</dc:rights>");
	        	  scrivi.println("\t\t</intellectualproperty>");
	        	  scrivi.println("\t\t<instantiation>");
	        	  scrivi.println("\t\t\t<dc:identifier>" + URI + v.get(i).get(1).toString() + "</dc:identifier>");
	        	  scrivi.println("\t\t\t<dc:date>" + sdf.format(v.get(i).get(0)) + "</dc:date>");
	        	  scrivi.println("\t\t\t<dc:coverage></dc:coverage>");
	        	  scrivi.println("\t\t</instantiation>");  	  
	        	  scrivi.println("\t</image>");
	          }
	          scrivi.println("</metadata>");
	     }
	      catch (IOException e)
	      {
	          System.out.println("Errore: " + e);
	          System.exit(1);
	      }	

	} // end main
	
	public static Vector<Vector<Object>> getExif() throws ImageProcessingException, IOException, ParseException {
		
		File folder = new File("C:\\Users\\Gianluca\\Desktop\\Archivio_digitale\\test");
		File[] listOfFiles = folder.listFiles();
		Vector<Vector<Object>> arrayOfDates = new Vector<Vector<Object>>();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
				File jpegFile = new File(folder + "\\" + file.getName());
				Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);		
				ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
				Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
     			Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.HOUR, -2);
				date = cal.getTime();				
								
//				System.out.println(dt1.format(date));
				Date dat = dt1.parse(dt1.format(date));
				Vector<Object> coppia = new Vector<Object>();
				coppia.addElement(dat);
				coppia.addElement(file.getName());
				arrayOfDates.addElement(coppia);
		    }
		} // end for
		
		Collections.sort(arrayOfDates, new Comparator<Vector<Object>>(){
		    @Override  public int compare(Vector<Object> v1, Vector<Object> v2) {
		        return ((Date) v1.get(0)).compareTo((Date) v2.get(0)); //If you order by 2nd element in row
		}});
		
		return arrayOfDates;
	}

}
