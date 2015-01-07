package actors;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import akka.actor.UntypedActor;
import app.messages.Stats;
import app.messages.Utility;


public class MessageCollector extends UntypedActor {

	/**
	 * Configuration
	 */
	private final int MESSAGE_LIMIT = 100;
	private final String ROOT_FOLDER = "/root/monitoring/analytics/experiments/";
	private String TEST_FOLDER = "";

	/**
	 * Containers
	 */
	private File file;
	private HashMap<String, TreeMap<Long,String>> messages = new HashMap<String, TreeMap<Long,String>>();
	private String startTime;
	private String finishTime;
	private String id;

	/**
	 * Constructor
	 * @param id
	 */
	public MessageCollector(String id) {
		this.id = id;

		// Build folder for test case
		TEST_FOLDER = id.substring(0, id.lastIndexOf("-"));
		File dir = new File(ROOT_FOLDER + TEST_FOLDER);
		if (!dir.exists()) {
			dir.mkdir();
		}

	}

	/**
	 * Messagebox
	 * @param message
	 */
	public void onReceive(Object message) {

		if(message instanceof String){

			String command = (String) message;

			if(command == "start"){
				start();
			}
			else if(command == "stop"){
				stop();
			}
			else if(command == "success"){
				success();
			}
		}

		if(message instanceof Utility){
			Utility utility = (Utility) message;
			update(utility.getAgent(), utility.getTimestamp(), utility.getUtility());
		}

		if(message instanceof Stats){
			Stats stats = (Stats) message;
			stats(stats.getStats());
		}
	}

	/**
	 * Functions
	 */
	public void start(){

		file = new File(ROOT_FOLDER + TEST_FOLDER + "/" + id + ".txt");

		// if file doesn't exist, create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}

		startTime = new Date().getTime()+";start\n";
		writeLine(startTime, false);
	} 

	public void stop(){
		writeAllToFile();
	}

	public void success(){
		writeAllToFile();
		finishTime = (new Date().getTime()) + ";finished\n";
		writeLine(finishTime, true);
	}

	public void update(String agent, Long timestamp, String update){

		TreeMap<Long, String> entries = new TreeMap<>();
		if(messages.containsKey(agent)){
			entries = messages.get(agent);
		}
		entries.put(timestamp, update);
		messages.put(agent, entries);

		// Check if writout is necessary
		if(entries.size() > MESSAGE_LIMIT){
			writeToFile(agent, entries, null);
		}

	}

	public void stats(Map<String,String> stats){

		File file = new File(ROOT_FOLDER + TEST_FOLDER + "/" + id + "_stats.txt");

		// if file doesn't exist, create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}

		try {
			String filepath = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filepath, true);

			// Process messages
			for(String key : stats.keySet()){
				fw.write(key + ";" + stats.get(key) + "\n");
			}
			fw.close();
		} catch (Exception e){
			System.out.println("FileWriting failed");
			e.printStackTrace();
		}

	}

	/**
	 * Helper Functions
	 */
	private void writeAllToFile(){
		
		// Determine longest running agent and last timestamp (other agents utilities need to be expanded)
		Long maxRuntime = Long.parseLong("0");
		for(TreeMap<Long,String> value : messages.values()){
			for(Long timestamp : value.keySet()){
				if(timestamp > maxRuntime)
					maxRuntime = timestamp;
			}
		}
		
		System.out.println(maxRuntime);
		
		for(String agent : messages.keySet()){
			writeToFile(agent, messages.get(agent), maxRuntime);
		}
	}

	private void writeToFile(String agent, TreeMap<Long,String> entries, Long maxRuntime){
		
		try {
			String filepath = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filepath, true);

			Long lastTimepoint = (long) 0;
			String lastEntry = "";
			String data = "";
			for(Long timepoint : entries.keySet()){
				if(lastTimepoint != 0){
					while(lastTimepoint < timepoint){
						data = lastEntry.substring(lastEntry.indexOf(";"));
						lastTimepoint += 100;
						if(lastTimepoint < timepoint){
							fw.write(lastTimepoint + data);
						}
					}
				}
				
				lastTimepoint = timepoint;
				lastEntry = entries.get(timepoint);
				fw.write(lastEntry);
			}
			
			// if a maxRuntime is given, continue last utility until last timepoint
			if(maxRuntime != null && lastTimepoint < maxRuntime){
				while(lastTimepoint <= maxRuntime){
					data = lastEntry.substring(lastEntry.indexOf(";"));
					lastTimepoint += 100;
					if(lastTimepoint <= maxRuntime){
						fw.write(lastTimepoint + data);
					}
				}
			}

			fw.close();
		} catch (Exception e){
			System.out.println("FileWriting failed");
			e.printStackTrace();
		}

		entries.clear();
		messages.put(agent, entries);
	}
	
	private void writeLine(String line, Boolean append){
		
		try {
			String filepath = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filepath, append);
			fw.write(line);
			fw.close();
		} catch (Exception e){
			System.out.println("FileWriting failed");
			e.printStackTrace();
		}
	}
}
