package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weka.clusterers.Clusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.util.HashMap;

@Service
public class ClusterService {

    @Value("${cluster.kmeans.modal.location}")
    private String kMeansModalLocation;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
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

    private void runKMeans() throws Exception {
        Clusterer clusterer = (Clusterer) SerializationHelper.read(kMeansModalLocation);
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFeatureOutput));

        Instances trainingDataSet = loader.getDataSet();

        for (Instance i : trainingDataSet) {
            int result = clusterer.clusterInstance(i);
            //countMap.put(result, countMap.getOrDefault(result, 0) + 1);
            System.out.println(result);
            System.out.println(i);
        }

    }
}
