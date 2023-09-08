package com.msc.sinhalasongpredictorbackend.modal;

import lombok.Data;

import java.util.Map;

@Data
public class DataSetCreatorRequest {
private Map<String,Boolean> selectedAlgorithms;
}
