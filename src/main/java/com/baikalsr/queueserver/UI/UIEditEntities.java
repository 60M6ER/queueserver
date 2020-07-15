package com.baikalsr.queueserver.UI;



import java.util.ArrayList;
import java.util.HashMap;

public interface UIEditEntities {

    ArrayList<HashMap<String, Object>> getFields();

    Object getField(int i);

    TableEditor getTable(int i);
}
