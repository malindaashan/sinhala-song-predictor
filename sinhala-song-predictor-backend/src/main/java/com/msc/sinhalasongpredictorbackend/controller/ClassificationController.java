package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.service.ClassifyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/classify")
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
}
