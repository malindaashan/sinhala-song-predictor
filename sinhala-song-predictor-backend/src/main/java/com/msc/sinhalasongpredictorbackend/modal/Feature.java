package com.msc.sinhalasongpredictorbackend.modal;

import javax.xml.bind.annotation.XmlElement;
public class Feature {

    private String name;
    private String val;

    public String getName() {
        return name;
    }
    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }
    @XmlElement(name = "v")
    public void setVal(String val) {
        this.val = val;
    }
}
