package controllers;

import data.storage.db.CassandraSimple;
import play.*;
import play.mvc.*;

public class Predictions extends Controller {

	// Database Instances
	private static CassandraSimple cassandra = null;

	private static void checkConnection() {
		if(cassandra == null){
			cassandra = CassandraSimple.getInstance();
		}
	}

	public static Result showOverview() {
		
		checkConnection();
		
		String query = "select *";
		ResultSet result = cassandra.execute(query);
		
		return ok(views.html.stock());
	}

	public static Result showTweets() {
		return ok("Hello Tweet");
	}

	public static Result showMarketData() {
		return ok("Hello Stock");
	}

	public static Result showNewsFeed() {
		return ok("Hello NewsItem");
	}

	public static Result showBanksOpinion() {
		return ok("Hello BankOpinion");
	}

	public static Result showTrends() {
		return ok("Hello GoogleTrends");
	}

}