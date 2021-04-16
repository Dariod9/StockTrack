package com.example.stocktracking.api;

import com.example.stocktracking.MainController;
import com.example.stocktracking.models.Stock;
import com.example.stocktracking.models.StockTable;
import com.example.stocktracking.MainController.*;
import com.example.stocktracking.models.StockTableRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@Controller
public class ApiController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockTableRepository rep;
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private ArrayList<Stock> data;

    @GetMapping("/")
    public String getIndex(Model model) {

        if(data == null){
            updateData();
        }

        System.out.println(data.toString());

        model.addAttribute("stocks", data);

        return "index";
    }

    // api só suporta 5 request por minuto e 500 por dia
    // that was learned the hard way :)
    @Scheduled(fixedRate = 60000) // 1 vez por minuto
    private void updateData(){

        logger.info("Fetchind data from API!");

        ArrayList<Stock> temp = new ArrayList<>();

        String[] symbols = {"NKE", "TSLA", "GOOGL", "AZN"};

        for (int i = 0; i < symbols.length; i++) {

            String json = apiResponse(symbols[i]);

            Stock stock = jsonToObject(json);

            temp.add(stock);
        }

        for(int i=0;i<temp.size();i++){
            Stock a= temp.get(i);
//            double per=(double) a.getPercent().replace("%","");
            rep.save(new StockTable(a.getSymbol(), a.getPrice(), a.getPreviousClose()));
        }
//    }

        this.data = temp;
        logger.info("Updated");
    }

    private String apiResponse(String symbol) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=Z6CCUOWMUEO729W6";
        UriComponentsBuilder builder =  UriComponentsBuilder.fromHttpUrl(url);
        url = builder.build().toUriString();

        logger.info("url: " + url);

        String result = restTemplate.getForObject(url, String.class);

        return result;
    }

    private Stock jsonToObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        Stock stock = null;
        try {
            node = objectMapper.readTree(String.valueOf(json));
            String s = node.get("Global Quote").toString();
            // json to object
            stock = objectMapper.readValue(s, Stock.class);
            System.out.println(stock.toString());
        } catch (JsonProcessingException e) {
            logger.error("ERROR! Something went wrong when parsing JSON. '\' + " + e.toString());
        }

        return stock;
    }

}
