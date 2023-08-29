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
    //extract features of audios which are residing in specific path
    @PostMapping("/extract-features-bulk")
    public void extractJAudioFeaturesSeparated(@RequestBody JAudioBulkRequest body) {
        try{
            jAudioService.extractJAudioFeaturesFromBulkAudioPath(body.getPath());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
