package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import play.Play;
//import play.mvc.Controller;
//import play.mvc.Result;
//import play.mvc.WebSocket;
//
//import akka.actor.ActorRef;
//import actors.*;
//import akka.actor.*;
//import play.libs.Akka;
//import play.libs.F;
//import scala.Option;



//import actors.*;
import akka.actor.*;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Monitoring extends Controller {
	
	/*
	 * Fields
	 */
	private static double globalUtility = 0;
	private static Map<String,Double> agentUtilities = new HashMap<>();

	private static File file;
	private static FileWriter fw;
	
	private static PrintWriter writer;
	
	public static Result start() throws Exception {
		
		System.out.println("Start signal received");
		
		long timestamp = new Date().getTime();
		
		file = new File("analytics/experiments/results" + timestamp + ".txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		fw = new FileWriter(file.getAbsolutePath());
		fw.write("timestamp;agent;utility\n");

		return ok("Started");
	}
	
	public static Result stop() {
		
		System.out.println("Stop signal received");
		try{
			fw.close();
			Application.sendUpdate(0);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		return ok("Stopped");
	}
	
	public static Result success() {
		
		System.out.println("Success");
	
		String message = (new Date().getTime() / 100) + ";finished\n";
		try{
			fw.write(message);
			stop();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return ok("Success");
	}
	
	/**
	 * Methods
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
		
		// Write to file
		String message = (new Date().getTime() / 100) + ";" + agent + ";" + utility + "\n";
		fw.write(message);

		// Update UI
		double utilityUpdate = 0.0;
		for(String agentUtility : agentUtilities.keySet()){
			utilityUpdate += agentUtilities.get(agentUtility);
		}
		Application.sendUpdate(utilityUpdate);
		
		System.out.println(utilityUpdate);
		
		return ok("Update received: " + agent + " | " + utility);
	}
}
