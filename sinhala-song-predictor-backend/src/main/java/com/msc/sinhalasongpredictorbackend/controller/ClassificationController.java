package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.modal.JAudioBulkRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionBulkRequest;
import com.msc.sinhalasongpredictorbackend.service.ClassifyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/classify")
@Slf4j
public class ClassificationController {

    @Autowired
    ClassifyService classifyService;

    @PostMapping(value = "/classify-ml", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse classifyRForest(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                                       @RequestParam("algorithm") String algorithm){
        try {
            return new ApiResponse(classifyService.classifyMusic(multipartFile, algorithm));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/classify-ml-bulk")
    public void classifyAndSaveBulk(@RequestBody PredictionBulkRequest predictionBulkRequest) {
        try{
            classifyService.classifyAndSaveBulk(predictionBulkRequest);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
