package actors;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import akka.actor.UntypedActor;
import app.messages.Stats;
import app.messages.Utility;


public class MessageCollector extends UntypedActor {

	/**
	 * Configuration
	 */
	private final int MESSAGE_LIMIT = 5000;

	/**
	 * Containers
	 */
	private File file;
	private LinkedList<String> messages = new LinkedList<String>();
	private String id;

	/**
	 * Constructor
	 * @param id
	 */
	public MessageCollector(String id) {
		this.id = id;
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
			update(utility.getUtility());
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

		file = new File("/root/monitoring/analytics/experiments/" + id + ".txt");

		// if file doesn't exist, create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}

		String message = new Date().getTime()+";start\n";
		messages.add(message);
	} 

	public void stop(){
		writeToFile();
	}

	public void success(){
		String message = (new Date().getTime()) + ";finished\n";
		messages.add(message);
		stop();
	}

	public void update(String update){

		messages.add(update);

		// Check if writout is necessary
		if(messages.size() > MESSAGE_LIMIT){
			writeToFile();
		}

	}

	public void stats(JsonNode stats){

		File file = new File("/root/monitoring/analytics/experiments/" + id + "_stats.txt");

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
			FileWriter fw = new FileWriter(filepath);

			// Process messages
			Iterator<String> it = stats.fieldNames();
			while(it.hasNext()){

				String key = it.next();
				JsonNode value = stats.get(key);

				// Process Text
				String singleStat = value.toString();
				System.out.println(stats);


				fw.write(key + " " + stats);
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
	private void writeToFile(){

		if(!messages.isEmpty()){

			final LinkedList<String> writeout = (LinkedList<String>) messages.clone();
			messages.clear();

			try {
				String filepath = file.getAbsolutePath();
				FileWriter fw = new FileWriter(filepath);
				for(String message : writeout){
					fw.write(message);
				}
				fw.close();
			} catch (Exception e){
				System.out.println("FileWriting failed");
				e.printStackTrace();
			}

		}

	}
}
