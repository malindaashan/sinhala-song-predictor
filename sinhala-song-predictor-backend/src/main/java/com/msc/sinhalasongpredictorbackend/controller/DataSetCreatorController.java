package com.msc.sinhalasongpredictorbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msc.sinhalasongpredictorbackend.modal.ApiResponse;
import com.msc.sinhalasongpredictorbackend.modal.DataSetCreatorRequest;
import com.msc.sinhalasongpredictorbackend.service.DataSetCreatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/dataset-creator")
public class DataSetCreatorController {

    @Autowired
    DataSetCreatorService dataSetCreatorService;

    @PostMapping(value = "/save-extract-features", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse saveExtractFeatures(HttpServletRequest request, @RequestPart("file") MultipartFile multipartFile) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Boolean> map = objectMapper.readValue(request.getParameter("algorithm"), Map.class);

            DataSetCreatorRequest dataSetCreatorRequest = new DataSetCreatorRequest();
            dataSetCreatorRequest.setSelectedAlgorithms(map);
            return new ApiResponse(dataSetCreatorService.saveExtractFeatures(multipartFile, dataSetCreatorRequest));

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find-by-paginated")
    public ApiResponse getFeatureDataPaginated(@RequestParam("size") Integer size, @RequestParam("page") Integer page){
        try {
            return new ApiResponse(dataSetCreatorService.getPredictionPaginatedData(size, page));
        } catch (Exception e){
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ApiResponse getDataCount() {
        try{
            return new ApiResponse(dataSetCreatorService.getPredictionDataCount());
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
