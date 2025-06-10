<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShortLink - URL Shortener</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #6c63ff;
            --secondary-color: #4d44db;
            --light-color: #f8f9fa;
            --dark-color: #343a40;
            --success-color: #28a745;
            --blue-50: #f0f9ff;
            --indigo-100: #e0e7ff;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            color: #333;
            line-height: 1.6;
        }

        header {
            background-color: white;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 1rem 0;
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 2rem;
        }

        /* Hero Section */
        .hero {
            background: linear-gradient(135deg, var(--blue-50), var(--indigo-100));
            text-align: center;
            padding: 5rem 1rem;
            margin-bottom: 3rem;
            border-radius: 0 0 20px 20px;
        }

        .hero-content {
            max-width: 800px;
            margin: 0 auto;
        }

        .hero h1 {
            font-size: 3rem;
            margin-bottom: 1.5rem;
            color: var(--primary-color);
            line-height: 1.2;
        }

        .hero p {
            font-size: 1.3rem;
            color: var(--dark-color);
            margin-bottom: 2.5rem;
            opacity: 0.9;
        }

        .cta-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: var(--primary-color);
            color: white;
            border: none;
        }

        .btn-primary:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(108, 99, 255, 0.2);
        }

        .btn-outline {
            background-color: transparent;
            color: var(--primary-color);
            border: 2px solid var(--primary-color);
        }

        .btn-outline:hover {
            background-color: rgba(108, 99, 255, 0.1);
            transform: translateY(-2px);
        }

        /* Stats Section */
        .stats-section {
            padding: 3rem 0;
            text-align: center;
        }

        .stats-section h2 {
            font-size: 2rem;
            margin-bottom: 3rem;
            color: var(--dark-color);
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
            margin-bottom: 4rem;
        }

        .stat-card {
            background-color: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
        }

        .stat-icon {
            font-size: 2.5rem;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
        }

        .stat-label {
            color: #666;
            font-size: 1rem;
        }

        /* Features Section */
        .features-section {
            padding: 4rem 0;
            background-color: white;
        }

        .section-title {
            text-align: center;
            font-size: 2rem;
            margin-bottom: 3rem;
            color: var(--dark-color);
        }

        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }

        .feature-card {
            background-color: var(--light-color);
            padding: 2rem;
            border-radius: 12px;
            text-align: center;
            transition: all 0.3s ease;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
        }

        .feature-icon {
            font-size: 2.5rem;
            color: var(--primary-color);
            margin-bottom: 1.5rem;
        }

        .feature-title {
            font-size: 1.5rem;
            margin-bottom: 1rem;
            color: var(--dark-color);
        }

        .feature-desc {
            color: #666;
            line-height: 1.6;
        }

        /* About Section */
        .about-section {
            padding: 4rem 0;
            background-color: var(--light-color);
        }

        .about-card {
            background-color: white;
            padding: 3rem;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            max-width: 900px;
            margin: 0 auto;
            text-align: center;
        }

        .about-card h2 {
            font-size: 2rem;
            margin-bottom: 1.5rem;
            color: var(--dark-color);
        }

        .about-card p {
            color: #666;
            line-height: 1.8;
            margin-bottom: 2rem;
            font-size: 1.1rem;
        }

        /* Final CTA */
        .final-cta {
            padding: 5rem 0;
            text-align: center;
            background: linear-gradient(135deg, var(--blue-50), var(--indigo-100));
        }

        .final-cta h2 {
            font-size: 2rem;
            margin-bottom: 1.5rem;
            color: var(--dark-color);
        }

        .final-cta p {
            color: #666;
            max-width: 600px;
            margin: 0 auto 2rem;
            font-size: 1.1rem;
        }

        /* Responsive Styles */
        @media (max-width: 768px) {
            .hero h1 {
                font-size: 2.2rem;
            }

            .hero p {
                font-size: 1.1rem;
            }

            .btn {
                padding: 0.75rem 1rem;
                font-size: 0.9rem;
            }

            .about-card {
                padding: 2rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>Shorten, Share, and Track Your Links</h1>
                <p>Create memorable short URLs and get detailed analytics on your audience and traffic.</p>
                <div class="cta-buttons">
                    <a href="/signup" class="btn btn-primary">Get Started for Free</a>
                    <a href="/signin" class="btn btn-outline">Sign In</a>
                </div>
            </div>
        </section>

        <!-- Global Stats -->
        <section class="stats-section">
            <div class="container">
                <h2>Our Global Impact</h2>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-number">${totalUsers}+</div>
                        <div class="stat-label">Active Users</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-link"></i>
                        </div>
                        <div class="stat-number">${totalUrls}+</div>
                        <div class="stat-label">URLs Shortened</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-mouse-pointer"></i>
                        </div>
                        <div class="stat-number">${totalClicks}+</div>
                        <div class="stat-label">Total Clicks</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-globe"></i>
                        </div>
                        <div class="stat-number">${totalCountries}</div>
                        <div class="stat-label">Countries Served</div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Features Section -->
        <section class="features-section">
            <div class="container">
                <h2 class="section-title">Why Choose ShortLink?</h2>
                <div class="features-grid">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-bolt"></i>
                        </div>
                        <h3 class="feature-title">Lightning Fast</h3>
                        <p class="feature-desc">
                            Our high-performance servers ensure your links are shortened instantly and
                            redirect with minimal latency.
                        </p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3 class="feature-title">Detailed Analytics</h3>
                        <p class="feature-desc">
                            Track clicks, geographic locations, referral sources, and more with our
                            comprehensive analytics dashboard.
                        </p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-lock"></i>
                        </div>
                        <h3 class="feature-title">Enterprise Security</h3>
                        <p class="feature-desc">
                            All links are encrypted and protected with industry-standard security
                            protocols to keep your data safe.
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <!-- About Section -->
        <section class="about-section">
            <div class="container">
                <div class="about-card">
                    <h2>About ShortLink</h2>
                    <p>
                        ShortLink is a powerful URL shortening service designed for individuals and businesses
                        who want to create memorable links and gain insights into their audience. Our mission
                        is to make link sharing simple, trackable, and secure.
                    </p>
                    <p>
                        Whether you're sharing links on social media, in emails, or in print materials,
                        ShortLink helps you optimize your sharing strategy with detailed analytics and
                        customizable short URLs.
                    </p>
                </div>
            </div>
        </section>

        <!-- Final CTA -->
        <section class="final-cta">
            <div class="container">
                <h2>Ready to Get Started?</h2>
                <p>
                    Join thousands of users who are already shortening their links and gaining valuable
                    insights into their audience.
                </p>
                <a href="/signup" class="btn btn-primary">Create Your Free Account</a>
            </div>
        </section>
    </main>
</body>
</html>