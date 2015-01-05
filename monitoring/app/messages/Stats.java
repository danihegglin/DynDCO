package app.messages;

import java.util.Map;

public class Stats {
	
	private Map<String,String> stats;

	public Stats(Map<String,String> stats){
		this.setStats(stats);
	}

	public Map<String,String> getStats() {
	    return stats;
    }

	public void setStats(Map<String,String> stats) {
	    this.stats = stats;
    }

}
