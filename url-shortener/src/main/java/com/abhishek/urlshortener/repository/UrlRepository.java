package com.abhishek.urlshortener.repository;

import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortenedUrl, Long>,
        JpaSpecificationExecutor<ShortenedUrl> {

    Optional<ShortenedUrl> findByShortCode(String shortCode);

    Optional<ShortenedUrl> findByCustomAlias(String customAlias);

    Optional<ShortenedUrl> findByOriginalUrlAndUser_Id(String originalUrl, Long userId);

    boolean existsByShortCode(String shortCode);

    @Query(value = "select u.* from t_shortened_url u where u.user_id = :userId" +
            " and u.expires_at between :now and :cutoff",
            countQuery = "select count(u.*) from t_shortened_url u where u.user_id = :userId" +
                    " and u.expires_at between :now and :cutoff",
            nativeQuery = true)
    Page<ShortenedUrl> findExpiringUrl(Long userId, @Param("now") Date now,
                                       @Param("cutoff") Date cutoff,
                                       Pageable pageable);

    @Query("SELECT COUNT(s) FROM ShortenedUrl s WHERE s.user.id = :userId")
    long countTotalUrls(@Param("userId") Long userId);

    @Query("SELECT SUM(s.clickCount) FROM ShortenedUrl s WHERE s.user.id = :userId")
    Long getTotalClickCount(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM ShortenedUrl s WHERE s.status = :status AND s.user.id = :userId")
    long countByStatus(@Param("status") Status status, @Param("userId") Long userId);

    @Query("SELECT AVG(s.clickCount) FROM ShortenedUrl s WHERE s.user.id = :userId")
    Double getAverageClickPerUrl(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM ShortenedUrl s WHERE s.expiresAt IS NOT NULL" +
            " AND s.status = :status AND s.user.id = :userId" +
            " AND s.expiresAt BETWEEN :now AND :futureDate")
    long countExpiringUrlsInNextDays(@Param("now") Date now,
                                     @Param("futureDate") Date futureDate,
                                     @Param("status") Status status,
                                     @Param("userId") Long userId);

    @Query(value = "SELECT * FROM t_shortened_url WHERE status = :status AND user_id = :userId " +
            "ORDER BY click_count DESC LIMIT 1",
            nativeQuery = true)
    Optional<ShortenedUrl> findTopPerformingUrl(@Param("status") String status,
                                                @Param("userId") Long userId);

}
