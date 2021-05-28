package com.debug.demo.comsumer;

import com.debug.demo.consumer.TestXmlDeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TestXmlDeController.class)
public class TestXmlDeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("\"Author\" is marked as \"@XmlTransient\" thus won't be used in Ser/De")
    public void HappyPathAuthorShouldBeTransient() throws Exception {

        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<book id=\"1\">\n" +
                "    <title>Book1</title>\n" +
                "    <Author>test</Author>\n" +
                "</book>";
        String expect = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<book id=\"1\">\n" +
                "    <title>Book1</title>\n" +
                "</book>";
        mockMvc.perform(post("/deXml").content(payload)
                .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().xml(expect));
    }

    @Test
    @DisplayName("A typo on \"title\" in request should make the field silently ignored.")
    public void HappyPathUnknownFieldShouldBeIgnored() throws Exception {

        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<book id=\"1\">\n" +
                "    <titles>Book1</titles>\n" +
                "</book>";
        String expect = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<book id=\"1\">\n" +
                "</book>";
        mockMvc.perform(post("/deXml").content(payload)
                .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().xml(expect));
    }

    @Test
    @DisplayName("If there's a serialization issue occurs, it should return 400 not 415")
    public void SerializationIssueShouldReturn400() throws Exception {

        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<book id=\"s\">\n" +
                "    <title>Book1</title>\n" +
                "</book>";
        mockMvc.perform(post("/deXml").content(payload)
                .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When request header type does not match consume, then it will return 415")
    public void PostJsonContentHeaderShouldReturn415() throws Exception {

        String payload = "{\n" +
                "  \"field\": \"does not matter\"\n" +
                "}";
        mockMvc.perform(post("/deXml").content(payload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }
}
