package com.msc.sinhalasongpredictorbackend.modal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "feature_vector_file")
public class FeatureVectorFile {
    private String comments;
    private DataSet dataSet;

    public String getComments() {
        return comments;
    }
    @XmlElement(name = "comments")
    public void setComments(String comments) {
        this.comments = comments;
    }

    public DataSet getDataSet() {
        return dataSet;
    }
    @XmlElement(name = "data_set_id")
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
