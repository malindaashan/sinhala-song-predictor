package com.msc.sinhalasongpredictorbackend.modal;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class PredictionDto {

    private Long id;
    private String fileName;
    private String filePath;
    private Integer dataset;
    private String featureSelection;
    private String smo;
    private String naiveBayes;
    private String randomForest;
    private String kMeans;
    private String hCluster;

}
