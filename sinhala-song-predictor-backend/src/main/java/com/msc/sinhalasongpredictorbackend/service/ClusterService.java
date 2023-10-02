package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.PredictionBulkRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
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
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;

@Service
@Slf4j
public class ClusterService {

    @Value("${cluster.kmeans.modal.location}")
    private String kMeansModalLocation;

    @Value("${cluster.hcluster.modal.location}")
    private String hClusterLocation;
    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
    private static final String K_MEANS = "K-Means";

    private static final String H_CLUSTER = "Hierarchical-Clustering";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private static final String SAD = "Sad";
    private static final String HAPPY = "Happy";
    private static final String CALM = "Calm";

    @Autowired
    CommonUtil commonUtil;

    public PredictionResponse predictCluster(MultipartFile multipartFile, String algorithm) throws Exception {
        commonUtil.preProcessAudio(multipartFile);
        if (K_MEANS.equalsIgnoreCase(algorithm)) {
            return runKMeans();
        } else if (H_CLUSTER.equalsIgnoreCase(algorithm)) {
            return runHCluster();
        }
        return new PredictionResponse();
    }

    public PredictionResponse predictCluster(File file, String algorithm) throws Exception {
        commonUtil.preProcessAudio(file);
        if (K_MEANS.equalsIgnoreCase(algorithm)) {
            return runKMeans();
        } else if (H_CLUSTER.equalsIgnoreCase(algorithm)) {
            return runHCluster();
        }
        return new PredictionResponse();
    }

    private PredictionResponse runKMeans() throws Exception {
        Clusterer clusterer = (Clusterer) SerializationHelper.read(kMeansModalLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {

            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            Instances selectedDataset = getInstancesWithSpecificHeaders(trainingDataSet);

            for (Instance i : selectedDataset) {
                int result = clusterer.clusterInstance(i);
                PredictionResponse predictionResponse = new PredictionResponse();
                predictionResponse.setPredictedValue(result);
                return predictionResponse;
            }

            return new PredictionResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("run KMeans Error:{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    private PredictionResponse runHCluster() throws Exception {
        Clusterer clusterer = (Clusterer) SerializationHelper.read(hClusterLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {

            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            Instances selectedDataset = getInstancesWithSpecificHeaders(trainingDataSet);

            for (Instance i : selectedDataset) {
                int result = clusterer.clusterInstance(i);
                PredictionResponse predictionResponse = new PredictionResponse();
                predictionResponse.setPredictedValue(result);
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

    public static String getPredictedClusterString(Integer predictedValue) {
        if (predictedValue == 1) {
            return SAD;
        } else if (predictedValue == 2) {
            return HAPPY;
        } else if (predictedValue == 0) {
            return CALM;
        } else {
            return "ERROR";
        }
    }

    public Instances getInstancesWithSpecificHeaders(Instances instances) throws Exception {

        int[] indicesToKeep = {6, 14, 19, 21, 26, 28, 29, 34, 38, 47};

        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(indicesToKeep);
        removeFilter.setInvertSelection(true);
        removeFilter.setInputFormat(instances);

        return Filter.useFilter(instances, removeFilter);


    }
}
