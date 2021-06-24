package com.example.controller;

import com.example.config.NewsConfig;
import com.example.model.News;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class NewsController {

    @Autowired
    private NewsConfig newsConfig;

    @Value("${NEWSAPIKEY}")
    private String localNewsAPIKey;

    // Example of using GET with RestTemplate.geForObject() with ObjectMapper
    // You can use @GetMapping which is shorthand for @RequestMapping(method=RequestMethod.GET)
    @GetMapping(value = "news1", produces = MediaType.APPLICATION_JSON_VALUE)
    //@RequestMapping(value = "/news1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String news1(@RequestParam(defaultValue = "us") String country) throws Exception{

        final String uri = String.format("http://newsapi.org/v2/top-headlines?country=%s&apiKey=%s&pageSize=100", country, newsConfig.getApikey());

        RestTemplate restTemplate = new RestTemplate();

        News news = restTemplate.getForObject(uri, News.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
        mapper.writeValueAsString(news);
        String jsonString = mapper.writeValueAsString(news);

        return jsonString;

    }

    // Example of using GET with RestTemplate.exchange() with headers
    @RequestMapping(value = "/news2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> news2(@RequestParam(defaultValue = "us") String country) throws Exception{

        final String uri = String.format("http://newsapi.org/v2/top-headlines?country=%s&pageSize=100", country);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("x-api-key", newsConfig.getApikey());
//        httpHeaders.add("useQueryString", "true");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);

        return response;
    }

    // Example of using GET with RestTemplate.exchange() with headers
    // Also, converts the response to a News object as the return object
    @RequestMapping(value = "/news3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public News news3(@RequestParam(defaultValue = "us") String country) throws Exception{

        final String uri = String.format("http://newsapi.org/v2/top-headlines?country=%s&pageSize=100", country);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("x-api-key", newsConfig.getApikey());
//        httpHeaders.add("useQueryString", "true");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<News> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, News.class);

        System.out.println(response.getBody().getStatus());

        return response.getBody();
    }
}
