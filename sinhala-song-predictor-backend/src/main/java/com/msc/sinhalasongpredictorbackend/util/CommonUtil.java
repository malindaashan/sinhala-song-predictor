package com.msc.sinhalasongpredictorbackend.util;

import com.msc.sinhalasongpredictorbackend.modal.Feature;
import com.msc.sinhalasongpredictorbackend.modal.FeatureVectorFile;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


@Component
@Slf4j
public class CommonUtil {

    @Value("${file.original.save.location}")
    private String originalFileSavePath;

    @Value("${file.converted.save.location}")
    private String convertedFileSavePath;

    @Value("${file.trim.save.location}")
    private String trimFileSavePath;

    @Value("${file.output.save.location}")
    private String outputFileSavePath;

    @Value("${jaudio.jar.path}")
    private String jAudioJarPath;

    @Value("${jaudio.sample.batch.xml}")
    private String jAudioSampleBatchXml;

    @Value("${jaudio.feature.output.xml}")
    private String jAudioFeaturesXml;

    @Value("${feature.csv.output}")
    private String csvFeatureOutput;

    private static final String SAD = "Sad";
    private static final String HAPPY = "Happy";
    private static final String CALM = "Calm";


    public void preProcessAudio(File file) throws Exception {
        saveMP3File(file);

        //trim and convert mp3 file wav
        convertMP3Wav(file.getName());

        //trim mp3
        trimMP3(file.getName());

        //extract features
        extractFeatures(file.getName());

        //convert features to csv
        FeatureVectorFile featureVectorFile = readAudioFeatureXml();
        createCsv(featureVectorFile);

    }

    public void preProcessAudio(MultipartFile multipartFile) throws Exception {
        saveMP3File(multipartFile);

        //trim and convert mp3 file wav
        convertMP3Wav(multipartFile.getOriginalFilename());

        //trim mp3
        trimMP3(multipartFile.getOriginalFilename());

        //extract features
        extractFeatures(multipartFile.getOriginalFilename());

        //convert features to csv
        FeatureVectorFile featureVectorFile = readAudioFeatureXml();
        createCsv(featureVectorFile);
    }

    public void trimMP3(String fileName) throws Exception {
        //ffmpeg -ss 00:00:00.000 -i "output3.wav" -t 90 -map 0 -c copy "output31.wav"
        String inputPath = convertedFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        String outputPath = trimFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        ;
        String cmd1 = "ffmpeg -ss 00:00:00.000 -y -i " + inputPath + " -t 90 -map 0 -c copy " + outputPath;
        Process ps1 = Runtime.getRuntime().exec(cmd1);
        log.info(cmd1);
        int exitCode1 = ps1.waitFor();
        java.io.InputStream is1 = ps1.getInputStream();
        if (exitCode1 == 0) {
            log.info("File trim successfully");
        } else {
            log.error("Error Occurred while trim mp3 file. Exit code: " + exitCode1);
        }
        byte b1[] = new byte[is1.available()];
        is1.read(b1, 0, b1.length);
        log.info("log ffmpeg trim conversion");
        log.info(new String(b1));

        //copy final output to specific folder
        String finalOut = outputFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        ;

        Files.copy(Path.of(outputPath), Path.of(finalOut), StandardCopyOption.REPLACE_EXISTING);
    }

    public void convertMP3Wav(String fileName) throws IOException, InterruptedException {
        /*ffmpeg conversion to wav and khz conversion*/
        //String cmd1 = "ffmpeg -i \"C:\\Users\\MalindaPieris\\Downloads\\Kavikariye - Bathiya N Santhush.mp3\" -acodec pcm_s16le -ac 2 -ar 48000 output6.wav";
        String inputPath = originalFileSavePath + File.separator + fileName.replaceAll("\\s", "");
        String outputPath = convertedFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        ;
        String cmd1 = "ffmpeg -y -i " + inputPath + " -acodec pcm_s16le -ac 2 -ar 48000 " + outputPath;
        Process ps1 = Runtime.getRuntime().exec(cmd1);
        log.info(cmd1);
        int exitCode1 = ps1.waitFor();
        java.io.InputStream is1 = ps1.getInputStream();
        if (exitCode1 == 0) {
            log.info("File converted successfully");
        } else {
            log.error("Error Occurred while converting mp3 file. Exit code: " + exitCode1);
        }
        byte b1[] = new byte[is1.available()];
        is1.read(b1, 0, b1.length);
        log.info("log ffmpeg wav conversion");
        log.info(new String(b1));
    }

