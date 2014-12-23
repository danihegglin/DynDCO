package actors;

import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Play;
import play.libs.Json;
import play.mvc.WebSocket;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
			else {
				update(command);
			}

		}
	}

	/**
	 * Functions
	 */
	public void start(){
		
		file = new File("/root/monitoring/analytics/experiments/" + id + ".txt");

//		 if file doesn't exist, create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e){
				System.out.println("Creating file failed");
				e.printStackTrace();
			}
		}

		String message = "timestamp;agent;utility\n";
		messages.add(message);
	} 

	public void stop(){
		writeToFile();
	}

	public void success(){
		String message = (new Date().getTime() / 100) + ";finished\n";
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
