package com.msc.sinhalasongpredictorbackend.repository;

import com.msc.sinhalasongpredictorbackend.entity.MLPredResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MLPredResultRepository extends JpaRepository<MLPredResult, Long> {
}
