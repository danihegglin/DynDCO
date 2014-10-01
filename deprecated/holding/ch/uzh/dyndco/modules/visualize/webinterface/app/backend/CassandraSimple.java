package data.storage.db;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraSimple {

	private CassandraSimple client;
	private Cluster cluster;
	private Session session = null;
	
	private static CassandraSimple instance = null;
	protected CassandraSimple() {
	}
	public static CassandraSimple getInstance() {
		if(instance == null) {
			instance = new CassandraSimple();
		}
		return instance;
	}
	
	public Session connect(String node) {
		
		cluster = Cluster.builder()
				.addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", 
				metadata.getClusterName());
		for ( Host host : metadata.getAllHosts() ) {
			System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
		
		return cluster.connect();
	}

	public void close() {
		client.close();
	}

	public void start() {
		this.session = this.connect("5.101.106.110");
	}
	
	public String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	public ResultSet execute(String query) {
		System.out.println(query);
		if(this.session == null){
			start();
		}
		return session.execute(query);
	}

}

