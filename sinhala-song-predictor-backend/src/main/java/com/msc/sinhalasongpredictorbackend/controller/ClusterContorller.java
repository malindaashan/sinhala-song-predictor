package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.service.ClusterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cluster")
public class ClusterContorller {

    @Autowired
    ClusterService clusterService;

    @PostMapping(value = "/predict-cluster", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse predictCluster(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                                      @RequestParam("algorithm") String algorithm) {
        try {
            return new ApiResponse(clusterService.predictCluster(multipartFile, algorithm));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
