package com.msc.sinhalasongpredictorbackend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class CommonUtil {

    @Value("${file.save.location}")
    private String fileSavePath;
    public void convertMP3Wav(String inputPath, String outputPath) throws IOException, InterruptedException {
        /*ffmpeg conversion to wav and khz conversion*/
      //  String cmd1 = "ffmpeg -i \"C:\\Users\\MalindaPieris\\Downloads\\Kavikariye - Bathiya N Santhush.mp3\" -acodec pcm_s16le -ac 2 -ar 48000 output6.wav";

        String cmd1 = "ffmpeg -i "+inputPath+" -acodec pcm_s16le -ac 2 -ar 48000 "+outputPath;
        Process ps1 = Runtime.getRuntime().exec(cmd1);

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

    public void saveMP3File(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String fileDestPath = fileSavePath + File.separator + fileName;
        Files.copy(file.getInputStream(), Paths.get(fileDestPath), StandardCopyOption.REPLACE_EXISTING);
    }
}
