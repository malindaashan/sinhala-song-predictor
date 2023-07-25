package com.msc.sinhalasongpredictorbackend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
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

    public void trimMP3(String fileName) throws Exception {
        //ffmpeg -ss 00:00:00.000 -i "output3.wav" -t 90 -map 0 -c copy "output31.wav"
        String inputPath = convertedFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        String outputPath = trimFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        ;
        String cmd1 = "ffmpeg -ss 00:00:00.000 -y -i " + inputPath + " -t 90 -map 0 -c copy " + outputPath;
        Process ps1 = Runtime.getRuntime().exec(cmd1);
        System.out.println(cmd1);
        int exitCode1 = ps1.waitFor();
        java.io.InputStream is1 = ps1.getInputStream();
        if (exitCode1 == 0) {
            System.out.println("File trim successfully");
        } else {
            System.out.println("Error Occurred while trim mp3 file. Exit code: " + exitCode1);
        }
        byte b1[] = new byte[is1.available()];
        is1.read(b1, 0, b1.length);
        System.out.println("log ffmpeg trim conversion");
        System.out.println(new String(b1));

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
        System.out.println(cmd1);
        int exitCode1 = ps1.waitFor();
        java.io.InputStream is1 = ps1.getInputStream();
        if (exitCode1 == 0) {
            System.out.println("File converted successfully");
        } else {
            System.out.println("Error Occurred while converting mp3 file. Exit code: " + exitCode1);
        }
        byte b1[] = new byte[is1.available()];
        is1.read(b1, 0, b1.length);
        System.out.println("log ffmpeg wav conversion");
        System.out.println(new String(b1));
    }

    public void saveMP3File(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename().replaceAll("\\s", "").replace(".mp3", ".wav");
        String fileDestPath = originalFileSavePath + File.separator + fileName;
        Files.copy(file.getInputStream(), Paths.get(fileDestPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public void extractFeatures(String fileName) throws Exception {
        String fileDestPath = outputFileSavePath + File.separator + fileName.replaceAll("\\s", "").replace(".mp3", ".wav");
        File xmlFile = new File(jAudioSampleBatchXml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getElementsByTagName("file").item(0).getFirstChild().setNodeValue(fileDestPath);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(jAudioSampleBatchXml));
        transformer.transform(source, streamResult);

        File dir = new File(jAudioJarPath + File.separator);
        String cmd = "java -jar jAudio-1.0.4.jar -b batch-sample-file.xml";
        Process ps = Runtime.getRuntime().exec(cmd, null, dir);
        java.io.InputStream is = ps.getInputStream();
        int exitCode = ps.waitFor();
        if (exitCode == 0) {
            System.out.println("JAR file executed successfully.");
        } else {
            System.out.println("Error executing JAR file. Exit code: " + exitCode);
        }
        byte b[] = new byte[is.available()];
        is.read(b, 0, b.length);
        System.out.println("log out from jAudio");
        System.out.println(new String(b));
    }
}
