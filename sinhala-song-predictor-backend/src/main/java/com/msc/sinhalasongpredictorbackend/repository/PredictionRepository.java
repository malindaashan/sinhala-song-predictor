package com.msc.sinhalasongpredictorbackend.repository;

import com.msc.sinhalasongpredictorbackend.entity.Prediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    Page<Prediction> findALlByOrderByIdAsc(Pageable page);

}
