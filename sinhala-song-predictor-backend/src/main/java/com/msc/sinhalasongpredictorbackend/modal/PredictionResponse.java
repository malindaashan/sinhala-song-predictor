package com.msc.sinhalasongpredictorbackend.modal;

import lombok.Data;

import java.util.Map;

@Data
public class PredictionResponse {
    private Integer predictedValue;
    private Map<String, String> predictedDistribution;
}
