package com.example.stocktracking.api;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

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
        int total= (int) rep.count();

        ArrayList<StockTable> listaTSLA= new ArrayList<>();
        ArrayList<StockTable> listaAZN= new ArrayList<>();
        ArrayList<StockTable> listaNKE= new ArrayList<>();
        ArrayList<StockTable> listaGOOGL= new ArrayList<>();

        int cntNike=0;
        int cntT=0;
        int cntA=0;
        int cntG=0;

        for(int i=total;i>0;i--) {
//            int tempIndex=i%4;
//            System.out.println(tempIndex);
//            switch(tempIndex){
//                case 0:
//                    listaNKE.add(rep.findById(i).get());
//                case 1:
//                    listaTSLA.add(rep.findById(i).get());
//                case 2:
//                    listaGOOGL.add(rep.findById(i).get());
//                case 3:
//                    listaAZN.add(rep.findById(i).get());
//            }
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
        model.addAttribute("listaGrande", lista);

        return "index";
    }

    // api só suporta 5 request por minuto e 500 por dia
    @Scheduled(fixedRate = 10 * 60000) // 1 vez por minuto
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
