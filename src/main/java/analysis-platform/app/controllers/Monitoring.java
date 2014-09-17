import java.util.HashMap;
import java.util.Map;


public class Monitoring {
	
	/*
	 * Fields
	 */
	private double globalUtility = 0;
	private Map<String,Double> agentUtilities = new HashMap<>();
	
	/**
	 * Methods
	 */
	public void updateGlobal(){
		return html(monitoring.html);
	}
	
	public void updateAgent(String agent, double utility){
		
		// Remove old utility from global utility
		if(agentUtilities.containsKey(agent)){
			globalUtility -= agentUtilities.get(agent);
		}
		
		// Add new utility to global utility
		globalUtility += utility;
				
		// Add new values to agent map
		agentUtilities.put(agent, utility);
	}

}
