package com.example.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {
    private static final String NULL_INPUT_STRING_ERROR_MESSAGE = "Input string should not be NULL";

    private static final String ZERO_LENGTH_OF_INPUT_STRING_MESSAGE = "Length of input string should not be 0";

    @PostMapping(value = "/doFunc")
    public ResponseEntity<JsonNode> funcJson(@RequestBody InputStringDto inputStringDto) {
        validateInputData(inputStringDto);
        deleteAllSpace(inputStringDto);
        String input_str = inputStringDto.getInput();
        Map<String, Integer> map_data = convertStringToMap(input_str);
        JsonNode jsonNode = convertToSortedJson(map_data);
        return ResponseEntity.ok(jsonNode);
    }

    private void deleteAllSpace(InputStringDto inputStringDto){
        String new_string = inputStringDto.getInput().replaceAll(" ", "");
        if (new_string.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ZERO_LENGTH_OF_INPUT_STRING_MESSAGE);
        }
        inputStringDto.setInput(new_string);
    }

    private void validateInputData(InputStringDto inputStringDto) {
        if (inputStringDto.getInput() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NULL_INPUT_STRING_ERROR_MESSAGE);
        }
        if (inputStringDto.getInput().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ZERO_LENGTH_OF_INPUT_STRING_MESSAGE);
        }
    }

    private Map<String, Integer> convertStringToMap(String data) {
        Map<String, Integer> mapData = new HashMap<>();
        Integer count;
        String symbol;
        for (int i = 0; i < data.length(); i++) {
            symbol = String.valueOf(data.charAt(i));
            count = mapData.get(symbol);
            if (count == null) {
                mapData.put(symbol, 1);
            } else {
                mapData.put(symbol, count + 1);
            }
        }
        return mapData;
    }

    private JsonNode convertToSortedJson(Map<String, Integer> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{}");
        } catch (IOException ignored) {
        }
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(map.entrySet());
        sortedList.sort(Map.Entry.comparingByKey());
        sortedList.sort(Map.Entry.comparingByValue());
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            ObjectNode element = objectMapper.createObjectNode();
            element.put("value", sortedList.get(i).getValue());
            ((ObjectNode) jsonNode).set(sortedList.get(i).getKey(), element.get("value"));
        }
        return jsonNode;
    }
}
