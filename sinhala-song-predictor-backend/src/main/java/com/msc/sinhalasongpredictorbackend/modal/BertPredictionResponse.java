package com.msc.sinhalasongpredictorbackend.modal;

import lombok.Data;

@Data
public class BertPredictionResponse {
    private String label;
    private Double score;

}
