package com.msc.sinhalasongpredictorbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cluster")
public class ClusterContorller {

    @GetMapping("predict-cluster")
    public String predictCluster(@RequestParam String algorithm){

        return "Done";

    }
}
