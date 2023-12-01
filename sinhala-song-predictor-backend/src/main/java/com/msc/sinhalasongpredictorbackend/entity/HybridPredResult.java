package com.msc.sinhalasongpredictorbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hybridpredresult")
public class HybridPredResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer predLabel;
    private Double score;
    private String timestamp;

    @OneToOne
    @JoinColumn(name = "ml_pred_result_id")
    private MLPredResult mlPredResult;

    @OneToOne
    @JoinColumn(name = "nlp_pred_result_id")
    private NLPPredResult nlpPredResult;


}
