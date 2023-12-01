package com.msc.sinhalasongpredictorbackend.repository;

import com.msc.sinhalasongpredictorbackend.entity.NLPPredResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NLPPredResultRepository extends JpaRepository<NLPPredResult, Long> {
}
