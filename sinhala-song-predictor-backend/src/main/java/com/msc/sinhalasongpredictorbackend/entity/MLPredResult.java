package com.msc.sinhalasongpredictorbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mlpredresult")
public class MLPredResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String distribution;

    private String featureSelection;

    private String model;

    private Integer predLabel;

    private String fileName;
}
