package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

import akka.actor.*;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Monitoring extends Controller {
	
	/**
	 * Configuration
	 */
	private final static int MESSAGE_LIMIT = 500;
	
	/*
	 * Fields
	 */
//	private static double globalUtility = 0;
	private static Map<String,Double> agentUtilities = new HashMap<>();
	private static LinkedList<String> messages = new LinkedList<String>();
	
	private static File file;
	private static FileWriter fw;
	
	private static PrintWriter writer;
	
	/**
	 * Initializes file and header
	 * @return
	 * @throws Exception
	 */
	public static Result start() throws Exception {
		
		System.out.println("Start signal received");
		
		long timestamp = new Date().getTime();
		
		file = new File("/root/monitoring/analytics/results/" + timestamp + ".txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
//		fw = new FileWriter(file.getAbsolutePath());
		
		String message = "timestamp;agent;utility\n";
		messages.add(message);

		return ok("Started");
	}
	
	/**
	 * Writes messages to file, Resets UI
	 * @return
	 */
	public static Result stop() {
		
		System.out.println("Stop signal received");
		
		writeToFile();
		
//		try {
//			fw.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		Application.sendUpdate(0);
		
		return ok("Stopped");
	}
	
	/**
	 * Adds finish entry and stops
	 * @return
	 */
	public static Result success() {
		
		System.out.println("Success");
	
		String message = (new Date().getTime() / 100) + ";finished\n";
		messages.add(message);
		
		stop();
		
		return ok("Success");
	}
	
	/**
	 * Utility message processing
	 */
	public static Result updateAgent(String agent) throws Exception {
		
		// Receive parameter
		Map<String,String> parameters = new HashMap<String,String>();
		final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
		for (Map.Entry<String,String[]> entry : entries) {
			final String key = entry.getKey();
			final String value = Arrays.toString(entry.getValue());
			parameters.put(key, value.substring(1, value.length()-1));
		}
		double utility = Double.parseDouble(parameters.get("utility"));
				
		// Add new values to agent map
		agentUtilities.put(agent, utility);
		
		// Add to list
		String message = (new Date().getTime() / 100) + ";" + agent + ";" + utility + "\n";
		messages.add(message);
		
		// Check if writout is necessary
		if(messages.size() > MESSAGE_LIMIT){
			writeToFile();
		}

		// Determine Global Utility
		double utilityGlobal = 0.0;
		for(String agentUtility : agentUtilities.keySet()){
			utilityGlobal += agentUtilities.get(agentUtility);
		}
		
		// Update UI
		Application.sendUpdate(utilityGlobal);
		System.out.println(utilityGlobal);
		
		return ok("Update received: " + agent + " | " + utility);
	}
	
	private static void writeToFile(){
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try{
					if(!messages.isEmpty()){
						
						final LinkedList<String> writeout = (LinkedList<String>) messages.clone();
						messages.clear();
						
						fw = new FileWriter(file.getAbsolutePath());
						for(String message : writeout){
							fw.write(message);
						}
						fw.flush();
						fw.close();
						
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		};
		
		new Thread(r).start();
	}
}
