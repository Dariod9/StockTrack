package com.example.stocktracking.controllers;

import com.example.stocktracking.kafka.KafkaProducer;
import com.example.stocktracking.models.Stock;
import com.example.stocktracking.models.StockTable;
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

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private ArrayList<Stock> data;

    @GetMapping("/")
    public String getIndex(Model model) {

        if(data == null){
            updateData();
        }

        System.out.println(data.toString());

        model.addAttribute("stocks", data);
        model.addAttribute("listaGrande", getDataFromRepo());

        String message="Index page";
        logger.info(message);
        kafkaProducer.send("logs", message);


        return "index";
    }

    private ArrayList<ArrayList<StockTable>> getDataFromRepo(){
        int total = (int) rep.count();

        ArrayList<StockTable> listaTSLA= new ArrayList<>();
        ArrayList<StockTable> listaAZN= new ArrayList<>();
        ArrayList<StockTable> listaNKE= new ArrayList<>();
        ArrayList<StockTable> listaGOOGL= new ArrayList<>();

        int cntNike = 0;
        int cntT = 0;
        int cntA = 0;
        int cntG = 0;

        for(int i = total; i > 0; i--) {
            StockTable sT = rep.findById(i).get();
            if (sT.getSymbol().equalsIgnoreCase("nke")) {
                if (listaNKE.size() < 10) {
                    listaNKE.add(sT);
                    cntNike++;
                }
            } else if (sT.getSymbol().equalsIgnoreCase("tsla")) {
                if (listaTSLA.size() < 10) {
                    listaTSLA.add(sT);
                    cntT++;
                }
            } else if (sT.getSymbol().equalsIgnoreCase("googl")) {
                if (listaGOOGL.size() < 10) {
                    listaGOOGL.add(sT);
                    cntG++;
                }
            } else if (sT.getSymbol().equalsIgnoreCase("azn")) {
                if (listaAZN.size() < 10) {
                    listaAZN.add(sT);
                    cntA++;
                }
            }
        }


        ArrayList<ArrayList<StockTable>> lista= new ArrayList<>();
        lista.add(listaNKE);
        lista.add(listaAZN);
        lista.add(listaGOOGL);
        lista.add(listaTSLA);

        return lista;
    }

    // api só suporta 5 request por minuto e 500 por dia
    @Scheduled(fixedRate = 10 * 60000) // 1 vez por minuto
    private void updateData(){

        String message="Fetchind data from API!";
        logger.info(message);
        kafkaProducer.send("logs", message);

        ArrayList<Stock> temp = new ArrayList<>();

        String[] symbols = {"NKE", "TSLA", "GOOGL", "AZN"};
//        String[] symbols = {"NKE"};         // to reduce api request

        for (String symbol : symbols) {

            String json = apiResponse(symbol);

            Stock stock = jsonToObject(json);

            //
            String[] ss = stock.getPercent().split("%");
            double d = Double.parseDouble(ss[0]);
            if (d < -0.5 || d > 0.5){
                kafkaProducer.send("event" ,stock.getSymbol() + " had a significant variation!");
            }

            temp.add(stock);
        }

        for (Stock a : temp) {
            rep.save(new StockTable(a.getSymbol(), a.getPrice(), a.getPreviousClose()));
        }

        this.data = temp;
        message="API Data Updated";
        logger.info(message);
        kafkaProducer.send("logs", message);
    }

    private String apiResponse(String symbol) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=Z6CCUOWMUEO729W6";
        UriComponentsBuilder builder =  UriComponentsBuilder.fromHttpUrl(url);
        url = builder.build().toUriString();

        String message= "url: " +url;
        logger.info(message);
        kafkaProducer.send("logs", message);

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
            logger.error("ERROR! Something went wrong when parsing JSON. \n + " + e);
            kafkaProducer.send("logs", e.toString());
        }

        return stock;
    }

}
