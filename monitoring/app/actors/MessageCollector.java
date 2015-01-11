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
import app.messages.Conflicts;

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
	private File utilityFile;
	private File conflictsFile;
	private File statsFile;
	private HashMap<String, TreeMap<Long,String>> utilities = new HashMap<String, TreeMap<Long,String>>();
	private HashMap<String, TreeMap<Long,String>> conflicts = new HashMap<String, TreeMap<Long,String>>();
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
		
		// Create utility file
		utilityFile = new File(ROOT_FOLDER + TEST_FOLDER + "/" + id + ".txt");

		// if file doesn't exist, create it
		if (!utilityFile.exists()) {
			try {
				utilityFile.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}
		
		// Create conflics file
		conflictsFile = new File(ROOT_FOLDER + TEST_FOLDER + "/" + id + "_conflicts.txt");

		// if file doesn't exist, create it
		if (!conflictsFile.exists()) {
			try {
				conflictsFile.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}
		
		// Create stats file
		statsFile = new File(ROOT_FOLDER + TEST_FOLDER + "/" + id + "_stats.txt");

		// if file doesn't exist, create it
		if (!statsFile.exists()) {
			try {
				statsFile.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
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
		
		if(message instanceof Conflicts){
			Conflicts conflicts = (Conflicts) message;
			conflicts(conflicts.getAgent(), conflicts.getTimestamp(), conflicts.getConflicts());
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
		startTime = new Date().getTime()+";start\n";
		writeLine(startTime, false, utilityFile);
	} 

	public void stop(){
		writeAllToFile();
	}

	public void success(){
		writeAllToFile();
		finishTime = (new Date().getTime()) + ";finished\n";
		writeLine(finishTime, true, utilityFile);
	}

	public void update(String agent, Long timestamp, String update){

		TreeMap<Long, String> entries = new TreeMap<>();
		if(utilities.containsKey(agent)){
			entries = utilities.get(agent);
		}
		entries.put(timestamp, update);
		utilities.put(agent, entries);

		// Check if writout is necessary
		if(entries.size() > MESSAGE_LIMIT){
			writeUtilitiesToFile(agent, entries, null, utilityFile);
		}

	}
	
	public void conflicts(String agent, Long timestamp, String numConflicts){
		
		TreeMap<Long, String> entries = new TreeMap<>();
		if(conflicts.containsKey(agent)){
			entries = conflicts.get(agent);
		}
		entries.put(timestamp, numConflicts);
		conflicts.put(agent, entries);

		// Check if writout is necessary
		if(entries.size() > MESSAGE_LIMIT){
			writeConflictsToFile(agent, entries, conflictsFile);
		}

	}

	public void stats(Map<String,String> stats){

		try {
			String filepath = statsFile.getAbsolutePath();
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
		for(TreeMap<Long,String> value : utilities.values()){
			for(Long timestamp : value.keySet()){
				if(timestamp > maxRuntime)
					maxRuntime = timestamp;
			}
		}
		
		// Write all utilities
		for(String agent : utilities.keySet()){
			writeUtilitiesToFile(agent, utilities.get(agent), maxRuntime, utilityFile);
		}
		
		// Write all conflicts
		for(String agent : conflicts.keySet()){
			writeConflictsToFile(agent, conflicts.get(agent), conflictsFile);
		}
	}

	private void writeUtilitiesToFile(String agent, TreeMap<Long,String> entries, Long maxRuntime, File file){
		
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
		utilities.put(agent, entries);
	}
	
	private void writeConflictsToFile(String agent, TreeMap<Long,String> entries, File file){
		
		try {
			String filepath = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filepath, true);

			for(Long timepoint : entries.keySet()){
				fw.write(""+timepoint+"\n");
			}
			
			fw.close();
		} catch (Exception e){
			System.out.println("FileWriting failed");
			e.printStackTrace();
		}

		entries.clear();
		conflicts.put(agent, entries);
	}
	
	
	
	private void writeLine(String line, Boolean append, File file){
		
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
