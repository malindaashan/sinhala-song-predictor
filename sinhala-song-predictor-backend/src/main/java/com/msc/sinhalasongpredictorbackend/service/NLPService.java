package com.msc.sinhalasongpredictorbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msc.sinhalasongpredictorbackend.modal.BertPredictionResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPPredictionResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.KeyPair;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NLPService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommonUtil commonUtil;

    @Value("${flask.base.url}")
    private String flaskBaseUrl;
    private static String TFIDF = "TFIDF";
    private static String TRANSFORMER = "TRANSFORMER";
    private static String WORD2VEC = "WORD2VEC";

    public Object predictNlp(NLPRequest nlpRequest, String embedding,Boolean isHybrid) {
        try {


            if (embedding.toUpperCase().equals(TFIDF)) {
                URI uri = new URI(flaskBaseUrl+"/tfidf/svm");
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<NLPRequest> requestEntity = new HttpEntity<>(nlpRequest, headers);
                NLPPredictionResponse response = restTemplate.postForObject(uri, requestEntity, NLPPredictionResponse.class);
                System.out.println("Success Result is:" + response.getPrediction());
                return response.getPrediction();

            } else if(embedding.toUpperCase().equals(WORD2VEC)){
                URI uri = new URI(flaskBaseUrl+"/fasttext");
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<NLPRequest> requestEntity = new HttpEntity<>(nlpRequest, headers);
                NLPPredictionResponse response = restTemplate.postForObject(uri, requestEntity, NLPPredictionResponse.class);
                System.out.println("Success Result is:" + response.getPrediction());
                return response.getPrediction();

            } else if (embedding.toUpperCase().equals(TRANSFORMER)) {

                URI uri = new URI(flaskBaseUrl+"/bert");
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<NLPRequest> requestEntity = new HttpEntity<>(nlpRequest, headers);
                ArrayList<BertPredictionResponse> response = restTemplate.postForObject(uri, requestEntity, ArrayList.class);
                if(isHybrid.equals(Boolean.TRUE)){
                    PredictionResponse predictionResponse = new PredictionResponse();
                    Map<String,String> map = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    for(int i=0; i < response.size();i++){
                        BertPredictionResponse emotion = mapper.convertValue(response.get(i), BertPredictionResponse.class);
                        DecimalFormat df = new DecimalFormat("#.##");
                        map.put(emotion.getLabel(), String.valueOf(df.format(emotion.getScore())));
                    }
                    BertPredictionResponse bestEmotion = findBestEmotion(response);
                    Integer emotionInt = commonUtil.getPredictedInteger(bestEmotion.getLabel());
                    predictionResponse.setPredictedValue(emotionInt);
                    predictionResponse.setPredictedDistribution(map);
                    return predictionResponse;

                } else {
                    BertPredictionResponse bestEmotion = findBestEmotion(response);
                    System.out.println("Best Bert Emotion: " + bestEmotion.getLabel());
                    return bestEmotion.getLabel();

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private BertPredictionResponse findBestEmotion(ArrayList<BertPredictionResponse> emotions) {
        ObjectMapper mapper = new ObjectMapper();
        BertPredictionResponse bestEmotion = mapper.convertValue(emotions.get(0), BertPredictionResponse.class);
        for(int i=0; i < emotions.size();i++){
            BertPredictionResponse emotion = mapper.convertValue(emotions.get(i), BertPredictionResponse.class);
            if (emotion.getScore() > bestEmotion.getScore()) {
                bestEmotion = emotion;
            }
        }

        return bestEmotion;
    }
}
