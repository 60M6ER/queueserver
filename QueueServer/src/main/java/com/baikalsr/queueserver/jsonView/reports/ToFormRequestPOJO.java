package com.baikalsr.queueserver.jsonView.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ToFormRequestPOJO {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    private Date startDate;
    private Date endDate;
    private double norm;
    private double overNorm;
    private double critNorm;
    private double overCritNorm;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        try {
            this.startDate = df.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        try {
            this.endDate = df.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getNorm() {
        return norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    public double getOverNorm() {
        return overNorm;
    }

    public void setOverNorm(double overNorm) {
        this.overNorm = overNorm;
    }

    public double getCritNorm() {
        return critNorm;
    }

    public void setCritNorm(double critNorm) {
        this.critNorm = critNorm;
    }

    public double getOverCritNorm() {
        return overCritNorm;
    }

    public void setOverCritNorm(double overCritNorm) {
        this.overCritNorm = overCritNorm;
    }
}
