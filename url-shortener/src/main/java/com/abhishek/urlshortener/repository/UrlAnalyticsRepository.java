package com.abhishek.urlshortener.repository;

import com.abhishek.urlshortener.entity.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {

    @Query(value = "SELECT COUNT(*) FROM t_url_analytics c " +
            "WHERE c.url_id = :urlId AND DATE(c.click_time) = :date", nativeQuery = true)
    Long countByUrlIdAndDate(@Param("urlId") Long urlId, @Param("date") Date date);

    @Query(value = "SELECT COUNT(*) FROM t_url_analytics c " +
            "WHERE c.url_id = :urlId AND EXTRACT(YEAR FROM c.click_time) = :year " +
            "AND EXTRACT(WEEK FROM c.click_time) = :week", nativeQuery = true)
    Long countByUrlIdAndWeek(@Param("urlId") Long urlId, @Param("year") int year,
                             @Param("week") int week);

    @Query(value = "SELECT COUNT(*) FROM t_url_analytics c " +
            "WHERE c.url_id = :urlId AND EXTRACT(YEAR FROM c.click_time) = :year " +
            "AND EXTRACT(MONTH FROM c.click_time) = :month", nativeQuery = true)
    Long countByUrlIdAndMonth(@Param("urlId") Long urlId, @Param("year") int year,
                              @Param("month") int month);

}
