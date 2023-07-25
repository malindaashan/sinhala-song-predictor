package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClusterService {
    private static final String K_MEANS = "K-Means";
    private static final String K_MEANS_MODEL_SAVE_PATH = "C:\\Users\\MalindaPieris\\Documents\\MscResearch\\Sinhala-Audio-Classfication-notebooks\\notebooks\\models\\k-means\\model-kmeans.model";

    @Autowired
    CommonUtil commonUtil;

    public String predictCluster(MultipartFile multipartFile, String algorithm) throws Exception {
        //save mp3 file
        commonUtil.saveMP3File(multipartFile);


        //trim and convert mp3 file wav
        commonUtil.convertMP3Wav(multipartFile.getOriginalFilename());

        //trim mp3
        commonUtil.trimMP3(multipartFile.getOriginalFilename());

        //extract features
        commonUtil.extractFeatures(multipartFile.getOriginalFilename());

        //convert features to csv
        commonUtil.readAudioFeatureXml();
        if (K_MEANS.equalsIgnoreCase(algorithm)) {
            runKMeans();
        }
        return "Done";
    }

    private void runKMeans() {
    }
}
