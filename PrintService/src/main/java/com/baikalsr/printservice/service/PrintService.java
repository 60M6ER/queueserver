package com.baikalsr.printservice.service;

import org.springframework.stereotype.Component;

@Component
public class PrintService {

    private final byte[] initPrinter = new byte[]{0x1B, '@'}; //Сбрасывает все установленные настройки
    private final byte[] setWCP1251 = new byte[]{0x1B, 0x74, 7}; //Установка кодировки WCP-1251
    private final byte[] setAlignLeft = new byte[]{0x1B, 0x61, 0};
    private final byte[] setAlignCenter = new byte[]{0x1B, 0x61, 1};
    private final byte[] setAlignRight = new byte[]{0x1B, 0x61, 2};
    private final byte[] setKegel0 = new byte[]{0x1D, 0x21, 0x00}; //Устанавливает ширину и высоту текста (0x00 - 0x77)
    private final byte[] printAndNewLine = new byte[]{0x0A}; //Вывести строку из буфера на печать с переходом на новую строку
    private final byte[] cutPaper = new byte[]{0x1B, 0x69}; //Отразать бумагу
    private final byte[] setLineSpacing = new byte[]{0x1B, 0x33, 0x33}; //Высота строки. Заменить последний байт 0<x<255;
    private final byte[] printAndFeedLines = new byte[]{0x1B, 0x64, 0x05}; //Вывести строку из буфера на печать и перейти на указанное количество линий (0<x<255)


    public byte[] getAlign(int n) {
        if (n < 0 || n > 2) n = 0;
        switch (n) {
            case 0:
                return setAlignLeft;
            case 1:
                return setAlignCenter;
            case 2:
                return setAlignRight;
        }
        return new byte[0];
    }

    public byte[] getKegel(int n, int m){
        byte[] kegel = setKegel0;
        kegel[2] = (byte) ((n << 4) + m);
        return kegel;
    }

    public byte[] getPrintAndNewLine(){
        return printAndNewLine;
    }

    public byte[] getCutPaper(){
        return cutPaper;
    }

    public byte[] getLineSpacing(int n){
        if (n < 0 || n > 255) n = 33;
        byte[] lineSpacing = setLineSpacing;
        lineSpacing[2] = (byte) n;
        return lineSpacing;
    }

    public byte[] getPrintAndFeedLines(int n){
        if (n < 0 || n > 255) n = 1;
        byte[] feedLines = printAndFeedLines;
        feedLines[2] = (byte) n;
        return feedLines;
    }

    public byte[] getSetWCP1251() {
        return setWCP1251;
    }
}
