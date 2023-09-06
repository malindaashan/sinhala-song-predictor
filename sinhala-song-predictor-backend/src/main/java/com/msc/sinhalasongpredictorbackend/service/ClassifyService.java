package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.FeatureVectorFile;
import com.msc.sinhalasongpredictorbackend.modal.PredictionBulkRequest;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

    public Integer classifyMusic(File file, String algorithm) throws Exception {
        commonUtil.saveMP3File(file);


        //trim and convert mp3 file wav
        commonUtil.convertMP3Wav(file.getName());

        //trim mp3
        commonUtil.trimMP3(file.getName());

        //extract features
        commonUtil.extractFeatures(file.getName());

        //convert features to csv
        FeatureVectorFile featureVectorFile = commonUtil.readAudioFeatureXml();
        commonUtil.createCsv(featureVectorFile);
        if (RANDOM_FOREST.equalsIgnoreCase(algorithm)) {
            return runRandomForest();
        }
        return -1;
    }


    private Integer runRandomForest() throws Exception {
        Classifier randomForest = (Classifier) SerializationHelper.read(randomForestLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();

            List values = new ArrayList();
            values.add("1.0");
            trainingDataSet.insertAttributeAt(new Attribute("label", values), trainingDataSet.numAttributes());
            trainingDataSet.setClassIndex(trainingDataSet.numAttributes() - 1);

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

    public void classifyAndSaveBulk(PredictionBulkRequest predictionBulkRequest) {
        try {
            File files = new File(predictionBulkRequest.getBulkAudioPath());
            File csvFile = new File(predictionBulkRequest.getSavePath());
            FileWriter outputfile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] headers = {"path ", "result"};
            writer.writeNext(headers);
            for (File file : files.listFiles()) {
                String result = classifyMusic(file, predictionBulkRequest.getAlgorithm()).toString();
                String[] names = {file.getName(), "0".equals(result) ? "Calm" : "1".equals(result) ? "Happy" : "Sad"};
                writer.writeNext(names);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Occurred while executing extractJAudioFeaturesFromBulkAudioPath");
            e.printStackTrace();
        }

    }
}
