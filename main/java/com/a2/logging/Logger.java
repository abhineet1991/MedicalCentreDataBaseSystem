package com.a2.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/** 
 * @author abhineet.gupta
 * studentId 012426427
 */

public class Logger {
	private File file;
	
	public Logger(String filename) {
		file = new File(filename);
	}
	
	public void info(String text){
		BufferedWriter output = null;
		
		text = new Date().toString() +" : "+text;
		System.out.println(text);
		try {
            output = new BufferedWriter(new FileWriter(file, true));
            output.write(text);
            output.newLine();
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null )
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}
}
