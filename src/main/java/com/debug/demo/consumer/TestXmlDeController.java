package com.debug.demo.consumer;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestXmlDeController {

    @PostMapping(value = "/deXml", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TestXmlObject> deXmlEndpoint(@RequestBody TestXmlObject testXmlObject) {
        return ResponseEntity.ok(testXmlObject);
    }
}
