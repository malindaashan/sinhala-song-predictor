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
    public List<PredictionResponse> predictHybrid(NLPRequest nlpRequest, MultipartFile multipartFile) throws Exception {
        List<PredictionResponse> predictionResponseList = new ArrayList<>();
        PredictionResponse predictionResponseMl = classifyService.classifyMusic(multipartFile,RANDOM_FOREST);
        ArrayList bertPredictionResponse = (ArrayList) nlpService.predictNlp(nlpRequest,TRANSFORMER,Boolean.TRUE);
        predictionResponseList.add(predictionResponseMl);
        return predictionResponseList;

    }
}
