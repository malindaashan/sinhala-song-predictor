package com.msc.sinhalasongpredictorbackend.modal;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class DataSet {
    private String dataSetId;
    private List<Feature> featureList;


    public String getDataSetId() {
        return dataSetId;
    }
    @XmlElement(name = "data_set_id")
    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }
    public List<Feature> getFeatureList() {
        return featureList;
    }
    @XmlElement(name = "feature")
    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }
}
