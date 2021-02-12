package com.baikalsr.queueserver.jsonView.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportTicketNormPOJO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private String nameGroup;
    private Integer countTickets;
    private Double percentNorm;
    private Double percentOverNorm;
    private Double percentCritNorm;
    private Double percentOverCritNorm;
    private Integer countTicketsNorm;
    private Integer countTicketsOverNorm;
    private Integer countTicketsCritNorm;
    private Integer countTicketsOverCritNorm;
    private List<ReportTicketNormPOJO> lines;

    public ReportTicketNormPOJO(List<Object[]> result, ToFormRequestPOJO toFormRequestPOJO) {
        this.lines = new ArrayList<>();
        this.nameGroup = "All";
        this.countTickets = result.size();
        this.countTicketsNorm = 0;
        this.countTicketsOverNorm = 0;
        this.countTicketsCritNorm = 0;
        this.countTicketsOverCritNorm = 0;

        long normTime = (long) toFormRequestPOJO.getNorm() * 60 * 1000;
        long overNormTime = (long) toFormRequestPOJO.getOverNorm() * 60 * 1000;
        long critNormTime = (long) toFormRequestPOJO.getCritNorm() * 60 * 1000;
        //long overCritNormTime = (long) toFormRequestPOJO.getOverCritNorm() * 60 * 1000;

        ReportTicketNormPOJO currentFirstGroup = null;
        ReportTicketNormPOJO currentSecondGroup = null;
        ReportTicketNormPOJO currentThirdGroup = null;
        if (result.size() == 0) {
            throw new RuntimeException("Не найдено данных для отображения по указанным параметрам");
        }

        for (int i = 0; i < result.size(); i++) {
            boolean newGroupFirst = false;
            boolean newGroupSecond = false;
            if (currentFirstGroup == null || !currentFirstGroup.getNameGroup().equals(result.get(i)[4])) {
                //Вычисляем проценты у заполненой группировки
                if (currentFirstGroup != null) {
                    calculatePercent(currentFirstGroup);
                }
                currentFirstGroup = new ReportTicketNormPOJO();
                currentFirstGroup.setNameGroup((String) result.get(i)[4]);
                this.lines.add(currentFirstGroup);
                newGroupFirst = true;
            }
            String nameSecondGroup = Integer.toString(((Double) result.get(i)[5]).intValue());
            if (currentSecondGroup == null || !currentSecondGroup.getNameGroup().equals(nameSecondGroup) || newGroupFirst) {
                //Вычисляем проценты у заполненой группировки
                if (currentSecondGroup != null) {
                    calculatePercent(currentSecondGroup);
                }
                currentSecondGroup = new ReportTicketNormPOJO();
                currentSecondGroup.setNameGroup(nameSecondGroup);
                currentFirstGroup.getLines().add(currentSecondGroup);
                newGroupSecond = true;
            }
            String nameThirdGroup = dateFormat.format((Date) result.get(i)[1]);
            if (currentThirdGroup == null || !currentThirdGroup.getNameGroup().equals(nameThirdGroup) || newGroupSecond) {
                //Вычисляем проценты у заполненой группировки
                if (currentThirdGroup != null) {
                    calculatePercent(currentThirdGroup);
                }
                currentThirdGroup = new ReportTicketNormPOJO();
                currentThirdGroup.setNameGroup(nameThirdGroup);
                currentSecondGroup.getLines().add(currentThirdGroup);
            }

            currentThirdGroup.setCountTickets(currentThirdGroup.getCountTickets() + 1);
            currentSecondGroup.setCountTickets(currentSecondGroup.getCountTickets() + 1);
            currentFirstGroup.setCountTickets(currentFirstGroup.getCountTickets() + 1);

            long difDates = ((Date) result.get(i)[2]).getTime() - ((Date) result.get(i)[1]).getTime();

            //Норма
            if (difDates <= normTime) {
                currentThirdGroup.setCountTicketsNorm(currentThirdGroup.getCountTicketsNorm() + 1);
                currentSecondGroup.setCountTicketsNorm(currentSecondGroup.getCountTicketsNorm() + 1);
                currentFirstGroup.setCountTicketsNorm(currentFirstGroup.getCountTicketsNorm() + 1);
                this.countTicketsNorm++;
            }
            //Сверхнорма
            if (difDates >= normTime && difDates <= overNormTime) {
                currentThirdGroup.setCountTicketsOverNorm(currentThirdGroup.getCountTicketsOverNorm() + 1);
                currentSecondGroup.setCountTicketsOverNorm(currentSecondGroup.getCountTicketsOverNorm() + 1);
                currentFirstGroup.setCountTicketsOverNorm(currentFirstGroup.getCountTicketsOverNorm() + 1);
                this.countTicketsOverNorm++;
            }
            //Критическая норма
            if (difDates >= overNormTime && difDates <= critNormTime) {
                currentThirdGroup.setCountTicketsCritNorm(currentThirdGroup.getCountTicketsCritNorm() + 1);
                currentSecondGroup.setCountTicketsCritNorm(currentSecondGroup.getCountTicketsCritNorm() + 1);
                currentFirstGroup.setCountTicketsCritNorm(currentFirstGroup.getCountTicketsCritNorm() + 1);
                this.countTicketsCritNorm++;
            }
            //Сверхкритическая норма
            if (difDates > critNormTime) {
                currentThirdGroup.setCountTicketsOverCritNorm(currentThirdGroup.getCountTicketsOverCritNorm() + 1);
                currentSecondGroup.setCountTicketsOverCritNorm(currentSecondGroup.getCountTicketsOverCritNorm() + 1);
                currentFirstGroup.setCountTicketsOverCritNorm(currentFirstGroup.getCountTicketsOverCritNorm() + 1);
                this.countTicketsOverCritNorm++;
            }
        }
        calculatePercent(this);
        calculatePercent(currentFirstGroup);
        calculatePercent(currentSecondGroup);
        calculatePercent(currentThirdGroup);
    }

    private ReportTicketNormPOJO() {
        this.nameGroup = "";
        this.lines = new ArrayList<>();
        this.countTickets = 0;
        this.countTicketsNorm = 0;
        this.countTicketsOverNorm = 0;
        this.countTicketsCritNorm = 0;
        this.countTicketsOverCritNorm = 0;
    }

    private void calculatePercent(ReportTicketNormPOJO reportTicketNormPOJO) {
        reportTicketNormPOJO.setPercentNorm(reportTicketNormPOJO.getCountTicketsNorm().doubleValue()
                / reportTicketNormPOJO.getCountTickets().doubleValue() * 100);
        reportTicketNormPOJO.setPercentOverNorm(reportTicketNormPOJO.getCountTicketsOverNorm().doubleValue()
                / reportTicketNormPOJO.getCountTickets().doubleValue() * 100);
        reportTicketNormPOJO.setPercentCritNorm(reportTicketNormPOJO.getCountTicketsCritNorm().doubleValue()
                / reportTicketNormPOJO.getCountTickets().doubleValue() * 100);
        reportTicketNormPOJO.setPercentOverCritNorm(reportTicketNormPOJO.getCountTicketsOverCritNorm().doubleValue()
                / reportTicketNormPOJO.getCountTickets().doubleValue() * 100);
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public Integer getCountTickets() {
        return countTickets;
    }

    public void setCountTickets(Integer countTickets) {
        this.countTickets = countTickets;
    }

    public Double getPercentNorm() {
        return percentNorm;
    }

    public void setPercentNorm(Double percentNorm) {
        this.percentNorm = percentNorm;
    }

    public Double getPercentOverNorm() {
        return percentOverNorm;
    }

    public void setPercentOverNorm(Double percentOverNorm) {
        this.percentOverNorm = percentOverNorm;
    }

    public Double getPercentCritNorm() {
        return percentCritNorm;
    }

    public void setPercentCritNorm(Double percentCritNorm) {
        this.percentCritNorm = percentCritNorm;
    }

    public Double getPercentOverCritNorm() {
        return percentOverCritNorm;
    }

    public void setPercentOverCritNorm(Double percentOverCritNorm) {
        this.percentOverCritNorm = percentOverCritNorm;
    }

    public Integer getCountTicketsNorm() {
        return countTicketsNorm;
    }

    public void setCountTicketsNorm(Integer countTicketsNorm) {
        this.countTicketsNorm = countTicketsNorm;
    }

    public Integer getCountTicketsOverNorm() {
        return countTicketsOverNorm;
    }

    public void setCountTicketsOverNorm(Integer countTicketsOverNorm) {
        this.countTicketsOverNorm = countTicketsOverNorm;
    }

    public Integer getCountTicketsCritNorm() {
        return countTicketsCritNorm;
    }

    public void setCountTicketsCritNorm(Integer countTicketsCritNorm) {
        this.countTicketsCritNorm = countTicketsCritNorm;
    }

    public Integer getCountTicketsOverCritNorm() {
        return countTicketsOverCritNorm;
    }

    public void setCountTicketsOverCritNorm(Integer countTicketsOverCritNorm) {
        this.countTicketsOverCritNorm = countTicketsOverCritNorm;
    }

    public List<ReportTicketNormPOJO> getLines() {
        return lines;
    }

    public void setLines(List<ReportTicketNormPOJO> lines) {
        this.lines = lines;
    }
}
