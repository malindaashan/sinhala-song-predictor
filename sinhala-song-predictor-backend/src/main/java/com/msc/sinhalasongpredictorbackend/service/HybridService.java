package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.BertPredictionResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class HybridService {

    @Autowired
    ClassifyService classifyService;

    @Autowired
    NLPService nlpService;
    private static final String RANDOM_FOREST = "Random-Forest";

    private static String TRANSFORMER = "TRANSFORMER";
    public List<PredictionResponse> predictHybrid(String text, MultipartFile multipartFile,String algorithm, String embedding) throws Exception {
        List<PredictionResponse> predictionResponseList = new ArrayList<>();
        PredictionResponse predictionResponseMl = classifyService.classifyMusic(multipartFile,algorithm);
        NLPRequest nlpRequest = new NLPRequest();
        nlpRequest.setText(text);
        PredictionResponse predictionResponse = (PredictionResponse) nlpService.predictNlp(nlpRequest,embedding,Boolean.TRUE);

        predictionResponseList.add(predictionResponseMl);
        predictionResponseList.add(predictionResponse);

        return predictionResponseList;

    }
}
