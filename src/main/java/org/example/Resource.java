package org.example;

import cloud.sql.Druid;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;

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


    //http://localhost:8080//api/echo?msg=aaaa
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








}
