package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.modal.NLPRequest;
import com.msc.sinhalasongpredictorbackend.service.HybridService;
import com.msc.sinhalasongpredictorbackend.service.NLPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hybrid")
public class HybridController {

    @Autowired
    HybridService hybridService;

    @PostMapping(value = "/predict",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse predictHybrid(@RequestBody NLPRequest nlpRequest,
                                     @RequestParam("file") MultipartFile multipartFile) {
        System.out.println("Executed predictHybrid with request " + nlpRequest.getText());
        try{
            return new ApiResponse(hybridService.predictHybrid(nlpRequest,multipartFile));
        } catch(Exception e){
            e.printStackTrace();
            return new ApiResponse("Error predictHybrid",e);
        }

    }
}
