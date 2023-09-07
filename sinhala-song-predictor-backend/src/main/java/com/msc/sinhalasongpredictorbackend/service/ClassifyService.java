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
import weka.classifiers.meta.Bagging;
import weka.core.*;
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

    @Value("${classify.naivebayes.modal.location}")
    private String naiveBayesLocation;

    @Value("${classify.smo.modal.location}")
    private String smoLocation;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;
    private static final String RANDOM_FOREST = "Random-Forest";

    private static final String NAIVE_BAYES = "Naive-Bayes";

    private static final String SMO = "SMO";
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
        } else if (SMO.equals(algorithm)){
            return runSMO();
        } else if(NAIVE_BAYES.equals(algorithm)){
            return  runNaiveBayes();
        }
        return -1;
    }


    private Integer runRandomForest() throws Exception {
        Classifier randomForest = (Classifier) SerializationHelper.read(randomForestLocation);
        CSVLoader loader = new CSVLoader();
        try (InputStream fis = new FileInputStream(csvFeatureOutput)) {
            loader.setSource(fis);

            Instances trainingDataSet = loader.getDataSet();

            ArrayList labels = new ArrayList();
            labels.add("0.0");
            labels.add("1.0");
            labels.add("2.0");
            Attribute attributeCls = new Attribute("label",labels);

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

            Double prediction = randomForest.classifyInstance(instance);
            System.out.println(prediction);
            return prediction.intValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    private Integer runSMO() throws Exception {
        Classifier randomForest = (Classifier) SerializationHelper.read(smoLocation);
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

    private Integer runNaiveBayes() throws Exception {
        Classifier randomForest = (Classifier) SerializationHelper.read(naiveBayesLocation);
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
    private double[] getInstances(Instances dataset) {
        int n = dataset.numAttributes();
        double [] v = new double[ n ];
        for( int i = 0; i < dataset.size(); i++ ) {
            Instance instance = dataset.get(i);
            for( int j = 0; j < n; j++ ) {
                v[ j ] = instance.value(j);
            }
            return v ;
        }
        return null;
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
