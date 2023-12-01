package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view-prediction")
public class ViewPredictionController {

    @Autowired
    DatabaseService databaseService;

    @GetMapping("/find-all")
    public ApiResponse getAllPredictions() {
        try {
            return new ApiResponse(databaseService.findAllPredictions());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
