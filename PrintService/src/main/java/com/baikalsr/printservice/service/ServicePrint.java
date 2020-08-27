package com.baikalsr.printservice.service;

import com.baikalsr.printservice.entity.PrintJob;
import com.baikalsr.printservice.entity.StatusJob;
import com.baikalsr.printservice.entity.StatusPrinter;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Properties;

@Component
public class ServicePrint {
    @Autowired
    QueuePrint queuePrint;
    @Autowired
    PrintService printService;

    private SerialPort serialPort;
    private int[] parametersCOM;

    private boolean getStatuses = false;
    private boolean printing = false;

    private StatusPrinter statusPrinter;
    private StatusPrinter statusPaper;
    private StatusPrinter statusCuter;
    private StatusPrinter statusHead;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicePrint.class);

    public ServicePrint() {
        String PATH_TO_PROPERTIES = Paths.get("").toAbsolutePath().toString() + "/application.properties";
        parametersCOM = new int[4];
        try {
            FileInputStream fis = new FileInputStream(PATH_TO_PROPERTIES);
            Properties properties = new Properties();
            properties.load(fis);
            String COMPort = properties.getProperty("COMPort");
            if (!COMPort.equals("")) {
                String COMParameters = properties.getProperty("COMParam");
                LOGGER.info("Создан COM порт: " + COMPort);
                serialPort = new SerialPort(COMPort);
                int i = 0;
                for (String parameter : COMParameters.split(";")) {
                    parametersCOM[i] = Integer.parseInt(parameter);
                    i++;
                }
                LOGGER.info("Получены параметры для поключени COM: [boundRate: " + parametersCOM[0] +
                        ", dataBits: " + parametersCOM[1] +
                        ", stopBits: " + parametersCOM[2] +
                        ", parity: " + parametersCOM[3] + "]");
            }else
                LOGGER.warn("Требуется указать настройки COM!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized boolean openPort() {
        try {
            if (!serialPort.isOpened())
                if (serialPort.openPort())
                    if (serialPort.setParams(parametersCOM[0], parametersCOM[1], parametersCOM[2], parametersCOM[3]))
                        return true;
                    else
                        throw new Exception("Не правильные параметры COM порта");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private synchronized boolean closePort() {
        boolean result = false;
        try {
            result = serialPort.closePort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public synchronized void getStatuses() {
        try {
            if (printing) return;
            getStatuses = true;
            openPort();
            //Получаем стостояние онлайн или не онлайн
            if (serialPort.writeBytes(new byte[]{0x10, 0x04, 1})) {
                byte[] data = serialPort.readBytes(1, 1000);
                BitSet bitsPrinter = BitSet.valueOf(data);
                if (bitsPrinter.get(3)) statusPrinter = StatusPrinter.OFFLINE;
                else statusPrinter = StatusPrinter.ONLINE;

                //Получаем стояния частей принтера
                serialPort.writeBytes(new byte[]{0x10, 0x04, 3});
                data = serialPort.readBytes(1, 1000);
                BitSet bitsErrors = BitSet.valueOf(data);
                if (bitsErrors.get(3)) statusCuter = StatusPrinter.CUTTER_ERROR;
                else statusCuter = StatusPrinter.CUTTER_NORMAL;

                if (bitsErrors.get(6)) statusHead = StatusPrinter.PRINT_HEAD_BAD;
                else statusHead = StatusPrinter.PRINT_HEAD_NORMAL;

                //Получаем состояния бумаги
                serialPort.writeBytes(new byte[]{0x10, 0x04, 4});
                data = serialPort.readBytes(1, 1000);
                BitSet bitsPaper = BitSet.valueOf(data);
                if (bitsPaper.get(3)) statusPaper = StatusPrinter.WILL_NO_PAPER;
                else statusPaper = StatusPrinter.NORMAL_PAPER;

                if (bitsPaper.get(6)) statusPaper = StatusPrinter.NO_PAPER;
                else statusPaper = statusPaper == StatusPrinter.WILL_NO_PAPER ? StatusPrinter.WILL_NO_PAPER : StatusPrinter.NORMAL_PAPER;
//                LOGGER.info("Статус принтера: " + statusPrinter.name() +
//                        "\nСтатус бумаги: " + statusPaper.name() +
//                        "\nСтатус головы: " + statusHead.name() +
//                        "\nСтатус ножа: " + statusCuter.name());
            } else {
                statusPrinter = StatusPrinter.OFFLINE;
            }
        } catch (SerialPortTimeoutException te) {
            LOGGER.warn("Не удалось получеть ответ от COM порта. Ожидание сброшено по таймауту.");
        } catch (Exception e) {
            LOGGER.warn("Не удалось связаться с COM портом.");
        }
        closePort();
        getStatuses = false;
    }

    @Scheduled(fixedDelay = 1 * 1000)
    public synchronized void printFromQueue() {
        PrintJob printJob = queuePrint.getJobForPrint();
        if (printJob != null) {
            if (getStatuses) return;
            printing = true;
            this.getStatuses();
            if (statusPrinter == StatusPrinter.ONLINE && statusPaper != StatusPrinter.NO_PAPER &&
                    statusCuter == StatusPrinter.CUTTER_NORMAL && statusHead == StatusPrinter.PRINT_HEAD_NORMAL) {
                if (openPort()) {
                    try {
                        boolean writeBytes = false;
                        writeBytes = serialPort.writeBytes(printJob.getContent());
                        LOGGER.info("Отправлен контент: " + writeBytes);
                        printJob.setStatusJob(writeBytes ? StatusJob.COMPLETE : StatusJob.ERROR);
                    } catch (SerialPortException e) {
                        LOGGER.error("При выводе задания печати не удалось записать данные в COM порт.");
                        e.printStackTrace();
                        printJob.setStatusJob(StatusJob.ERROR);
                    }
                }else {
                    LOGGER.error("Порт не открылся при печати талона.");
                    printJob.setStatusJob(StatusJob.ERROR);
                }
                closePort();
            }else {
                printJob.setStatusJob(StatusJob.ERROR);
            }
        }
        printing = false;
    }

    //Getters and Setters


    public synchronized SerialPort getSerialPort() {
        return serialPort;
    }

    public synchronized void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public synchronized int[] getParametersCOM() {
        return parametersCOM;
    }

    public synchronized void setParametersCOM(int[] parametersCOM) {
        this.parametersCOM = parametersCOM;
    }

    public  synchronized StatusPrinter getStatusPrinter() {
        return statusPrinter;
    }

    public synchronized void setStatusPrinter(StatusPrinter statusPrinter) {
        this.statusPrinter = statusPrinter;
    }

    public synchronized StatusPrinter getStatusPaper() {
        return statusPaper;
    }

    public synchronized void setStatusPaper(StatusPrinter statusPaper) {
        this.statusPaper = statusPaper;
    }

    public synchronized StatusPrinter getStatusCuter() {
        return statusCuter;
    }

    public synchronized void setStatusCuter(StatusPrinter statusCuter) {
        this.statusCuter = statusCuter;
    }

    public synchronized StatusPrinter getStatusHead() {
        return statusHead;
    }

    public synchronized void setStatusHead(StatusPrinter statusHead) {
        this.statusHead = statusHead;
    }
}
