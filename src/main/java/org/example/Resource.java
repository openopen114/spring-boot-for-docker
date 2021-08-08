package org.example;

import cloud.sql.Druid;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

 

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import entity.Accounts;
import entity.LineNotify;
import http.Http;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Resource {

    Repo repo = new Repo();


    @RequestMapping(value = "/")
    String hello() {
        return "API WORKS!";
    }



    @RequestMapping(value = "/test", method= RequestMethod.GET)
    public String test() {
        return "test success";
    }


    //http://localhost:8080/api/echo?msg=aaaa
    @RequestMapping(value = "/echo", method = RequestMethod.GET, produces = { "application/json" })
    public String echo(@RequestParam("msg") String _msg) {
        JsonObject obj = new JsonObject();
        obj.addProperty("echo msg", _msg);
        obj.addProperty("time", String.valueOf(java.time.LocalDateTime.now()));

        String json = new Gson().toJson(obj);

        return json;
    }


    //http://localhost:8080//api/echo/1112
    @RequestMapping(value = "/echo/{_id}", method = RequestMethod.GET, produces = { "application/json" })
    public String echoId(@PathVariable("_id") Integer _id) {
        JsonObject obj = new JsonObject();
        obj.addProperty("echo id", _id);
        obj.addProperty("time", String.valueOf(java.time.LocalDateTime.now()));

        String json = new Gson().toJson(obj);

        return json;
    }
    
    

    @RequestMapping(value = "/db/config", method= RequestMethod.GET, produces = { "application/json" })
    public String dbConfig() throws IOException {

        InputStream in = this.getClass().getResourceAsStream("/DB.properties");
        Properties props = new Properties();
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        props.load(inputStreamReader);


        JsonObject obj = new JsonObject();
        obj.addProperty("APIENV", props.getProperty("APIENV"));
        obj.addProperty("CLOUD_SQL_CONNECTION_NAME", props.getProperty("CLOUD_SQL_CONNECTION_NAME"));

        String json = new Gson().toJson(obj);

        return json;


    }




    @RequestMapping(value = "/druidInfo", method= RequestMethod.GET, produces = { "application/json" })
    public String druidInfo() throws IOException, SQLException {

        repo.getTest();

        return Druid.getConnectInfo();

    }



    //http://localhost:8080/api/account/1
    @RequestMapping(value = "/account/{_id}", method = RequestMethod.GET, produces = { "application/json" })
    public String getAccountsByUserId(@PathVariable("_id") Integer _id) throws SQLException {
        JsonObject obj = new JsonObject();

        List<Accounts>  list = repo.getAccountsByUserId(_id);


        String json = new Gson().toJson(list);

        return json;
    }
    
    
    
    
    // 發送到 Line Notify
    // http://localhost:8080/api/send/line/notify  
    @PostMapping(value ="/send/line/notify", produces = { "application/json" }) 
    public String sendLineNotify(@RequestBody String _json) throws SQLException, InterruptedException, ExecutionException { 
    	 
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("====> Line Notify");
			System.out.println(_json);
			Gson gson = new Gson(); 
			LineNotify model = gson.fromJson(_json, LineNotify.class);  
			
			
 
			
			//讀 properties
			InputStream in = this.getClass().getResourceAsStream("/DB.properties");
			Properties props = new Properties();
			InputStreamReader inputStreamReader = null;
			try {
				inputStreamReader = new InputStreamReader(in, "UTF-8");
				props.load(inputStreamReader);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//API
			String LineNotifyAPI = props.getProperty("line.notify.api");
			
			//TOKEN
			String token = props.getProperty("line.bearer.token.notifytest");
	 
			
			// 發送 LINE NOTIFY
			try {
				Http http = new Http();
				http.sendPost(LineNotifyAPI, token, model.getMESSAGE());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
 
			// response
			JsonObject obj = new JsonObject(); 
			obj.addProperty("STATUS", true);
			obj.addProperty("ACTION", "LINE_NOTIFY"); 
			
			
			
			String json = new Gson().toJson(obj); 
			  
			return json; 
		 
		});
		
		String result = future.get(); 
		return result;
	}

    // 取得 tra01 最新一筆
    //http://localhost:8080/api/tra01/lastest
    @RequestMapping(value = "/tra01/lastest", method= RequestMethod.GET)
    public String getTra01Lastest() throws SQLException {
        return repo.getTra01Lastest();
    }









}
