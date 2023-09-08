package com.msc.sinhalasongpredictorbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prediction")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
