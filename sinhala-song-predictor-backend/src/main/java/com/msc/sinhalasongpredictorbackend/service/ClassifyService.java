package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.PredictionBulkRequest;
import com.msc.sinhalasongpredictorbackend.modal.PredictionResponse;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class ClassifyService {

    @Value("${classify.randomforest.modal.location}")
    private String randomForestLocation;

    @Value("${classify.naivebayes.modal.location}")
    private String naiveBayesLocation;

    @Value("${classify.smo.modal.location}")
    private String smoLocation;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
    private static final String RANDOM_FOREST = "Random-Forest";

    private static final String NAIVE_BAYES = "Naive-Bayes";

    private static final String SMO_ = "SMO";

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Autowired
    CommonUtil commonUtil;

    public PredictionResponse classifyMusic(MultipartFile multipartFile, String algorithm) throws Exception {
        commonUtil.preProcessAudio(multipartFile);
        if (RANDOM_FOREST.equalsIgnoreCase(algorithm)) {
            return runRandomForest();
        } else if (SMO_.equals(algorithm)) {
            return runSMO();
        } else if (NAIVE_BAYES.equals(algorithm)) {
            return runNaiveBayes();
        }
        return new PredictionResponse();
    }

    public PredictionResponse classifyMusic(File file, String algorithm) throws Exception {
        commonUtil.preProcessAudio(file);
        if (RANDOM_FOREST.equalsIgnoreCase(algorithm)) {
            return runRandomForest();
        } else if (SMO_.equals(algorithm)) {
            return runSMO();
        } else if (NAIVE_BAYES.equals(algorithm)) {
            return runNaiveBayes();
        }
        return new PredictionResponse();
    }


    private PredictionResponse runRandomForest() throws Exception {
        RandomForest randomForest = (RandomForest) SerializationHelper.read(randomForestLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            trainingDataSet = getInstancesWithSpecificHeaders(trainingDataSet);
            ArrayList labels = new ArrayList();
            labels.add("0.0");
            labels.add("1.0");
            labels.add("2.0");
            Attribute attributeCls = new Attribute("label", labels);

            ArrayList<Attribute> attributeArrayList = new ArrayList<>();
            for (int i = 0; i < trainingDataSet.numAttributes(); i++) {
                Attribute attribute = trainingDataSet.attribute(i);
                attributeArrayList.add(attribute);
            }
            attributeArrayList.add(attributeCls);
            double[] arrayDouble = getInstances(trainingDataSet);

            Instances dataset = new Instances("testdata", attributeArrayList, 0);

            DenseInstance instance = new DenseInstance(1.0, arrayDouble);
            instance.setDataset(dataset);
            dataset.setClassIndex(dataset.numAttributes() - 1);

            Double result = randomForest.classifyInstance(instance);
            double[] prediction = randomForest.distributionForInstance(instance);

            PredictionResponse predictionResponse = new PredictionResponse();
            predictionResponse.setPredictedValue(result.intValue());
            predictionResponse.setPredictedDistribution(getDistributionMap(prediction));

            System.out.println(result);
            System.out.println(Arrays.toString(prediction));
            return predictionResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private PredictionResponse runSMO() throws Exception {
        SMO smo = (SMO) SerializationHelper.read(smoLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            trainingDataSet = getInstancesWithSpecificHeaders(trainingDataSet);
            List values = new ArrayList();
            values.add("1.0");
            trainingDataSet.insertAttributeAt(new Attribute("label", values), trainingDataSet.numAttributes());
            trainingDataSet.setClassIndex(trainingDataSet.numAttributes() - 1);
            for (Instance i : trainingDataSet) {
                Double result = smo.classifyInstance(i);
                double[] prediction = smo.distributionForInstance(i);
                PredictionResponse predictionResponse = new PredictionResponse();
                predictionResponse.setPredictedValue(result.intValue());
                predictionResponse.setPredictedDistribution(getDistributionMap(prediction));

                System.out.println(result);
                System.out.println(Arrays.toString(prediction));
                return predictionResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new PredictionResponse();
    }

    private PredictionResponse runNaiveBayes() throws Exception {
        NaiveBayes naiveBayes = (NaiveBayes) SerializationHelper.read(naiveBayesLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();
            trainingDataSet = getInstancesWithSpecificHeaders(trainingDataSet);
            List values = new ArrayList();
            values.add("1.0");
            trainingDataSet.insertAttributeAt(new Attribute("label", values), trainingDataSet.numAttributes());
            trainingDataSet.setClassIndex(trainingDataSet.numAttributes() - 1);

            for (Instance i : trainingDataSet) {
                Double result = naiveBayes.classifyInstance(i);
                double[] prediction = naiveBayes.distributionForInstance(i);
                PredictionResponse predictionResponse = new PredictionResponse();
                predictionResponse.setPredictedValue(result.intValue());
                predictionResponse.setPredictedDistribution(getDistributionMap(prediction));

                System.out.println(result);
                System.out.println(Arrays.toString(prediction));
                return predictionResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new PredictionResponse();
    }

    private Map getDistributionMap(double[] prediction) {
        Map map = new HashMap();
        map.put("Calm", df.format(prediction[0]));
        map.put("Happy", df.format(prediction[1]));
        map.put("Sad", df.format(prediction[2]));
        return map;
    }

    private double[] getInstances(Instances dataset) {
        int n = dataset.numAttributes();
        double[] v = new double[n];
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.get(i);
            for (int j = 0; j < n; j++) {
                v[j] = instance.value(j);
            }
            return v;
        }
        return null;
    }

    public Instances getInstancesWithSpecificHeaders(Instances instances) throws Exception {

        int[] indicesToKeep = {6, 14, 19, 21, 26, 28, 29, 34, 38, 47};

        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(indicesToKeep);
        removeFilter.setInvertSelection(true);
        removeFilter.setInputFormat(instances);

        return Filter.useFilter(instances, removeFilter);


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
                PredictionResponse predictionResponse = classifyMusic(file, predictionBulkRequest.getAlgorithm());
                String[] names = {file.getName(), commonUtil.getPredictedString(predictionResponse.getPredictedValue())};
                writer.writeNext(names);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Occurred while executing extractJAudioFeaturesFromBulkAudioPath");
            e.printStackTrace();
        }

    }
}
