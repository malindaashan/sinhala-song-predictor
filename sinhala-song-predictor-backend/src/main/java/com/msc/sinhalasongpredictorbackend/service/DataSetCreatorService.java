package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.entity.Prediction;
import com.msc.sinhalasongpredictorbackend.modal.DataSetCreatorRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionDto;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.repository.PredictionRepository;
import com.msc.sinhalasongpredictorbackend.util.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataSetCreatorService {

    @Autowired
    ClassifyService classifyService;

    @Autowired
    ClusterService clusterService;

    @Autowired
    PredictionRepository predictionRepository;

    public Boolean saveExtractFeatures(MultipartFile multipartFile, DataSetCreatorRequest dataSetCreatorRequest) throws Exception {
        Prediction prediction = new Prediction();
        for (Map.Entry<String, Boolean> entry : dataSetCreatorRequest.getSelectedAlgorithms().entrySet()) {
            PredictionResponse predictionResponse;
            if (Constants.SMO_.equals(entry.getKey())) {
                predictionResponse = classifyService.classifyMusic(multipartFile, Constants.SMO_);
                prediction.setSmo(predictionResponse.getPredictedDistribution().toString());

            } else if (Constants.NAIVE_BAYES.equals(entry.getKey())) {
                predictionResponse = classifyService.classifyMusic(multipartFile, Constants.NAIVE_BAYES);
                prediction.setNaiveBayes(predictionResponse.getPredictedDistribution().toString());

            } else if (Constants.RANDOM_FOREST.equals(entry.getKey())) {
                predictionResponse = classifyService.classifyMusic(multipartFile, Constants.RANDOM_FOREST);
                prediction.setRandomForest(predictionResponse.getPredictedDistribution().toString());

            } else if (Constants.K_MEANS.equals(entry.getKey())) {
                predictionResponse = clusterService.predictCluster(multipartFile, Constants.K_MEANS);
                prediction.setKMeans(predictionResponse.getPredictedValue().toString());

            } else if (Constants.H_CLUSTER.equals(entry.getKey())) {
                predictionResponse = clusterService.predictCluster(multipartFile, Constants.H_CLUSTER);
                prediction.setHCluster(predictionResponse.getPredictedValue().toString());
            }
        }
        prediction.setDataset(1);
        prediction.setFileName(multipartFile.getOriginalFilename());
        predictionRepository.save(prediction);
        return true;
    }

    public Object getPredictionPaginatedData(Integer size, Integer page) {
        List<Prediction> predictionList = new ArrayList<>();
        predictionRepository.findALlByOrderByIdAsc(PageRequest.of(page, size))
                .forEach(predictionList::add);
        return getPredictionDtoList(predictionList);
    }

    private List<PredictionDto> getPredictionDtoList(List<Prediction> predictionList) {
        return predictionList.stream().map(this::getPredictionDto).toList();
    }

    private PredictionDto getPredictionDto(Prediction prediction) {
        PredictionDto predictionDto = new PredictionDto();
        BeanUtils.copyProperties(prediction, predictionDto);
        return predictionDto;
    }

    public Object getPredictionDataCount() {
        return predictionRepository.count();
    }
}
