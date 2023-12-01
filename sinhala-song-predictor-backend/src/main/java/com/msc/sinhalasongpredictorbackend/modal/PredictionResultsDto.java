package com.msc.sinhalasongpredictorbackend.modal;

import lombok.Data;

@Data
public class PredictionResultsDto {

    private Long id;
    private String predLabel;
    private Double score;
    private String timestamp;
    private String mlDistribution;
    private String fileName;
    private String nlpDistribution;

}
