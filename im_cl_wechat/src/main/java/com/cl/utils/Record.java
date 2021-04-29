package com.cl.utils;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.util.Scanner;
import javax.sound.sampled.*;

/**
 * 录音record()，录音完毕后将音频文件保存到指定文件夹中
 * 播放音频文件play(音频文件路径)
 */
public class Record {
    private static AudioFormat audioFormat;
    static TargetDataLine targetDataLine;

    public File record() {
        System.out.println("想聊天，请按y。结束讲话，请按n。");
        Scanner input = new Scanner(System.in);
        String Sinput = input.next();
        if(Sinput.equals("y")){
            System.out.println("讲话中……");
            captureAudio();//调用录音方法
        }
        Scanner input_2 = new Scanner(System.in);
        String Sinput_2 = input_2.next();
        if(Sinput_2.equals("n")){
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("结束讲话。");
        }
        //结束录音后便在指定路径生成一个record.wav文件
        File audioFile = new File("F:\\record.wav");
        return audioFile;
    }

    //录音方法
    public void captureAudio(){
        try {
            audioFormat = new AudioFormat(16000,16,1,true,false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

            new CaptureThread().start();//开启线程
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    class CaptureThread extends Thread {
        public void run() {
            AudioFileFormat.Type fileType = null;
            //指定的文件类型
            File audioFile = null;
            //设置文件类型和文件扩展名
            //根据选择的单选按钮。
            fileType = AudioFileFormat.Type.WAVE;
            audioFile = new File("F://record.wav");
            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                //当开始音频捕获或回放时，生成 START 事件。
                AudioSystem.write(new AudioInputStream(targetDataLine),fileType, audioFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //播放音频文件
    public void play(String filePathName){
        File file = new File(filePathName);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            Player player = new Player(bis);
            player.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}