package tfip.ssf.day16.controllers;

import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
//1)Path
//2)Accept mediatype
@RequestMapping(path="/RSVP",produces=MediaType.APPLICATION_JSON_VALUE)
public class RSVPController {
    private Logger logger = Logger.getLogger(RSVPController.class.getName());
    //3)Post
    //4)Post consume mediatype

    // application/json
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postAsJson(@RequestBody String payload){
        logger.log(Level.INFO, "+++ payload %s".formatted(payload));
        Reader reader = new StringReader(payload);
		JsonReader jsonReader = Json.createReader(reader);
		JsonObject json = jsonReader.readObject();

        String name = json.getString("name");
        boolean attending = json.getBoolean("attending");
        logger.log(Level.INFO, "+++ payload: %s, %b".formatted(name,attending));
    
        json = Json.createObjectBuilder()
            .add("message","Received %s RSVP".formatted(name))
            .add("attending",attending)
            .build();

        return ResponseEntity.status(201)
            .body(json.toString());
    }

    // application/x-www-form-urlencoded
    @PostMapping(consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> postAsForm(@RequestBody MultiValueMap<String,String> form){
        String name = form.getFirst("name");
        boolean attending = Boolean.parseBoolean(form.getFirst("attending"));
        
        logger.log(Level.INFO,">>> name: %s, attending: %b".formatted(name,attending));

        //Response
        // >201 Created
        // >Content-Type: application/json
        // >{"message": "Received <name> RSVP", "attending": true/false} send
        JsonObject payload = Json.createObjectBuilder()
            .add("message","Received %s RSVP".formatted(name))
            .add("attending",attending)
            .build();
        return ResponseEntity.status(201)
            .body(payload.toString());
    }

}
