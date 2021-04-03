package com.example.stocktracking.api;

import com.example.stocktracking.models.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.DataInput;
import java.io.IOException;

@RestController
public class ApiController {

    @GetMapping("/data")
    public String getData() throws UnirestException, JsonProcessingException {

        HttpResponse<String> response = Unirest.get("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=AMD%2CIBM%2CAAPL")
                .header("x-rapidapi-key", "15c0328c08msh88388eb63d58c40p1ae8b9jsnf4d797bbcc8f")
                .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .asString();

        System.out.println(response.getHeaders());
        System.out.println("response: " + response.toString());
        System.out.println("response: " + response.getBody());

        String json = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(String.valueOf(json));
//        ApiResult node = objectMapper.treeToValue(n, ApiResult.class);
//        ApiResult node = objectMapper.readValue(response, ApiResult.class);

        String result = node.get("quoteResponse").get("result").toString();
        System.out.println("result : " + result);

        return result;
    }

}
