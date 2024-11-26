package com.vash.entel.repository;
import com.vash.entel.model.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer >{
    @Query(value = "SELECT * FROM fnt_average_surveys_by_adviser()", nativeQuery = true)
    List<Object[]> getSurveyReport();
}