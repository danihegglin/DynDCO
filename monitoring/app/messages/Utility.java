package app.messages;

public class Utility {
	
	private String utility;
	private String agent;
	private String timestamp;

	public Utility(String agent, String timestamp, String utility){
		this.setAgent(agent);
		this.setTimestamp(timestamp);
		this.setUtility(utility);
	}

	public String getUtility() {
	    return utility;
    }

	public void setUtility(String utility) {
	    this.utility = utility;
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
