package com.project.schoolmanagment.repository.businnes;

import com.project.schoolmanagment.entity.business.EducationTerm;
import com.project.schoolmanagment.entity.enums.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EducationTermRepository extends JpaRepository<EducationTerm, Long> {


    @Query("SELECT (count (e) > 0) FROM EducationTerm e WHERE e.term=?1 AND EXTRACT(YEAR FROM e.startDate) =?2")
    boolean existByTermAndYear(Term term, int year);

    @Query("SELECT E FROM EducationTerm e WHERE EXTRACT(YEAR FROM e.startDate) =?1")
    List<EducationTerm> findByYear(int year);

}
