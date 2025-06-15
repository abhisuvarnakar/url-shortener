<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="true" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>URL Analytics</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            <style>
                .stat-card {
                    border-left: 4px solid;
                    transition: transform 0.2s;
                }
                .stat-card:hover {
                    transform: translateY(-2px);
                }
                .chart-container {
                    position: relative;
                    height: 400px;
                }
                .country-flag {
                    width: 20px;
                    height: auto;
                    margin-right: 8px;
                }
                .analytics-column {
                    margin-bottom: 20px;
                }
                .loading-spinner {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 200px;
                }
                .error-message {
                    color: #dc3545;
                    background-color: #f8d7da;
                    border: 1px solid #f5c6cb;
                    padding: 10px;
                    border-radius: 5px;
                    margin: 10px 0;
                }
            </style>
        </head>
        <body>
            <div class="container-fluid py-4">
                <!-- Main Analytics Content -->
                <div class="main-content">
                    <!-- URL Information (will be populated via API) -->
                    <div class="url-info mb-4">
                        <h5 class="mb-2">
                            <i class="fas fa-chart-line me-2"></i>
                            Analytics for: <strong id="shortUrl">Loading...</strong>
                        </h5>
                        <p class="mb-0 text-muted">
                            Original URL: <a href="#" id="originalUrl" target="_blank" class="text-decoration-none">Loading...</a>
                        </p>
                    </div>

                    <!-- Date Range Control -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <label for="dateRange" class="form-label fw-semibold">Date Range</label>
                            <select class="form-select" id="dateRange">
                                <option value="1">Past 1 day</option>
                                <option value="7" selected>Past 7 days</option>
                                <option value="30">Past 30 days</option>
                                <option value="90">Past 3 months</option>
                                <option value="180">Past 6 months</option>
                                <option value="365">Past 1 year</option>
                            </select>
                        </div>
                    </div>

                    <!-- Loading Indicator -->
                    <div id="loadingIndicator" class="loading-spinner">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>

                    <!-- Error Message -->
                    <div id="errorMessage" class="error-message d-none">
                        Failed to load analytics data. Please try again.
                    </div>

                    <!-- Overview Statistics -->
                    <div class="row mb-4" id="statsSection" style="display: none;">
                        <div class="col-md-3 mb-3">
                            <div class="card stat-card h-100" style="border-left-color: #0d6efd;">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted">Total Clicks</h6>
                                    <h3 class="card-title text-primary" id="totalClicks">0</h3>
                                    <p class="card-text text-muted small">All time clicks</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card stat-card h-100" style="border-left-color: #198754;">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted">Today</h6>
                                    <h3 class="card-title text-success" id="todayClicks">0</h3>
                                    <p class="card-text text-muted small" id="todayChange">0% from yesterday</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card stat-card h-100" style="border-left-color: #fd7e14;">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted">This Week</h6>
                                    <h3 class="card-title text-warning" id="weekClicks">0</h3>
                                    <p class="card-text text-muted small" id="weekChange">0% from last week</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card stat-card h-100" style="border-left-color: #6f42c1;">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted">This Month</h6>
                                    <h3 class="card-title" style="color: #6f42c1;" id="monthClicks">0</h3>
                                    <p class="card-text text-muted small" id="monthChange">0% from last month</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Main Chart Section -->
                    <div class="card mb-4" id="chartSection" style="display: none;">
                        <div class="card-header bg-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-chart-area me-2 text-primary"></i>Clicks Over Time
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="clicksChart"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Analytics Grid -->
                    <div class="row" id="analyticsGrid" style="display: none;">
                        <!-- Top Countries -->
                        <div class="col-md-6 analytics-column">
                            <div class="card h-100">
                                <div class="card-header bg-white">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-globe me-2 text-primary"></i>Top Countries
                                    </h5>
                                </div>
                                <div class="card-body" id="countriesData">
                                    <p class="text-muted">Loading...</p>
                                </div>
                            </div>
                        </div>

                        <!-- Device Types -->
                        <div class="col-md-6 analytics-column">
                            <div class="card h-100">
                                <div class="card-header bg-white">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-mobile-alt me-2 text-primary"></i>Device Types
                                    </h5>
                                </div>
                                <div class="card-body" id="devicesData">
                                    <p class="text-muted">Loading...</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="browsersRow" style="display: none;">
                        <!-- Browsers -->
                        <div class="col-md-6 analytics-column">
                            <div class="card h-100">
                                <div class="card-header bg-white">
                                    <h5 class="card-title mb-0">
                                        <i class="fab fa-chrome me-2 text-primary"></i>Browsers
                                    </h5>
                                </div>
                                <div class="card-body" id="browsersData">
                                    <p class="text-muted">Loading...</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                class UrlAnalytics {
                    constructor(urlId) {
                        this.urlId = urlId;
                        this.clicksChart = null;
                        this.currentDays = 7;
                        this.init();
                    }

                    init() {
                        this.initializeChart();
                        this.setupEventListeners();
                        this.fetchAnalyticsData(this.currentDays);
                    }

                    setupEventListeners() {
                        document.getElementById('dateRange').addEventListener('change', (e) => {
                            this.currentDays = parseInt(e.target.value);
                            this.fetchAnalyticsData(this.currentDays);
                        });
                    }

                    initializeChart() {
                        const ctx = document.getElementById('clicksChart').getContext('2d');
                        this.clicksChart = new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: [],
                                datasets: [{
                                    label: 'Clicks',
                                    data: [],
                                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                                    borderColor: 'rgba(13, 110, 253, 1)',
                                    borderWidth: 2,
                                    tension: 0.1,
                                    fill: true
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    tooltip: {
                                        mode: 'index',
                                        intersect: false
                                    },
                                    legend: {
                                        display: false
                                    }
                                },
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        ticks: {
                                            precision: 0
                                        }
                                    }
                                }
                            }
                        });
                    }

                    showLoading() {
                        document.getElementById('loadingIndicator').style.display = 'flex';
                        document.getElementById('errorMessage').classList.add('d-none');
                        document.getElementById('statsSection').style.display = 'none';
                        document.getElementById('chartSection').style.display = 'none';
                        document.getElementById('analyticsGrid').style.display = 'none';
                        document.getElementById('browsersRow').style.display = 'none';
                    }

                    hideLoading() {
                        document.getElementById('loadingIndicator').style.display = 'none';
                        document.getElementById('statsSection').style.display = 'flex';
                        document.getElementById('chartSection').style.display = 'block';
                        document.getElementById('analyticsGrid').style.display = 'flex';
                        document.getElementById('browsersRow').style.display = 'flex';
                    }

                    showError() {
                        document.getElementById('loadingIndicator').style.display = 'none';
                        document.getElementById('errorMessage').classList.remove('d-none');
                    }

                    async fetchAnalyticsData(days) {
                        this.showLoading();

                        try {
                            const response = await fetch(`/api/analytics/${this.urlId}?days=${days}`, {
                                method: 'GET',
                                credentials: 'include', // Include cookies
                                headers: {
                                    'Content-Type': 'application/json',
                                }
                            });

                            if (!response.ok) {
                                throw new Error(`HTTP error! status: ${response.status}`);
                            }

                            const apiResponse = await response.json();

                            if (apiResponse.status === 'SUCCESS') {
                                this.updateUI(apiResponse.data);
                                this.hideLoading();
                            } else {
                                throw new Error(apiResponse.message || 'Unknown API error');
                            }

                        } catch (error) {
                            console.error('Error fetching analytics:', error);
                            this.showError();
                        }
                    }

                    updateUI(data) {
                        // Update URL information
                        document.getElementById('shortUrl').textContent = data.shortUrl || 'N/A';
                        const originalUrlLink = document.getElementById('originalUrl');
                        originalUrlLink.textContent = data.originalUrl || 'N/A';
                        originalUrlLink.href = data.originalUrl || '#';

                        // Update statistics
                        document.getElementById('totalClicks').textContent = this.formatNumber(data.totalClicks || 0);
                        document.getElementById('todayClicks').textContent = this.formatNumber(data.todayClicks || 0);
                        document.getElementById('weekClicks').textContent = this.formatNumber(data.weekClicks || 0);
                        document.getElementById('monthClicks').textContent = this.formatNumber(data.monthClicks || 0);

                        // Update change percentages
                        document.getElementById('todayChange').textContent = `${this.formatPercentage(data.todayChange)} from yesterday`;
                        document.getElementById('weekChange').textContent = `${this.formatPercentage(data.weekChange)} from last week`;
                        document.getElementById('monthChange').textContent = `${this.formatPercentage(data.monthChange)} from last month`;

                        // Update chart
                        if (this.clicksChart && data.chartLabels && data.chartData) {
                            this.clicksChart.data.labels = data.chartLabels;
                            this.clicksChart.data.datasets[0].data = data.chartData;
                            this.clicksChart.update();
                        }

                        // Update sections
                        this.updateCountriesSection(data.topCountries || []);
                        this.updateDevicesSection(data.deviceTypes || []);
                        this.updateBrowsersSection(data.browsers || []);
                    }

                    updateCountriesSection(countries) {
                        const container = document.getElementById('countriesData');
                        if (countries.length === 0) {
                            container.innerHTML = '<p class="text-muted">No data available</p>';
                            return;
                        }

                        container.innerHTML = countries.map(country => `
                            <div class="mb-3">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>
                                        <img src="https://flagcdn.com/w20/${country.code.toLowerCase()}.png"
                                             class="country-flag" alt="${country.name}"
                                             onerror="this.style.display='none'">
                                        ${country.name}
                                    </span>
                                    <span class="fw-semibold">${this.formatNumber(country.clicks)} (${country.percentage}%)</span>
                                </div>
                                <div class="progress">
                                    <div class="progress-bar bg-primary" role="progressbar"
                                         style="width: ${country.percentage}%"></div>
                                </div>
                            </div>
                        `).join('');
                    }

                    updateDevicesSection(devices) {
                        const container = document.getElementById('devicesData');
                        if (devices.length === 0) {
                            container.innerHTML = '<p class="text-muted">No data available</p>';
                            return;
                        }

                        container.innerHTML = devices.map(device => `
                            <div class="mb-3">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>
                                        <i class="fas ${device.icon} me-2"></i>
                                        ${device.type}
                                    </span>
                                    <span class="fw-semibold">${this.formatNumber(device.clicks)} (${device.percentage}%)</span>
                                </div>
                                <div class="progress">
                                    <div class="progress-bar" role="progressbar"
                                         style="width: ${device.percentage}%; background-color: ${device.color}"></div>
                                </div>
                            </div>
                        `).join('');
                    }

                    updateBrowsersSection(browsers) {
                        const container = document.getElementById('browsersData');
                        if (browsers.length === 0) {
                            container.innerHTML = '<p class="text-muted">No data available</p>';
                            return;
                        }

                        container.innerHTML = browsers.map(browser => `
                            <div class="mb-3">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>
                                        <i class="fab ${browser.icon} me-2"></i>
                                        ${browser.name}
                                    </span>
                                    <span class="fw-semibold">${this.formatNumber(browser.clicks)} (${browser.percentage}%)</span>
                                </div>
                                <div class="progress">
                                    <div class="progress-bar bg-warning" role="progressbar"
                                         style="width: ${browser.percentage}%"></div>
                                </div>
                            </div>
                        `).join('');
                    }

                    formatNumber(num) {
                        if (num >= 1000000) {
                            return (num / 1000000).toFixed(1) + 'M';
                        }
                        if (num >= 1000) {
                            return (num / 1000).toFixed(1) + 'K';
                        }
                        return num.toString();
                    }

                    formatPercentage(value) {
                        if (value === null || value === undefined) return '0%';
                        const formatted = parseFloat(value).toFixed(1);
                        return `${formatted >= 0 ? '+' : ''}${formatted}%`;
                    }
                }

                // Initialize analytics when DOM is loaded
                document.addEventListener('DOMContentLoaded', function() {
                    // Solution 1: Get URL ID from URL parameters
                    const urlParams = new URLSearchParams(window.location.search);
                    const urlId = urlParams.get('urlId') || window.location.pathname.split('/').pop();

                    if (urlId) {
                        new UrlAnalytics(urlId);
                    } else {
                        console.error('URL ID not found');
                        document.getElementById('errorMessage').classList.remove('d-none');
                        document.getElementById('errorMessage').textContent = 'URL ID not found';
                        document.getElementById('loadingIndicator').style.display = 'none';
                    }
                });
            </script>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>
        </html>