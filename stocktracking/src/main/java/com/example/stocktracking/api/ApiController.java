package com.example.stocktracking.api;

import com.example.stocktracking.models.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Iterator;

@Controller
public class ApiController {

//    @GetMapping("/data")
//    public String getData(Model model) throws UnirestException, JsonProcessingException {
//
//        String json = api_response();
//        Stock stock = parseJson(json);
//        System.out.println("stock : " + stock.toString());
//
//        model.addAttribute("stock", stock);
//
//        return "data";
//    }

    @GetMapping("/")
    public String getIndex(Model model) throws UnirestException, JsonProcessingException {

        String[] symbols = {"TSLA", "NKE", "GOOGL", "AZN"};
        Stock[] stocks= new Stock[symbols.length];

        for (int i = 0; i <= 3; i++) {

            String json = api_response(symbols[i]);
            Stock stock = parseJson(json);
            System.out.println("stock : " + stock.toString());

            double per = Math.round((stock.getRegularMarketPrice() - stock.getPreviousClose()) / stock.getPreviousClose() * 10000);
            double per2 = per / 100;
            boolean stat = (per2 >= 0);
            String perc = per2 + "%";
            stock.setStat(stat);
            stock.setPerc(perc);
            stocks[i]=stock;
        }

        String[] testes={"Asd","asbh"};
        model.addAttribute("stocks", stocks);
        model.addAttribute("testes", testes);

        return "index";
    }

    private String api_response(String symbol) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart?interval=5m&symbol="+symbol+"&range=1d&region=US&=&=apidojo-yahoo-finance-v1.p.rapidapi.com")
                .header("x-rapidapi-key", "15c0328c08msh88388eb63d58c40p1ae8b9jsnf4d797bbcc8f")
                .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .asString();

        System.out.println(response.getHeaders());
        System.out.println("response: " + response.getBody());

        return response.getBody();
    }

    private Stock parseJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(String.valueOf(json));

        ArrayNode result = (ArrayNode) node.get("chart").get("result");
        JsonNode meta = result.get(0).get("meta");
        System.out.println("meta : " + meta);

        // json to object
        return objectMapper.readValue(meta.toString(), Stock.class);
    }


    // not used
    private void process(String prefix, JsonNode currentNode) {
        if (currentNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) currentNode;
            Iterator<JsonNode> node = arrayNode.elements();
            int index = 1;
            while (node.hasNext()) {
                process(!prefix.isEmpty() ? prefix + "-" + index : String.valueOf(index), node.next());
                index += 1;
            }
        }
        else if (currentNode.isObject()) {
            currentNode.fields().forEachRemaining(entry -> process(!prefix.isEmpty() ? prefix + "-" + entry.getKey() : entry.getKey(), entry.getValue()));
        }
        else {
            System.out.println(prefix + ": " + currentNode.toString());
        }
    }

}
