package com.msc.sinhalasongpredictorbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nlppredresult")
public class NLPPredResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String distribution;

    private String wordEmbedding;

    private String model;

    private Integer predLabel;

    @Column(name="lyrics", length=1024)
    private String lyrics;
}
