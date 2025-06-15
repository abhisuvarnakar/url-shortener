package com.abhishek.urlshortener.sql;

import com.abhishek.urlshortener.entity.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UrlSql {

    private static final Logger log = LoggerFactory.getLogger(UrlSql.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UrlSql(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void expireOldUrls(Date date) {
        String sql = "UPDATE t_shortened_url SET status = :statusExpired " +
                "WHERE status = :statusActive AND expires_at < :date";

        Map<String, Object> params = new HashMap<>();
        params.put("statusActive", Status.ACTIVE.name());
        params.put("statusExpired", Status.EXPIRED.name());
        params.put("date", date);

        int updateCount = namedParameterJdbcTemplate.update(sql, params);
        if (updateCount > 0) {
            log.info("Marked {} URLs as expired.", updateCount);
        }
    }

    public List<Map<String, Object>> getDailyClicks(Long urlId, Date startDate) {
        String sql = "SELECT DATE(c.click_time) AS date, COUNT(*) AS clicks " +
                "FROM t_url_analytics c " +
                "WHERE c.url_id = :urlId AND c.click_time >= :startDate " +
                "GROUP BY DATE(c.click_time) " +
                "ORDER BY DATE(c.click_time)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("urlId", urlId);
        params.addValue("startDate", startDate);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, params);

        return result;
    }

    public Map<String, Long> getClickStats(Long urlId) {
        String sql = "SELECT " +
                "COUNT(*) AS totalClicks, " +
                "SUM(CASE WHEN DATE(c.click_time) = CURRENT_DATE THEN 1 ELSE 0 END) AS " +
                "todayClicks, " +
                "SUM(CASE WHEN DATE_TRUNC('week', c.click_time) = DATE_TRUNC('week', " +
                " CURRENT_DATE) THEN 1 ELSE 0 END) AS weekClicks, " +
                "SUM(CASE WHEN DATE_TRUNC('month', c.click_time) = DATE_TRUNC('month', " +
                " CURRENT_DATE) THEN 1 ELSE 0 END) AS monthClicks " +
                "FROM t_url_analytics c " +
                "WHERE c.url_id = :urlId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("urlId", urlId);

        Map<String, Long> resultMap = namedParameterJdbcTemplate.queryForObject(sql, params,
                (rs, rownum) -> {
                    Map<String, Long> result = new HashMap<>();
                    result.put("totalClicks", rs.getLong("totalClicks"));
                    result.put("todayClicks", rs.getLong("todayClicks"));
                    result.put("weekClicks", rs.getLong("weekClicks"));
                    result.put("monthClicks", rs.getLong("monthClicks"));
                    return result;
                });

        return resultMap;
    }

    public List<Map<String, Object>> getTopCountries(Long urlId, Date startDate) {
        String sql = "SELECT c.country AS name, COUNT(*) AS clicks " +
                "FROM t_url_analytics c " +
                "WHERE c.url_id = :urlId AND c.click_time >= :startDate " +
                "GROUP BY c.country " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 5";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("urlId", urlId);
        params.addValue("startDate", startDate);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, params);

        return result;
    }

    public List<Map<String, Object>> getDeviceTypes(Long urlId, Date startDate) {
        String sql = "SELECT c.device_type AS type, COUNT(*) AS clicks " +
                "FROM t_url_analytics c " +
                "WHERE c.url_id = :urlId AND c.click_time >= :startDate " +
                "GROUP BY c.device_type " +
                "ORDER BY COUNT(*) DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("urlId", urlId);
        params.addValue("startDate", startDate);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, params);

        return result;
    }

    public List<Map<String, Object>> getBrowsers(Long urlId, Date startDate) {
        String sql = "SELECT c.browser AS name, COUNT(*) AS clicks " +
                "FROM t_url_analytics c " +
                "WHERE c.url_id = :urlId AND c.click_time >= :startDate " +
                "GROUP BY c.browser " +
                "ORDER BY COUNT(*) DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("urlId", urlId);
        params.addValue("startDate", startDate);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, params);

        return result;
    }

    public long getClickChange24h(Long userId) {
        String sql = "SELECT " +
                "    COUNT(*) FILTER ( " +
                "    WHERE click_time BETWEEN NOW() - INTERVAL '48 HOURS' AND NOW() - " +
                "     INTERVAL '24 HOURS') AS previous24h, " +
                "    COUNT(*) FILTER ( " +
                "    WHERE click_time >= NOW() - INTERVAL '24 HOURS') AS current24h " +
                "FROM v_url_analytics " +
                "WHERE user_id = :userId ";

        Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(sql,
                new MapSqlParameterSource().addValue("userId", userId));
        long previous = ((Number) result.get("previous24h")).longValue();
        long current = ((Number) result.get("current24h")).longValue();

        return previous == 0 ? 0 : ((current - previous) * 100) / previous;
    }

    public Map<String, Object> getTopCountry(Long userId) {
        String sql = "SELECT t.country AS name, " +
                "       ROUND(COUNT(*) * 100.0 / total.total_clicks, 2) AS percentage " +
                "FROM v_url_analytics t " +
                "CROSS JOIN ( " +
                "    SELECT COUNT(*) AS total_clicks " +
                "    FROM v_url_analytics " +
                "    WHERE user_id = :userId " +
                ") AS total " +
                "WHERE t.user_id = :userId " +
                "GROUP BY t.country, total.total_clicks " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 1";

        Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(sql,
                new MapSqlParameterSource().addValue(
                        "userId", userId));

        return result;
    }

    public void deleteAnalyticsByUrlId(Long urlId) {
        String sql = "DELETE FROM t_url_analytics WHERE url_id = :urlId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("urlId", urlId);

        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void extendExpiry(Long urlId, int extensionDays) {
        String sql = "UPDATE t_shortened_url " +
                " SET expires_at = date_trunc('day', CURRENT_TIMESTAMP) + (:extensionDays " +
                " || ' days')::interval, " +
                " status = :status " +
                " WHERE url_id = :urlId";

        Map<String, Object> params = Map.of(
                "extensionDays", String.valueOf(extensionDays),
                "status", Status.ACTIVE.name(),
                "urlId", urlId
        );

        int rowsUpdated = namedParameterJdbcTemplate.update(sql, params);

        log.info("Extended expiry for URL ID {} by {} days. Rows affected: {}", urlId,
                extensionDays, rowsUpdated);
    }


}
