package app.messages;

public class Conflicts {
	
	private String conflicts;
	private String agent;
	private String timestamp;

	public Conflicts(String agent, String timestamp, String conflicts){
		this.setAgent(agent);
		this.setTimestamp(timestamp);
		this.setConflicts(conflicts);
	}

	public String getConflicts() {
	    return conflicts;
    }

	public void setConflicts(String conflicts) {
	    this.conflicts = conflicts;
    }

	public String getAgent() {
	    return agent;
    }

	public void setAgent(String agent) {
	    this.agent = agent;
    }

	public Long getTimestamp() {
	    return Long.parseLong(timestamp);
    }

	public void setTimestamp(String timestamp) {
	    this.timestamp = timestamp;
    }

}
