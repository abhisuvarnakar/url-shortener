<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			<!DOCTYPE html>
			<html>

			<head>
				<title>Dashboard - Shortly</title>
				<style>
					:root {
						--primary-color: #6c63ff;
						--secondary-color: #5a52d6;
						--light-color: #f8f9fa;
						--dark-color: #343a40;
						--danger-color: #e74c3c;
						--success-color: #28a745;
						--warning-color: #ffc107;
						--info-color: #17a2b8;
					}

					body {
						font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
						margin: 0;
						padding: 0;
						background-color: #f5f7fa;
					}

					.dashboard-container {
						display: flex;
						min-height: 100vh;
					}

					.main-content {
						flex: 1;
						padding: 2rem;
					}

					.header {
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 2rem;
					}

					.create-btn {
						background-color: var(--primary-color);
						color: white;
						border: none;
						border-radius: 4px;
						padding: 0.75rem 1.5rem;
						font-size: 1rem;
						font-weight: 500;
						cursor: pointer;
						transition: background-color 0.3s;
						display: flex;
						align-items: center;
						text-decoration: none;
					}

					.create-btn:hover {
						background-color: var(--secondary-color);
					}

					.create-btn i {
						margin-right: 0.5rem;
					}

					/* Overview Grid Styles */
					.overview-grid {
						display: grid;
						grid-template-columns: 1fr 1fr;
						gap: 1.5rem;
						margin-bottom: 2rem;
					}

					.url-form {
						background-color: white;
						padding: 1.5rem;
						border-radius: 8px;
						box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
					}

					.form-group {
						margin-bottom: 1rem;
					}

					.form-group label {
						display: block;
						margin-bottom: 0.5rem;
						font-weight: 500;
					}

					.form-control {
						width: 100%;
						padding: 0.75rem;
						border: 1px solid #ddd;
						border-radius: 4px;
						font-size: 1rem;
					}

					.stats-container {
						background-color: white;
						padding: 1.5rem;
						border-radius: 8px;
						box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
					}

					.stats-grid {
						display: grid;
						grid-template-columns: repeat(2, 1fr);
						gap: 1rem;
					}

					.stat-card {
						padding: 1rem;
						border-radius: 8px;
						text-align: center;
					}

					.stat-card h3 {
						margin: 0;
						font-size: 2rem;
						color: var(--primary-color);
					}

					.stat-card p {
						margin: 0.5rem 0 0;
						color: var(--dark-color);
						font-size: 0.9rem;
					}

					/* Performance Stats */
					.performance-stats {
						display: grid;
						grid-template-columns: repeat(4, 1fr);
						gap: 1.5rem;
						margin-bottom: 2rem;
					}

					.performance-card {
						background-color: white;
						padding: 1.5rem;
						border-radius: 8px;
						box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
					}

					.performance-card h3 {
						margin-top: 0;
						font-size: 1rem;
						color: var(--dark-color);
					}

					.performance-card .value {
						font-size: 1.5rem;
						font-weight: 600;
						margin: 0.5rem 0;
					}

					/* URL Table Styles */
					.url-table {
						width: 100%;
						border-collapse: collapse;
						background-color: white;
						border-radius: 8px;
						overflow: hidden;
						box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
					}

					.url-table th,
					.url-table td {
						padding: 1rem;
						text-align: left;
						border-bottom: 1px solid #eee;
					}

					.url-table th {
						background-color: var(--light-color);
						font-weight: 600;
					}

					.url-table tr:hover {
						background-color: #f9f9f9;
					}

					.short-url {
						color: var(--primary-color);
						font-weight: 500;
					}

					.action-btn {
						background: none;
						border: none;
						cursor: pointer;
						padding: 0.5rem;
						border-radius: 4px;
						transition: background-color 0.3s;
						position: relative;
						font-size: 14px;
					}

					.action-btn:hover {
						background-color: var(--light-color);
					}

					.action-btn[title]:hover::after {
						content: attr(title);
						position: absolute;
						bottom: 100%;
						left: 50%;
						transform: translateX(-50%);
						background-color: #333;
						color: white;
						padding: 0.25rem 0.5rem;
						border-radius: 4px;
						font-size: 0.75rem;
						white-space: nowrap;
						z-index: 1000;
						margin-bottom: 5px;
					}

					.action-btn[title]:hover::before {
						content: '';
						position: absolute;
						bottom: 100%;
						left: 50%;
						transform: translateX(-50%);
						border: 4px solid transparent;
						border-top-color: #333;
						z-index: 1000;
						margin-bottom: 1px;
					}

					.copy-btn {
						color: var(--info-color);
					}

					.edit-btn {
						color: var(--primary-color);
					}

					.analytics-btn {
						color: var(--success-color);
					}

					.delete-btn {
						color: var(--danger-color);
					}

					.regenerate-btn {
						color: var(--warning-color);
					}

					.status-badge {
						display: inline-block;
						padding: 0.25rem 0.5rem;
						border-radius: 12px;
						font-size: 0.75rem;
						font-weight: 500;
					}

					.active {
						background-color: #d4edda;
						color: var(--success-color);
					}

					.expired {
						background-color: #f8d7da;
						color: var(--danger-color);
					}

					/* Pagination Styles */
					.pagination {
						display: flex;
						justify-content: center;
						margin-top: 2rem;
						gap: 0.5rem;
					}

					.page-btn {
						padding: 0.5rem 1rem;
						border: 1px solid #ddd;
						background-color: white;
						border-radius: 4px;
						cursor: pointer;
					}

					.page-btn.active {
						background-color: var(--primary-color);
						color: white;
						border-color: var(--primary-color);
					}

					.page-btn:hover:not(.active) {
						background-color: var(--light-color);
					}

					/* Modal styles */
					.modal {
						display: none;
						position: fixed;
						top: 0;
						left: 0;
						width: 100%;
						height: 100%;
						background-color: rgba(0, 0, 0, 0.5);
						z-index: 1000;
						justify-content: center;
						align-items: center;
					}

					.modal-content {
						background-color: white;
						padding: 2rem;
						border-radius: 8px;
						width: 100%;
						max-width: 500px;
					}

					.modal-actions {
						display: flex;
						justify-content: flex-end;
						margin-top: 1.5rem;
						gap: 1rem;
					}

					.modal-btn {
						padding: 0.5rem 1rem;
						border-radius: 4px;
						cursor: pointer;
						font-weight: 500;
					}

					.modal-cancel {
						background-color: var(--light-color);
						border: 1px solid #ddd;
					}

					.modal-confirm {
						background-color: var(--danger-color);
						color: white;
						border: none;
					}

					.shorten-btn {
						padding: 0.5rem 1rem;
						width: auto;
						margin-top: 0.5rem;
					}

					.no-urls-message {
						text-align: center;
						padding: 2rem;
						background-color: #d4edda;
						color: var(--success-color);
						border-radius: 8px;
						margin: 1rem 0;
					}
				</style>
				<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
			</head>

			<body>
				<jsp:include page="header.jsp" />

				<div class="dashboard-container">

					<!-- Main Content -->
					<div class="main-content">
						<div class="header">
							<h1>Dashboard</h1>
							<a href="/dashboard/view-url" class="create-btn">
								<i class="fas fa-clock"></i> View All URLs
							</a>
						</div>

						<!-- Overview Content -->
						<div id="overview">
							<!-- Top Section - URL Shortener and Stats -->
							<div class="overview-grid">
								<!-- URL Shortener Form -->
								<div class="url-form">
									<h2>Shorten a URL</h2>
									<form id="shortenForm">
										<div class="form-group">
											<label for="longUrl">Long URL</label>
											<input type="url" id="longUrl" class="form-control"
												placeholder="https://example.com" required>
										</div>
										<div class="form-group">
											<label for="customAlias">Custom Alias (optional)</label>
											<input type="text" id="customAlias" class="form-control"
												placeholder="my-custom-link">
										</div>
										<button type="submit" class="create-btn shorten-btn">
											<i class="fas fa-link"></i> Shorten URL
										</button>
									</form>
								</div>

								<!-- Quick Stats -->
								<div class="stats-container">
									<h2>Quick Stats</h2>
									<div class="stats-grid">
										<div class="stat-card">
											<h3>${totalUrls}</h3>
											<p>Total URLs</p>
										</div>
										<div class="stat-card">
											<h3>${totalClicks}</h3>
											<p>Total Clicks</p>
										</div>
										<div class="stat-card">
											<h3>${activeUrls}</h3>
											<p>Active URLs</p>
										</div>
										<div class="stat-card">
											<h3>${avgClicksPerUrl}</h3>
											<p>Avg Clicks/URL</p>
										</div>
									</div>
								</div>
							</div>

							<!-- Performance Stats Cards -->
							<div class="performance-stats">
								<div class="performance-card">
									<h3>Top Performing URL</h3>
									<a href="/${topPerformingUrl.shortUrl}" target="_blank">shortlink.com/${topPerformingUrl.shortUrl}</a>
									<p>${topPerformingUrl.clicks} clicks</p>
								</div>
								<div class="performance-card">
									<h3>Click Rate (24h)</h3>
									<p class="value">${clickRate24h}%</p>
									<p>${clickChange24h >= 0 ? '↑' : '↓'} ${Math.abs(clickChange24h)}% from
										yesterday</p>
								</div>
								<div class="performance-card">
									<h3>Top Country</h3>
									<p class="value">${topCountry.name}</p>
									<p>${topCountry.percentage}% of traffic</p>
								</div>
								<div class="performance-card">
									<h3>Expiring Soon</h3>
									<p class="value">${expiringSoonCount}</p>
									<p>URLs in next 7 days</p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- Delete Confirmation Modal -->
				<div id="deleteModal" class="modal">
					<div class="modal-content">
						<h2>Confirm Deletion</h2>
						<p>Are you sure you want to delete this short URL? This action cannot be undone.</p>
						<div class="modal-actions">
							<button class="modal-btn modal-cancel" onclick="closeModal()">Cancel</button>
							<button class="modal-btn modal-confirm" onclick="deleteUrl()">Delete</button>
						</div>
					</div>
				</div>

				<!-- Regenerate URL Modal -->
				<div id="regenerateModal" class="modal">
					<div class="modal-content">
						<h2>Regenerate Short URL</h2>
						<p>This will create a new short code for this URL. The old short code will stop working.
						</p>
						<div class="modal-actions">
							<button class="modal-btn modal-cancel" onclick="closeModal()">Cancel</button>
							<button class="modal-btn" style="background-color: var(--warning-color); color: white;"
								onclick="confirmRegenerate()">
								Regenerate
							</button>
						</div>
					</div>
				</div>

				<script>
					let currentUrlId = null;

					// Copy text to clipboard
					function copyToClipboard(text) {
						navigator.clipboard.writeText(text)
							.then(() => {
								console.log('Copied to clipboard:', text);
							})
							.catch(err => {
								console.error('Failed to copy:', err);
								alert('Failed to copy to clipboard');
							});
					}

					document.getElementById('shortenForm')?.addEventListener('submit', function (e) {
						e.preventDefault();

						const longUrl = document.getElementById('longUrl').value;
						const customAlias = document.getElementById('customAlias').value;
						const submitBtn = this.querySelector('button[type="submit"]');

						if (!longUrl) {
							alert('Please enter a URL');
							return;
						}

						submitBtn.disabled = true;
						submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Shortening...';

						fetch('/api/url/shorten', {
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
							body: JSON.stringify({
								originalUrl: longUrl,
								customAlias: customAlias || null
							})
						})
							.then(response => {
								if (!response.ok) {
									return response.json().then(err => { throw err; });
								}
								return response.json();
							})
							.then(data => {
								console.log("API Response:", data);

								if (data.status === 'SUCCESS' && data.data && data.data.shortUrl) {
									const shortUrl = data.data.shortUrl;

									alert(`URL shortened successfully!\n\n${shortUrl}`);

									navigator.clipboard.writeText(shortUrl)
										.then(() => {
											console.log("Copied to clipboard");
										})
										.catch(() => {
											console.warn("Clipboard copy failed");
										});

									setTimeout(() => window.location.reload(), 2000);
								} else {
									throw new Error(data.message || 'Invalid response from server');
								}
							})
							.catch(error => {
								console.error('Error:', error);
								alert(error.message || 'An error occurred while shortening the URL');
							})
							.finally(() => {
								submitBtn.disabled = false;
								submitBtn.innerHTML = '<i class="fas fa-link"></i> Shorten URL';
							});
					});

					// Delete URL functions
					function confirmDelete(urlId) {
						currentUrlId = urlId;
						document.getElementById('deleteModal').style.display = 'flex';
					}

					function closeModal() {
						document.querySelectorAll('.modal').forEach(modal => {
							modal.style.display = 'none';
						});
						currentUrlId = null;
					}

					function deleteUrl() {
						if (!currentUrlId) return;

						fetch(`/api/urls/${currentUrlId}`, {
							method: 'DELETE',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
						})
							.then(response => {
								if (response.ok) {
									window.location.reload();
								} else {
									throw new Error('Delete failed');
								}
							})
							.catch(error => {
								console.error('Error:', error);
								alert('Failed to delete URL. Please try again.');
							})
							.finally(() => {
								closeModal();
							});
					}

					// Regenerate URL functions
					function regenerateUrl(urlId) {
						currentUrlId = urlId;
						document.getElementById('regenerateModal').style.display = 'flex';
					}

					function confirmRegenerate() {
						if (!currentUrlId) return;

						fetch(`/api/urls/${currentUrlId}/regenerate`, {
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
						})
							.then(response => {
								if (response.ok) {
									window.location.reload();
								} else {
									throw new Error('Regeneration failed');
								}
							})
							.catch(error => {
								console.error('Error:', error);
								alert('Failed to regenerate URL. Please try again.');
							})
							.finally(() => {
								closeModal();
							});
					}

					// Pagination functions
					function goToPage(page) {
						window.location.href = `/dashboard?page=${page}`;
					}

					// Close modals when clicking outside
					window.onclick = function (event) {
						if (event.target.className === 'modal') {
							closeModal();
						}
					};
				</script>
			</body>

			</html>