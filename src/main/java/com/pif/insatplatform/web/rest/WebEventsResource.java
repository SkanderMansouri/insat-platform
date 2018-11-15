package com.pif.insatplatform.web.rest;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;


@RestController
@RequestMapping("/api")

public class WebEventsResource {

    private final Logger log = LoggerFactory.getLogger(WebEventsResource.class);


    @PostMapping(value="/events")
    public String eventsTest(@RequestBody String chaine) {

        log.info("Receiving notification from slack");
           JsonReader jsonReader = Json.createReader(new StringReader(chaine));
        JsonObject object = jsonReader.readObject();
        String type = object.getString("type");
        String challenge = object.getString("challenge");
        jsonReader.close();

             if(type.equalsIgnoreCase("url_verification"))
       return challenge;
        else
return null;    }
}
