package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.NLPPredictionResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class NLPService {

    @Autowired
    RestTemplate restTemplate;
    private static String TFIDF = "TFIDF";

    public String predictNlp(NLPRequest nlpRequest, String embedding) {
        try {


            if (embedding.toUpperCase().equals(TFIDF)) {
                URI uri = new URI("http://127.0.0.1:5000/nlp/predict/tfidf/svm");
                HttpHeaders headers = new HttpHeaders();

                HttpEntity<NLPRequest> requestEntity = new HttpEntity<>(nlpRequest, headers);
                NLPPredictionResponse response = restTemplate.postForObject(uri, requestEntity, NLPPredictionResponse.class);
                System.out.println("Success Result is:" + response.getPrediction());
                return response.getPrediction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
