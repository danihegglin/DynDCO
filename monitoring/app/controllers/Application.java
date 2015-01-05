package controllers;

import scala.Option;
import akka.actor.ActorRef;
import akka.actor.Props;
import actors.UserActor;
import actors.StocksActor;

import actors.*;
import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
//import scala.Option;

//import com.sun.j3d.utils.scenegraph.io.retained.Controller;


/**
 * The main web controller that handles returning the index page, setting up a WebSocket, and watching a stock.
 */
public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render());
    }

    public static WebSocket<JsonNode> ws() {
        return new WebSocket<JsonNode>() {
            public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                
            	// create a new UserActor and give it the default stocks to watch
                final ActorRef userActor = Akka.system().actorOf(Props.create(UserActor.class, out));
                
                // send all WebSocket message to the UserActor
                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                    	System.out.println("Websocket opened");
                        // parse the JSON into WatchStock
                        WatchStock watchStock = new WatchStock(jsonNode.get("symbol").textValue());
                        // send the watchStock message to the StocksActor
                        StocksActor.stocksActor().tell(watchStock, userActor);
                    }
                });

                // on close, tell the userActor to shutdown
                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        final Option<String> none = Option.empty();
                        StocksActor.stocksActor().tell(new UnwatchStock(none), userActor);
                        Akka.system().stop(userActor);
                    }
                });
            }
        };
    }
    
    public static void sendUpdate(double utility){
    	StocksActor.stocksActor().tell(new UtilityUpdate(utility), ActorRef.noSender());
    }

}
