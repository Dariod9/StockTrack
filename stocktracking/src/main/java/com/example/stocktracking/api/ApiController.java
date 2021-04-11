package com.example.stocktracking.api;

import com.example.stocktracking.models.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;

@Controller
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private ArrayList<Stock> data;

    @GetMapping("/")
    public String getIndex(Model model) {

        if(data == null){
            updateData();
        }

        model.addAttribute("stocks", data);

        return "index";
    }

    @Scheduled(fixedRate = 10000)
    private void updateData(){

        ArrayList<Stock> temp = new ArrayList<>();

        String[] symbols = {"TSLA", "NKE", "GOOGL", "AZN"};

        for (int i = 0; i <= symbols.length - 1; i++) {
            String json = null;
            try {
                json = apiResponse(symbols[i]);
            } catch (UnirestException e) {
                logger.error("ERROR: Fetching data from API");
            }

            Stock stock = null;
            try {
                stock = jsonToObject(json);
            } catch (JsonProcessingException e) {
                logger.error("ERROR: parsing json data");
            }
            System.out.println("stock : " + stock.toString());

            // calcular atributos
            double per = Math.round((stock.getRegularMarketPrice() - stock.getPreviousClose()) / stock.getPreviousClose() * 10000);
            double per2 = per / 100;
            boolean stat = (per2 >= 0);
            String perc = per2 + "%";
            stock.setStat(stat);
            stock.setPerc(perc);

            temp.add(stock);
        }
        this.data = temp;
        logger.info("Updated");
    }

    private String apiResponse(String symbol) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart?interval=5m&symbol="+symbol+"&range=1d&region=US&=&=apidojo-yahoo-finance-v1.p.rapidapi.com")
                .header("x-rapidapi-key", "15c0328c08msh88388eb63d58c40p1ae8b9jsnf4d797bbcc8f")
                .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .asString();

        System.out.println(response.getHeaders());
        System.out.println("response: " + response.getBody());

        return response.getBody();
    }

    private Stock jsonToObject(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(String.valueOf(json));

        ArrayNode result = (ArrayNode) node.get("chart").get("result");
        JsonNode meta = result.get(0).get("meta");
        System.out.println("meta : " + meta);

        // json to object
        return objectMapper.readValue(meta.toString(), Stock.class);
    }

}
