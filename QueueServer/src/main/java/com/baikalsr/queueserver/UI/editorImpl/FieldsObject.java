package com.baikalsr.queueserver.UI.editorImpl;

import java.util.ArrayList;

public class FieldsObject {
    private ArrayList<String> fields;
    private ArrayList<TypeField> typeFields;

    private int count = 0;
    private int currentCount = -1;

    private String name;
    private TypeField typeField;

    public FieldsObject() {
        fields = new ArrayList<>();
        typeFields= new ArrayList<>();
    }

    public void addField(String name, TypeField typeField){
        fields.add(name);
        typeFields.add(typeField);
        count++;
    }

    public boolean isNull() {
        return currentCount > count;
    }

    public void next() {
        if (currentCount < count) {
            currentCount++;
            name = fields.get(currentCount);
            typeField =  typeFields.get(currentCount);
        }
        if (currentCount > count) {
            name = null;
            typeField = null;
        }
    }

    public int length() {
        return count + 1;
    }
}
