package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import com.msc.sinhalasongpredictorbackend.service.NLPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nlp")
public class NLPController {

    @Autowired
    NLPService nlpService;

    @PostMapping("/predict")
    public ApiResponse predictNlp(@RequestBody NLPRequest nlpRequest, @RequestParam("embedding") String embedding) {
        System.out.println("Exectured predictNLP with request " + nlpRequest.getText()+"embedding:"+embedding);
        return new ApiResponse(nlpService.predictNlp(nlpRequest, embedding, Boolean.FALSE));

    }
}
