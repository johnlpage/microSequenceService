package com.mydemocorp.mss;

import static spark.Spark.post;

import org.bson.Document;
import org.bson.RawBsonDocument;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import spark.Request;
import spark.Response;


public class MicroSequenceService 
{
	

	MongoClient mongoClient = null;
	MongoCollection<Document> collection = null;	
	
	public MicroSequenceService()
	{
		
		String username = Secrets.username;
		String password = Secrets.password;
		String hosts = "sequencecluster-kshqv.mongodb.net";
		String options = "/db?retryWrites=true";
		String URI;
		
		URI = "mongodb+srv://"+username+":"+password+"@"+hosts+options;
		
		mongoClient = new MongoClient(new MongoClientURI(URI));
		
		collection = mongoClient.getDatabase("mss").getCollection("sequence");
		mongoClient.getDatabase("admin").runCommand(new Document("ping",0));
	}
	
	public String nextNum(Request req, Response res) {
		res.type("application/json");
		
	
		Document newVal = collection.findOneAndUpdate(Filters.eq("_id","seq"),
				                    Updates.inc("counter", 1));
		
		return "{ok:"+newVal+"}\n";
		
	}
	 
    public static void main( String[] args )
    {
    	MicroSequenceService svc = new MicroSequenceService();
    	
    	post("/sequence", (req, res) -> svc.nextNum(req, res));
    }
}
