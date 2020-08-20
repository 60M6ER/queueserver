package com.baikalsr.queueserver.UI;



import java.util.ArrayList;
import java.util.HashMap;

public interface UIEditEntities {

    String getTitle();

    ArrayList<HashMap<String, Object>> getFields();

    ArrayList<HashMap<String, Object>> getOptionSelect(int i);

    TableEditor getTable(int i);
}
