package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.FeatureVectorFile;
import com.msc.sinhalasongpredictorbackend.modal.PredictionBulkRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weka.clusterers.Clusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddExpression;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClusterService {

    @Value("${cluster.kmeans.modal.location}")
    private String kMeansModalLocation;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
    private static final String K_MEANS = "K-Means";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private static final String SAD = "Sad";
    private static final String HAPPY = "Happy";
    private static final String CALM = "Calm";

    @Autowired
    CommonUtil commonUtil;

    public PredictionResponse predictCluster(MultipartFile multipartFile, String algorithm) throws Exception {
        //save mp3 file
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
        if (K_MEANS.equalsIgnoreCase(algorithm)) {
            return runKMeans();
        }
        return new PredictionResponse();
    }

    public PredictionResponse predictCluster(File file, String algorithm) throws Exception {
        //save mp3 file
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
        if (K_MEANS.equalsIgnoreCase(algorithm)) {
            return runKMeans();
        }
        return new PredictionResponse();
    }

    private PredictionResponse runKMeans() throws Exception {
        Clusterer clusterer = (Clusterer) SerializationHelper.read(kMeansModalLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {

            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            Instances selectedDataset= getInstancesWithSpecificHeaders(trainingDataSet);

            for (Instance i : selectedDataset) {
                int result = clusterer.clusterInstance(i);
                PredictionResponse predictionResponse = new PredictionResponse();
                predictionResponse.setPredictedValue(result);
//                double[] doubleDistributionArray = clusterer.distributionForInstance(i);
//                predictionResponse.setPredictedDistribution(getDistributionMap(doubleDistributionArray));
                return predictionResponse;
            }

            return new PredictionResponse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    public void clusterAndSaveBulk(PredictionBulkRequest predictionBulkRequest) {
        try {
            File files = new File(predictionBulkRequest.getBulkAudioPath());
            File csvFile = new File(predictionBulkRequest.getSavePath());
            FileWriter outputfile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] headers = {"path ", "result"};
            writer.writeNext(headers);
            for (File file : files.listFiles()) {
                PredictionResponse predictionResponse = predictCluster(file, predictionBulkRequest.getAlgorithm());
                String[] out = {file.getName(), getPredictedClusterString(predictionResponse.getPredictedValue())};
                System.out.println(Arrays.toString(out));
                writer.writeNext(out);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Occurred while executing extractJAudioFeaturesFromBulkAudioPath");
            e.printStackTrace();
        }
    }

    public static String getPredictedClusterString(Integer predictedValue){
        if(predictedValue ==1){
            return SAD;
        } else if(predictedValue ==2){
            return HAPPY;
        } else if (predictedValue ==0){
            return CALM;
        } else {
            return "ERROR";
        }
    }
    public Instances getInstancesWithSpecificHeaders(Instances instances) throws Exception {

        int[] indicesToKeep = {6,14,19,21,26,28,29,34,38,47};

        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(indicesToKeep);
        removeFilter.setInvertSelection(true);
        removeFilter.setInputFormat(instances);

        return Filter.useFilter(instances, removeFilter);


    }
}
