package com.msc.sinhalasongpredictorbackend.controller;

import com.msc.sinhalasongpredictorbackend.modal.JAudioBulkRequest;
import com.msc.sinhalasongpredictorbackend.service.JAudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jaudio")
public class JAudioController {
    @Autowired
    JAudioService jAudioService;

    @PostMapping("/extract-features")
    public void extractJAudioFeatures(@RequestBody JAudioBulkRequest body) {
        try{
            jAudioService.extractJAudioFeatures(body.getPath());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
