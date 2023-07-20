package com.msc.sinhalasongpredictorbackend.service;

import org.springframework.stereotype.Service;

@Service
public class ClusterService {
    private static final String K_MEANS= "K-Means";
    private static final String K_MEANS_MODEL_SAVE_PATH="C:\\Users\\MalindaPieris\\Documents\\MscResearch\\Sinhala-Audio-Classfication-notebooks\\notebooks\\models\\k-means\\model-kmeans.model";

    public String predictCluster(String algorithm){
        if(K_MEANS.equalsIgnoreCase(algorithm)){
            runKMeans();
        }
        return "Done";
    }

    private void runKMeans() {
    }
}
