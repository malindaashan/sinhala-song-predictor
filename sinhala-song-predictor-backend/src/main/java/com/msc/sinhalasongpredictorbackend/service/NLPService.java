package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import org.springframework.stereotype.Service;

@Service
public class NLPService {

    public String predictNlp(NLPRequest nlpRequest, String embedding){
        return nlpRequest.getText();

    }
}
