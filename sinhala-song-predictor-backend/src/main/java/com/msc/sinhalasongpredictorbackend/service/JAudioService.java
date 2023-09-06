package com.msc.sinhalasongpredictorbackend.service;

import com.msc.sinhalasongpredictorbackend.modal.Feature;
import com.msc.sinhalasongpredictorbackend.modal.FeatureVectorFile;
import com.msc.sinhalasongpredictorbackend.util.CommonUtil;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class JAudioService {

    @Autowired
    CommonUtil commonUtil;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;

    public void extractJAudioFeatures(String path) throws Exception {
        File[] fileList = new File(path).listFiles();
        int index = 0;
        File fileOut = new File(csvFeatureOutput);
        // create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(fileOut);

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);
        for (File file : fileList) {
            commonUtil.extractFeatures(file.getName());
            FeatureVectorFile featureVectorFile = commonUtil.readAudioFeatureXml();
            createMergedCsv(featureVectorFile, index, writer);
            index++;

        }
        writer.close();
    }

    private void createMergedCsv(FeatureVectorFile featureVectorFile, int index, CSVWriter writer) throws IOException {
        if (index == 0) {
            String[] header = {"SpectralCentroidOverallStandardDeviation", "SpectralRolloffPointOverallStandardDeviation",
                    "SpectralFluxOverallStandardDeviation", "CompactnessOverallStandardDeviation", "SpectralVariabilityOverallStandardDeviation",
                    "RootMeanSquareOverallStandardDeviation", "FractionOfLowEnergyWindowsOverallStandardDeviation", "ZeroCrossingsOverallStandardDeviation",
                    "StrongestBeatOverallStandardDeviation", "BeatSumOverallStandardDeviation", "StrengthOfStrongestBeatOverallStandardDeviation", "LPCOverallStandardDeviation/v/0",
                    "LPCOverallStandardDeviation/v/1", "LPCOverallStandardDeviation/v/2", "LPCOverallStandardDeviation/v/3", "LPCOverallStandardDeviation/v/4",
                    "LPCOverallStandardDeviation/v/5", "LPCOverallStandardDeviation/v/6", "LPCOverallStandardDeviation/v/7", "LPCOverallStandardDeviation/v/8",
                    "LPCOverallStandardDeviation/v/9", "MethodofMomentsOverallStandardDeviation/v/0", "MethodofMomentsOverallStandardDeviation/v/1",
                    "MethodofMomentsOverallStandardDeviation/v/2", "MethodofMomentsOverallStandardDeviation/v/3", "MethodofMomentsOverallStandardDeviation/v/4",
                    "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/0", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/1",
                    "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/2", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/3",
                    "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/4", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/5", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/6",
                    "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/7", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/8",
                    "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/9", "SpectralCentroidOverallAverage", "SpectralRolloffPointOverallAverage",
                    "SpectralFluxOverallAverage", "CompactnessOverallAverage", "SpectralVariabilityOverallAverage", "RootMeanSquareOverallAverage",
                    "FractionOfLowEnergyWindowsOverallAverage", "ZeroCrossingsOverallAverage", "StrongestBeatOverallAverage", "BeatSumOverallAverage",
                    "StrengthOfStrongestBeatOverallAverage", "LPCOverallAverage/v/0", "LPCOverallAverage/v/1", "LPCOverallAverage/v/2", "LPCOverallAverage/v/3",
                    "LPCOverallAverage/v/4", "LPCOverallAverage/v/5", "LPCOverallAverage/v/6", "LPCOverallAverage/v/7", "LPCOverallAverage/v/8",
                    "LPCOverallAverage/v/9", "MethodofMomentsOverallAverage/v/0", "MethodofMomentsOverallAverage/v/1", "MethodofMomentsOverallAverage/v/2",
                    "MethodofMomentsOverallAverage/v/3", "MethodofMomentsOverallAverage/v/4", "AreaMethodofMomentsofMFCCsOverallAverage/v/0",
                    "AreaMethodofMomentsofMFCCsOverallAverage/v/1", "AreaMethodofMomentsofMFCCsOverallAverage/v/2", "AreaMethodofMomentsofMFCCsOverallAverage/v/3",
                    "AreaMethodofMomentsofMFCCsOverallAverage/v/4", "AreaMethodofMomentsofMFCCsOverallAverage/v/5", "AreaMethodofMomentsofMFCCsOverallAverage/v/6",
                    "AreaMethodofMomentsofMFCCsOverallAverage/v/7", "AreaMethodofMomentsofMFCCsOverallAverage/v/8", "AreaMethodofMomentsofMFCCsOverallAverage/v/9"};
            writer.writeNext(header);
        }
        ArrayList<String> data = new ArrayList<String>();
        for (Feature feature : featureVectorFile.getDataSet().getFeatureList()) {
            for (String value : feature.getValues()) {

                data.add(value);
            }
        }
        String[] dataArray = data.toArray(new String[0]);

        writer.writeNext(dataArray);
    }

    public void extractJAudioFeaturesFromBulkAudioPath(String path) {
        try {
            File files = new File(path);
            File csvFile = new File(csvFeatureOutput);
            FileWriter outputfile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeNext(commonUtil.getCSVHeaders());
            for (File file : files.listFiles()) {
                System.out.println("Processing " + file.getName());
                //extract features
                commonUtil.extractFeatures(file);
                //convert features to csv
                FeatureVectorFile featureVectorFile = commonUtil.readAudioFeatureXml();
                writer.writeNext(commonUtil.getFeatureData(featureVectorFile).toArray(new String[0]));
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Occurred while executing extractJAudioFeaturesFromBulkAudioPath");
            e.printStackTrace();
        }
    }
}
