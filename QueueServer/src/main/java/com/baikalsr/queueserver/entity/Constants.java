package com.baikalsr.queueserver.entity;

import javax.persistence.*;

@Entity
public class Constants {
    public  enum NameConstant{
        BSService
    }

    @Id
    @Enumerated(EnumType.STRING)
    private NameConstant nameConstant;
    private String value;

    public NameConstant getNameConstant() {
        return nameConstant;
    }

    public void setNameConstant(NameConstant nameConstant) {
        this.nameConstant = nameConstant;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
