package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

import actors.MessageCollector;
import akka.actor.*;
import app.messages.Stats;
import app.messages.Utility;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.mvc.Http.RequestBody;
import scala.Option;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

public class Monitoring extends Controller {

	/*
	 * Fields
	 */
	private static Map<String,Double> agentUtilities = new HashMap<>();
	private static Map<String, ActorRef> collectors = new HashMap<String, ActorRef>();

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

		try{

			// Receive parameter
			final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
			Map<String, String> parameters = getParameters(entries);
			String id = parameters.get("id");

			// Build new Message Collector Actor
			final ActorRef collector = Akka.system().actorOf(Props.create(MessageCollector.class, id));
			collectors.put(id, collector);

			// Tell actor
			collector.tell("start",ActorRef.noSender());

		} catch (Exception e){
			e.printStackTrace();
		}

		return ok("Started");
	}

	/**
	 * Writes messages to file, Resets UI
	 * @return
	 */
	public static Result stop() {

		System.out.println("Stop signal received");

		try {

			// Receive parameter
			final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
			Map<String, String> parameters = getParameters(entries);
			String id = parameters.get("id");

			// Tell actor
			ActorRef collector = collectors.get(id);
			collector.tell("stop", ActorRef.noSender());

			// Update UI
			Application.sendUpdate(0);

			//		collector.stop(); FIXME

		} catch (Exception e){
			e.printStackTrace();
		}

		return ok("Stopped");
	}

	/**
	 * Adds finish entry and stops
	 * @return
	 */
	public static Result success() {

		System.out.println("Success");

		try {

			// Receive parameter
			final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
			Map<String, String> parameters = getParameters(entries);
			String id = parameters.get("id");

			// Tell actor
			ActorRef collector = collectors.get(id);
			collector.tell("success", ActorRef.noSender());

		} catch (Exception e){
			e.printStackTrace();
		}

		return ok("Success");
	}

	/**
	 * Utility message processing
	 */
	public static Result updateAgent(String agent) throws Exception {

		try {

			// Receive get parameters
			final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
			Map<String, String> parameters = getParameters(entries);
			String id = parameters.get("id");

			// Get json body
			RequestBody body = request().body();
			JsonNode json = Json.parse(body.asText());
			if(json == null) {
				System.out.println("JSON is NULL");
				return badRequest("Expecting Json data");
			}

			// Get collector
			ActorRef collector = collectors.get(id);

			// Process messages
			Iterator<String> it = json.fieldNames();
			while(it.hasNext()){

				String key = it.next();
				JsonNode value = json.get(key);

				// Process Text
				String info = value.toString();
				String[] parts = info.split(";");
				
				String utility = parts[0].replaceAll("\"", "");
				String agentIndex = parts[1].replaceAll("\"", "");
				String meetingIndex = parts[2].replaceAll("\"", "");
				
				System.out.println(agent + " | " + key + " | " + utility + " | " + agent);

				// Send to Collector Actor				
				String update = (
						key + ";" + 
						agent + ";" + 
						utility + ";" + 
						agentIndex + ";" + 
						meetingIndex + "\n");
				
				collector.tell(new Utility(update), ActorRef.noSender());

				// Add to utilities
				agentUtilities.put(agent, Double.parseDouble(parts[0].toString()));
			}

			// Determine Global Utility
			double utilityGlobal = 0.0;
			for(String agentUtility : agentUtilities.keySet()){
				utilityGlobal += agentUtilities.get(agentUtility);
			}

			// Update UI
			Application.sendUpdate(utilityGlobal);
			System.out.println(utilityGlobal);

		} catch (Exception e){
			e.printStackTrace();
		}

		return ok("Update received: " + agent);
	}

	/**
	 * Stats message processing
	 */
	public static Result stats() throws Exception {

		try {

			// Receive parameter
			final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
			Map<String, String> parameters = getParameters(entries);
			String id = parameters.get("id");

			// Get json body
			RequestBody body = request().body();
			JsonNode json = Json.parse(body.asText());
			if(json == null) {
				System.out.println("JSON is NULL");
				return badRequest("Expecting Json data");
			}

			// Get collector
			ActorRef collector = collectors.get(id);

			// Process stats
			Map<String,String> stats = new HashMap<String,String>();
			Iterator<String> it = json.fieldNames();
			while(it.hasNext()){
				String key = it.next();
				String value = json.get(key).toString();
				stats.put(key,value);
			}

			// Send to Collector Actor				
			collector.tell(new Stats(stats), ActorRef.noSender());

		} catch (Exception e){
			e.printStackTrace();
		}

		return ok("Stats received");
	}

	/**
	 * Helper
	 * @param entries
	 * @return
	 */
	private static Map<String, String> getParameters(Set<Map.Entry<String,String[]>> entries){
		Map<String,String> parameters = new HashMap<String,String>();
		for (Map.Entry<String,String[]> entry : entries) {
			final String key = entry.getKey();
			final String value = Arrays.toString(entry.getValue());
			parameters.put(key, value.substring(1, value.length()-1));
		}
		return parameters;
	}
	
}
