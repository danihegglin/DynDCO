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
		
		System.out.println("Agent: " + agent + " -> " + utility);
		
		// Remove old utility from global utility
		if(agentUtilities.containsKey(agent)){
			globalUtility -= agentUtilities.get(agent);
		}
		
		// Add new utility to global utility
		globalUtility += utility;
				
		// Add new values to agent map
		agentUtilities.put(agent, utility);
		
		return ok("Update received: " + agent + " | " + utility);
	}
	
	public static WebSocket<JsonNode> monitor() {
        return new WebSocket<JsonNode>() {
            public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                // create a new UserActor and give it the default stocks to watch
               // final ActorRef userActor = Akka.system().actorOf(Props.create(UserActor.class, out));
                
                // send all WebSocket message to the UserActor
                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        // parse the JSON into WatchStock
                        //WatchStock watchStock = new WatchStock(jsonNode.get("symbol").textValue());
                        // send the watchStock message to the StocksActor
                        //StocksActor.stocksActor().tell(watchStock, userActor);
                    }
                });

                // on close, tell the userActor to shutdown
                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        final Option<String> none = Option.empty();
                        //StocksActor.stocksActor().tell(new UnwatchStock(none), userActor);
                      //  Akka.system().stop(userActor);
                    }
                });
            }
        };
    }

}
