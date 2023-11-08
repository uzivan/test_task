package com.example.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
public class TestApplicationTests {
    @TestSubject
    private Controller controller;

    @Before
    public void setUp() {
        controller = new Controller();
    }
    @Test
    public void sendZeroLengthOrNullStringOrAllSpace(){
        assertThrows(ResponseStatusException.class, () -> controller.funcJson(new InputStringDto()));
        assertThrows(ResponseStatusException.class, ()->controller.funcJson(new InputStringDto("")));
        assertThrows(ResponseStatusException.class, ()->controller.funcJson(new InputStringDto("       ")));
    }

    @Test
    public void sendOrdinaryString(){
        String input = "qwew";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        ObjectNode element = objectMapper.createObjectNode();
        element.put("value", 2);
        ((ObjectNode) jsonNode).set("w", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("e", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("q", element.get("value"));
        assertEquals(controller.funcJson(new InputStringDto(input)).getBody(), jsonNode);
    }
    @Test
    public void sendOneElementString(){
        String input = "q";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        ObjectNode element = objectMapper.createObjectNode();
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("q", element.get("value"));
        assertEquals(controller.funcJson(new InputStringDto(input)).getBody(), jsonNode);
    }
    @Test
    public void sendUniqueSymbolString(){
        String input = "aaaaaaaaaaaaaaa";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        ObjectNode element = objectMapper.createObjectNode();
        element.put("value", 15);
        ((ObjectNode) jsonNode).set("a", element.get("value"));
        assertEquals(controller.funcJson(new InputStringDto(input)).getBody(), jsonNode);
    }
    @Test
    public void sendNoRepeatString(){
        String input = "abcde";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        ObjectNode element = objectMapper.createObjectNode();
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("a", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("b", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("c", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("d", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("e", element.get("value"));
        assertEquals(controller.funcJson(new InputStringDto(input)).getBody(), jsonNode);
    }
    @Test
    public void sendStringContainsSpace(){
        String input = "  a  b b s   ";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        ObjectNode element = objectMapper.createObjectNode();
        element.put("value", 2);
        ((ObjectNode) jsonNode).set("b", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("s", element.get("value"));
        element.put("value", 1);
        ((ObjectNode) jsonNode).set("a", element.get("value"));
        assertEquals(controller.funcJson(new InputStringDto(input)).getBody(), jsonNode);
    }

}
