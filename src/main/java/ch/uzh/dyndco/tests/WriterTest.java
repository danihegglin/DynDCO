package ch.uzh.dyndco.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class WriterTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		long timestamp = new Date().getTime();

		File file = new File("results" + timestamp + ".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

//		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

//		bw.write("timestamp;agent;utility");
		String message = "timestamp;agent;utility";
		String command = "echo '"+ message + "' >> " + file.getAbsolutePath();
//		System.out.println("command: " + command);
		
		System.out.println("command: " + command);
		
		 Process p = Runtime.getRuntime().exec(
				 new String[]{"sh","-c",command},
			        null, null);
		    p.waitFor();
		    
		    BufferedReader reader = 
		            new BufferedReader(new InputStreamReader(p.getInputStream()));
		    
		    StringBuffer output = new StringBuffer();
		    
		       String line = "";			
		       while ((line = reader.readLine())!= null) {
		   	output.append(line + "\n");
		       }

		       System.out.println(output);
	}

}