    public void saveMP3File(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename().replaceAll("\\s", "");
        String fileDestPath = originalFileSavePath + File.separator + fileName;
        Files.copy(file.getInputStream(), Paths.get(fileDestPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public void saveMP3File(File file) throws Exception {
        String fileName = file.getName().replaceAll("\\s", "");
        String fileDestPath = originalFileSavePath + File.separator + fileName;
        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(fileDestPath), StandardCopyOption.REPLACE_EXISTING);
    }


    public void extractFeatures(String fileName) throws Exception {
        log.info("Started Extract Features......");
        String fileDestPath = outputFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        File xmlFile = new File(jAudioSampleBatchXml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

//        NodeList nodesFile = doc.getElementsByTagName("file");
//        for (int i = 0; i < nodesFile.getLength(); i++) {
//            Node n = nodesFile.item(i);
//            doc.getElementsByTagName("file").item(i).removeChild(n);
//        }
        doc.getElementsByTagName("file").item(0).getFirstChild().setNodeValue(fileDestPath);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(jAudioSampleBatchXml));
        transformer.transform(source, streamResult);

        File dir = new File(jAudioJarPath + File.separator);
        String cmd = "java -jar jAudio-1.0.4.jar -b bdone";
        Process ps = Runtime.getRuntime().exec(cmd, null, dir);
        java.io.InputStream is = ps.getInputStream();
        log.info("Executing jAudio.jar.. Please wait");
        int exitCode = ps.waitFor();
        if (exitCode == 0) {
            log.info("JAR file executed successfully.");
        } else {
            log.error("Error executing JAR file. Exit code: " + exitCode);
        }
        byte b[] = new byte[is.available()];
        is.read(b, 0, b.length);
        log.info("log out from jAudio");
        log.info(new String(b));
    }

    public void extractFeatures(File file) throws Exception {
        log.info("Started Extract Features......");
        //String fileDestPath = outputFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        File xmlFile = new File(jAudioSampleBatchXml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        doc.getElementsByTagName("file").item(0).getFirstChild().setNodeValue(file.getAbsolutePath());
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(jAudioSampleBatchXml));
        transformer.transform(source, streamResult);

        File dir = new File(jAudioJarPath + File.separator);
        String cmd = "java -jar jAudio-1.0.4.jar -b bdone";
        Process ps = Runtime.getRuntime().exec(cmd, null, dir);
        java.io.InputStream is = ps.getInputStream();
        log.info("Executing jAudio.jar.. Please wait");
        int exitCode = ps.waitFor();
        if (exitCode == 0) {
            log.info("JAR file executed successfully.");
        } else {
            log.info("Error executing JAR file. Exit code: " + exitCode);
        }
        byte b[] = new byte[is.available()];
        is.read(b, 0, b.length);
        log.info("log out from jAudio");
        log.info(new String(b));
    }

    public FeatureVectorFile readAudioFeatureXml() throws JAXBException, IOException {
        File xmlFile = new File(String.valueOf(Path.of(jAudioFeaturesXml)));

        JAXBContext jaxbContext = JAXBContext.newInstance(FeatureVectorFile.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        FeatureVectorFile featureVectorFile = (FeatureVectorFile) jaxbUnmarshaller.unmarshal(xmlFile);
        log.info("===============================");
        return featureVectorFile;
    }

    public FeatureVectorFile readAudioFeatureXml(String path) throws JAXBException, IOException {
        File xmlFile = new File(String.valueOf(Path.of(path)));

        JAXBContext jaxbContext = JAXBContext.newInstance(FeatureVectorFile.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        FeatureVectorFile featureVectorFile = (FeatureVectorFile) jaxbUnmarshaller.unmarshal(xmlFile);
        log.info("===============================");
        return featureVectorFile;
    }

    public void createCsv(FeatureVectorFile featureVectorFile) throws IOException {
        File file = new File(csvFeatureOutput);
        // create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(file);

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);
        String[] header = getCSVHeaders();
        writer.writeNext(header);
        ArrayList<String> data = new ArrayList<String>();
        for (Feature feature : featureVectorFile.getDataSet().getFeatureList()) {
            for (String value : feature.getValues()) {

                data.add(value);
            }
        }
        String[] dataArray = data.toArray(new String[0]);
        writer.writeNext(dataArray);
        writer.close();
    }

    public ArrayList<String> getFeatureData(FeatureVectorFile featureVectorFile) throws IOException {

        ArrayList<String> data = new ArrayList<String>();
        for (Feature feature : featureVectorFile.getDataSet().getFeatureList()) {
            for (String value : feature.getValues()) {

                data.add(value);
            }
        }
        return data;
//        String[] dataArray = data.toArray(new String[0]);
//        writer.writeNext(dataArray);
//        writer.close();
    }

    public String[] getCSVHeaders() {
        return new String[]{"SpectralCentroidOverallStandardDeviation", "SpectralRolloffPointOverallStandardDeviation",
                "SpectralFluxOverallStandardDeviation", "CompactnessOverallStandardDeviation",
                "SpectralVariabilityOverallStandardDeviation",
                "RootMeanSquareOverallStandardDeviation", "FractionOfLowEnergyWindowsOverallStandardDeviation",
                "ZeroCrossingsOverallStandardDeviation", "StrongestBeatOverallStandardDeviation",
                "BeatSumOverallStandardDeviation", "StrengthOfStrongestBeatOverallStandardDeviation",
                "LPCOverallStandardDeviation/v/0", "LPCOverallStandardDeviation/v/1", "LPCOverallStandardDeviation/v/2",
                "LPCOverallStandardDeviation/v/3", "LPCOverallStandardDeviation/v/4", "LPCOverallStandardDeviation/v/5",
                "LPCOverallStandardDeviation/v/6", "LPCOverallStandardDeviation/v/7", "LPCOverallStandardDeviation/v/8",
                "LPCOverallStandardDeviation/v/9", "MethodofMomentsOverallStandardDeviation/v/0",
                "MethodofomentsOverallStandardDeviation/v/1", "MethodofMomentsOverallStandardDeviation/v/2", "MethodofMomentsOverallStandardDeviation/v/3",
                "MethodofMomentsOverallStandardDeviation/v/4", "SpectralCentroidOverallAverage",
                "SpectralRolloffPointOverallAverage", "SpectralFluxOverallAverage", "CompactnessOverallAverage", "SpectralVariabilityOverallAverage",
                "RootMeanSquareOverallAverage", "FractionOfLowEnergyWindowsOverallAverage", "ZeroCrossingsOverallAverage",
                "StrongestBeatOverallAverage", "BeatSumOverallAverage", "StrengthOfStrongestBeatOverallAverage", "LPCOverallAverage/v/0",
                "LPCOverallAverage/v/1", "LPCOverallAverage/v/2", "LPCOverallAverage/v/3", "LPCOverallAverage/v/4", "LPCOverallAverage/v/5",
                "LPCOverallAverage/v/6", "LPCOverallAverage/v/7", "LPCOverallAverage/v/8", "LPCOverallAverage/v/9", "MethodofMomentsOverallAverage/v/0",
                "MethodofMomentsOverallAverage/v/1", "MethodofMomentsOverallAverage/v/2", "MethodofMomentsOverallAverage/v/3",
                "MethodofMomentsOverallAverage/v/4"
        };

//        return new String[] {"SpectralCentroidOverallStandardDeviation", "SpectralRolloffPointOverallStandardDeviation",
//                "SpectralFluxOverallStandardDeviation", "CompactnessOverallStandardDeviation", "SpectralVariabilityOverallStandardDeviation",
//                "RootMeanSquareOverallStandardDeviation", "FractionOfLowEnergyWindowsOverallStandardDeviation", "ZeroCrossingsOverallStandardDeviation",
//                "StrongestBeatOverallStandardDeviation", "BeatSumOverallStandardDeviation", "StrengthOfStrongestBeatOverallStandardDeviation", "LPCOverallStandardDeviation/v/0",
//                "LPCOverallStandardDeviation/v/1", "LPCOverallStandardDeviation/v/2", "LPCOverallStandardDeviation/v/3", "LPCOverallStandardDeviation/v/4",
//                "LPCOverallStandardDeviation/v/5", "LPCOverallStandardDeviation/v/6", "LPCOverallStandardDeviation/v/7", "LPCOverallStandardDeviation/v/8",
//                "LPCOverallStandardDeviation/v/9", "MethodofMomentsOverallStandardDeviation/v/0", "MethodofMomentsOverallStandardDeviation/v/1",
//                "MethodofMomentsOverallStandardDeviation/v/2", "MethodofMomentsOverallStandardDeviation/v/3", "MethodofMomentsOverallStandardDeviation/v/4",
//                "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/0", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/1",
//                "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/2", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/3",
//                "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/4", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/5", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/6",
//                "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/7", "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/8",
//                "AreaMethodofMomentsofMFCCsOverallStandardDeviation/v/9", "SpectralCentroidOverallAverage", "SpectralRolloffPointOverallAverage",
//                "SpectralFluxOverallAverage", "CompactnessOverallAverage", "SpectralVariabilityOverallAverage", "RootMeanSquareOverallAverage",
//                "FractionOfLowEnergyWindowsOverallAverage", "ZeroCrossingsOverallAverage", "StrongestBeatOverallAverage", "BeatSumOverallAverage",
//                "StrengthOfStrongestBeatOverallAverage", "LPCOverallAverage/v/0", "LPCOverallAverage/v/1", "LPCOverallAverage/v/2", "LPCOverallAverage/v/3",
//                "LPCOverallAverage/v/4", "LPCOverallAverage/v/5", "LPCOverallAverage/v/6", "LPCOverallAverage/v/7", "LPCOverallAverage/v/8",
//                "LPCOverallAverage/v/9", "MethodofMomentsOverallAverage/v/0", "MethodofMomentsOverallAverage/v/1", "MethodofMomentsOverallAverage/v/2",
//                "MethodofMomentsOverallAverage/v/3", "MethodofMomentsOverallAverage/v/4", "AreaMethodofMomentsofMFCCsOverallAverage/v/0",
//                "AreaMethodofMomentsofMFCCsOverallAverage/v/1", "AreaMethodofMomentsofMFCCsOverallAverage/v/2", "AreaMethodofMomentsofMFCCsOverallAverage/v/3",
//                "AreaMethodofMomentsofMFCCsOverallAverage/v/4", "AreaMethodofMomentsofMFCCsOverallAverage/v/5", "AreaMethodofMomentsofMFCCsOverallAverage/v/6",
//                "AreaMethodofMomentsofMFCCsOverallAverage/v/7", "AreaMethodofMomentsofMFCCsOverallAverage/v/8", "AreaMethodofMomentsofMFCCsOverallAverage/v/9"};

    }

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File("tempfile"); // You can change the file name and path as needed
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        return file;
    }

    public static String getPredictedString(Integer predictedValue) {
        if (predictedValue == 0) {
            return CALM;
        } else if (predictedValue == 1) {
            return HAPPY;
        } else if (predictedValue == 2) {
            return SAD;
        } else {
            return "ERROR";
        }
    }
}
