package it;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class main {
	
	
	public static void main(String[] args) throws IOException, ImageProcessingException, ParseException {		
		
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

		for(int i = 0; i < arrayOfDates.size(); i++) {
			 File oldfile = new File(folder + "\\" + arrayOfDates.get(i).get(1).toString());
		     File newfile = new File(folder + "\\THUMB_" + String.format("%03d", i) + ".JPG");
		     if(oldfile.renameTo(newfile)){
		    	 System.out.println(oldfile.getName() + " renamed to " + newfile.getName());
		     }		     
		     FileUtils.copyFileToDirectory(newfile, 
		    		 new File("C:\\Users\\Gianluca\\Desktop\\Archivio_digitale\\test\\thumbnails")
		    		 );
		     javaxt.io.Image image = new javaxt.io.Image(folder + "\\thumbnails\\THUMB_" + String.format("%03d", i) + ".JPG");
		     image.resize(315,236); 
		     image.saveAs(folder + "\\thumbnails\\THUMB_" + String.format("%03d", i) + ".JPG");
		     
		}	
		System.out.println("Preprocessing completed");
		
	} // end main
	

	
} // end class main




