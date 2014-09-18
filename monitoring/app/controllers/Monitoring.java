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
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;

import com.fasterxml.jackson.databind.JsonNode;

public class Monitoring extends Controller {
	
	/*
	 * Fields
	 */
	private static double globalUtility = 0;
	private static Map<String,Double> agentUtilities = new HashMap<>();
//	private static Thread workerThread = new Thread(new UtilityWorker(globalUtility));
	
	/**
	 * Methods
	 */
	public static Result updateAgent(String agent) throws Exception {
		
		Map<String,String> parameters = new HashMap<String,String>();
		final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
		for (Map.Entry<String,String[]> entry : entries) {
			final String key = entry.getKey();
			final String value = Arrays.toString(entry.getValue());
			parameters.put(key, value.substring(1, value.length()-1));
		}
		
		double utility = Double.parseDouble(parameters.get("utility"));
		
		//System.out.println("Agent: " + agent + " -> " + utility);
		
		// Remove old utility from global utility
		if(agentUtilities.containsKey(agent)){
			globalUtility -= agentUtilities.get(agent);
		}
		
		// Add new utility to global utility
		globalUtility += utility;
				
		// Add new values to agent map
		agentUtilities.put(agent, utility);
		
		Application.sendUpdate(globalUtility);
		
		//Application.sendUpdate(globalUtility);
//		if(!workerThread.isAlive()){
//			workerThread.start();
//		}
		
		return ok("Update received: " + agent + " | " + utility);
	}
	
//	public static class UtilityWorker implements Runnable {
//		
//		private double utility = 0.0;
//		
//		public UtilityWorker(double utility){
//			this.utility = utility;
//		}
//		
//		public void setUtility(double utility){
//			this.utility = utility;
//		}
//		
//		public void run(){
//			Application.sendUpdate(this.utility);
//			try {
//			Thread.sleep(750);
//			}
//			catch (Exception e){}
//		}
//	}
}
