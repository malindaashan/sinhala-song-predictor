package com.msc.sinhalasongpredictorbackend.modal;

import lombok.Data;

@Data
public class PredictionBulkRequest {
    String bulkAudioPath;
    String savePath;
    String algorithm;
}
