package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.FeatureVectorFile;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.clusterers.Clusterer;
import weka.core.*;
import weka.core.converters.CSVLoader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassifyService {

    @Value("${classify.randomforest.modal.location}")
    private String randomForestLocation;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
    private static final String RANDOM_FOREST = "Random-Forest";
    @Autowired
    CommonUtil commonUtil;
    public Object classifyMusic(MultipartFile multipartFile, String algorithm) throws Exception {
        commonUtil.saveMP3File(multipartFile);


        //trim and convert mp3 file wav
        commonUtil.convertMP3Wav(multipartFile.getOriginalFilename());

        //trim mp3
        commonUtil.trimMP3(multipartFile.getOriginalFilename());

        //extract features
        commonUtil.extractFeatures(multipartFile.getOriginalFilename());

        //convert features to csv
        FeatureVectorFile featureVectorFile = commonUtil.readAudioFeatureXml();
        commonUtil.createCsv(featureVectorFile);
        if (RANDOM_FOREST.equalsIgnoreCase(algorithm)) {
            return runRandomForest();
        }
        return -1;
    }

    private Object runRandomForest() throws Exception {
        Classifier randomForest= (Classifier) SerializationHelper.read(randomForestLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();

            List values = new ArrayList();
            values.add("1.0");
            trainingDataSet.insertAttributeAt(new Attribute("label", values), trainingDataSet.numAttributes());
            trainingDataSet.setClassIndex(trainingDataSet.numAttributes()-1);

            for (Instance i : trainingDataSet) {
                Double result = randomForest.classifyInstance(i);
                return result.intValue();
            }

            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
