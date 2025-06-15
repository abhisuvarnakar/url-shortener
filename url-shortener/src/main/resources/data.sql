CREATE OR REPLACE VIEW v_url_analytics AS
select s.url_id, s.click_count, s.created_at as url_created_at, s.custom_alias,
 s.expires_at as url_expires_at, s.original_url, s.short_code, s.status as url_status,
 s.user_id, u.analytics_id, u.browser, u.click_time, u.country, u.device_type,
 u.ip_address, u.os, u.platform, u.referrer, u.user_agent
from t_shortened_url s
join t_url_analytics u on s.url_id = u.url_id;