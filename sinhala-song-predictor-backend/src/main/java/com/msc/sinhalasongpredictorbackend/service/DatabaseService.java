package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.entity.HybridPredResult;
import com.msc.sinhalasongpredictorbackend.entity.MLPredResult;
import com.msc.sinhalasongpredictorbackend.entity.NLPPredResult;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResultsDto;
import com.msc.sinhalasongpredictorbackend.repository.HybridPredResultRepository;
import com.msc.sinhalasongpredictorbackend.repository.MLPredResultRepository;
import com.msc.sinhalasongpredictorbackend.repository.NLPPredResultRepository;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

@Service
public class DatabaseService {

    @Autowired
    HybridPredResultRepository hybridPredResultRepository;

    @Autowired
    MLPredResultRepository mlPredResultRepository;

    @Autowired
    NLPPredResultRepository nlpPredResultRepository;

    @Autowired
    CommonUtil commonUtil;
    public void saveResults(List<PredictionResponse> predictionResponseList, String text, String fileName, String algorithm, String embedding){
        MLPredResult mlPredResult = new MLPredResult();

        mlPredResult.setModel(algorithm);
        mlPredResult.setFeatureSelection("JAUDIO");
        mlPredResult.setFileName(fileName);
        mlPredResult.setDistribution(predictionResponseList.get(0).getPredictedDistribution().toString());
        mlPredResult.setPredLabel(predictionResponseList.get(0).getPredictedValue());
        mlPredResultRepository.saveAndFlush(mlPredResult);


        NLPPredResult nlpPredResult = new NLPPredResult();
        nlpPredResult.setLyrics(text);
        nlpPredResult.setPredLabel(predictionResponseList.get(1).getPredictedValue());
        nlpPredResult.setDistribution( predictionResponseList.get(1).getPredictedDistribution().toString());
        nlpPredResult.setWordEmbedding(embedding);
        nlpPredResult.setModel("xlm-roberta-base");
        nlpPredResultRepository.saveAndFlush(nlpPredResult);
        Double mlBestPred;
        Double  mlBestPredIndex;

        Double calmTotDis = Double.valueOf(predictionResponseList.get(0).getPredictedDistribution().get("Calm"))+
                Double.valueOf(predictionResponseList.get(1).getPredictedDistribution().get("Calm"));

        Double happyTotDist = Double.valueOf(predictionResponseList.get(0).getPredictedDistribution().get("Happy"))+
                Double.valueOf(predictionResponseList.get(1).getPredictedDistribution().get("Happy"));

        Double sadTotDist = Double.valueOf(predictionResponseList.get(0).getPredictedDistribution().get("Sad"))+
                Double.valueOf(predictionResponseList.get(1).getPredictedDistribution().get("Sad"));

        Double max = DoubleStream.of(calmTotDis, happyTotDist, sadTotDist)
                .max()
                .getAsDouble();

        HybridPredResult hybridPredResult = new HybridPredResult();

        hybridPredResult.setMlPredResult(mlPredResult);
        hybridPredResult.setScore(max);
        hybridPredResult.setPredLabel(max.equals(calmTotDis)?0 : max.equals(happyTotDist)?1:2);
        hybridPredResult.setNlpPredResult(nlpPredResult);
        hybridPredResult.setTimestamp(String.valueOf(new Date()));
        hybridPredResultRepository.save(hybridPredResult);



    }

    public List<PredictionResultsDto> findAllPredictions() {

        List<HybridPredResult> hybridPredResultList = hybridPredResultRepository.findAll();
        List<PredictionResultsDto> predictionResultsDtoList = new ArrayList<>();

        for(HybridPredResult hybridPredResult:hybridPredResultList){
            PredictionResultsDto predictionResultsDto = new PredictionResultsDto();
            predictionResultsDto.setId(hybridPredResult.getId());
            predictionResultsDto.setScore(hybridPredResult.getScore());
            predictionResultsDto.setPredLabel(commonUtil.getPredictedString(hybridPredResult.getPredLabel()));
            predictionResultsDto.setFileName(hybridPredResult.getMlPredResult().getFileName());
            predictionResultsDto.setMlDistribution(hybridPredResult.getMlPredResult().getDistribution());
            predictionResultsDto.setNlpDistribution(hybridPredResult.getNlpPredResult().getDistribution());
            predictionResultsDto.setTimestamp(hybridPredResult.getTimestamp());
            predictionResultsDtoList.add(predictionResultsDto);

        }
        return predictionResultsDtoList;

    }
}
