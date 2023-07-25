package com.msc.sinhalasongpredictorbackend.modal;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Feature {

    private String name;
    private List<String> values;

    public String getName() {
        return name;
    }
    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement(name = "v")
    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }
}
