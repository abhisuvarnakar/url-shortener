CREATE TABLE t_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_shorturl (
    id SERIAL PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_code VARCHAR(20) NOT NULL UNIQUE,
    user_id INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE url_clicks (
    id SERIAL PRIMARY KEY,
    short_url_id INTEGER NOT NULL,
    click_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    FOREIGN KEY (short_url_id) REFERENCES short_urls(id)
);

-- Indexes for performance
CREATE INDEX idx_short_urls_short_code ON short_urls(short_code);
CREATE INDEX idx_url_clicks_short_url_id ON url_clicks(short_url_id);