package com.example.audiovideolearning.utils.audio.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/14
 */
public class PcmToWav {


    public static boolean mergePCMFilesToWAVFile(List<String> filePathList,
                                                 String destinationPath) {

        File[] file = new File[filePathList.size()];
        byte buffer[] = null;

        int TOTAL_SIZE = 0;
        int fileNum = filePathList.size();

        for (int i = 0; i < fileNum; i++) {
            file[i] = new File(filePathList.get(i));
            TOTAL_SIZE += file[i].length();
        }

        WaveHeader header = new WaveHeader();
        header.fileLength = TOTAL_SIZE + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 2;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DatahdrLeth = TOTAL_SIZE;

        byte[] h  = null;

        try {
            h = header.getHeader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (h.length != 44){
            return false;
        }
        File destfile = new File(destinationPath);
        if (destfile.exists())
            destfile.delete();


        buffer = new byte[1024*4];
        InputStream inputStream = null;
        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationPath));
            outputStream.write(h , 0 , h.length);

            for (int j = 0 ; j < fileNum ; j++){
                inputStream = new BufferedInputStream(new FileInputStream(file[j]));
                int size = inputStream.read(buffer);
                while (size != -1){
                    outputStream.write(buffer);
                    size = inputStream.read(buffer);
                }
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

}
