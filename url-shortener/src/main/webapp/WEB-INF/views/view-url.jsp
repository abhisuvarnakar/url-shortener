<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

			<!DOCTYPE html>
			<html>

			<head>
				<title>View All URLs - Shortlink</title>
				<style>
					/* Include all the CSS from dashboard.jsp here */
					:root {
						--primary-color: #6c63ff;
						--secondary-color: #5a52d6;
						--light-color: #f8f9fa;
						--dark-color: #343a40;
						--danger-color: #e74c3c;
						--success-color: #28a745;
						--warning-color: #ffc107;
						--info-color: #17a2b8;
						--muted-color: #6c757d;
						--border-color: #e9ecef;
					}

					body {
						font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
						margin: 0;
						padding: 0;
						background-color: #f5f7fa;
						line-height: 1.6;
					}

					.dashboard-container {
						display: flex;
						min-height: 100vh;
					}

					.main-content {
						flex: 1;
						padding: 2rem;
						max-width: 1200px;
						margin: 0 auto;
					}

					.header {
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 2rem;
					}

					.header h1 {
						color: var(--dark-color);
						font-size: 2rem;
						font-weight: 600;
						margin: 0;
					}

					/* Search and Filter Section */
					.search-section {
						background-color: white;
						padding: 1.5rem;
						border-radius: 8px;
						box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
						margin-bottom: 2rem;
					}

					.search-form {
						display: flex;
						flex-direction: column;
						gap: 1.5rem;
					}

					.search-line {
						display: grid;
						grid-template-columns: repeat(3, 1fr);
						gap: 1.5rem;
						align-items: end;
					}

					.form-group {
						margin-bottom: 0;
					}

					.form-group label {
						display: block;
						margin-bottom: 0.5rem;
						font-weight: 600;
						font-size: 0.9rem;
						color: var(--dark-color);
					}

					.form-input,
					.form-select {
						width: 100%;
						padding: 0.75rem;
						border: 1px solid var(--border-color);
						border-radius: 4px;
						font-size: 1rem;
						transition: border-color 0.3s;
						box-sizing: border-box;
					}

					.form-input:focus,
					.form-select:focus {
						outline: none;
						border-color: var(--primary-color);
					}

					.date-range {
						display: grid;
						grid-template-columns: 1fr 1fr;
						gap: 0.5rem;
					}

					.clicks-range {
						display: grid;
						grid-template-columns: 1fr auto 1fr;
						gap: 0.5rem;
						align-items: center;
					}

					.range-separator {
						text-align: center;
						color: var(--muted-color);
					}

					.search-actions {
						display: flex;
						justify-content: flex-end;
						gap: 1rem;
						margin-top: 1rem;
					}

					.btn {
						padding: 0.75rem 1.5rem;
						border-radius: 4px;
						font-weight: 500;
						cursor: pointer;
						transition: all 0.3s;
					}

					.btn-primary {
						background-color: var(--primary-color);
						color: white;
						border: none;
					}

					.btn-primary:hover {
						background-color: var(--secondary-color);
					}

					.btn-secondary {
						background-color: var(--light-color);
						color: var(--dark-color);
						border: 1px solid var(--border-color);
					}

					.btn-secondary:hover {
						background-color: var(--border-color);
					}

					/* Results Section */
					.results-header {
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 1.5rem;
					}

					.results-count {
						color: var(--muted-color);
						font-size: 1rem;
					}

					.url-cards {
						display: grid;
						grid-template-columns: 1fr;
						gap: 1.5rem;
					}

					.url-card {
						background-color: white;
						padding: 2rem;
						border-radius: 12px;
						box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
						border: 1px solid var(--border-color);
						transition: transform 0.2s ease, box-shadow 0.2s ease;
					}

					.url-card:hover {
						transform: translateY(-2px);
						box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
					}

					.url-card-header {
						display: flex;
						justify-content: space-between;
						align-items: flex-start;
						margin-bottom: 1.5rem;
					}

					.url-info {
						flex: 1;
						min-width: 0;
					}

					.url-info h3 {
						margin: 0 0 0.75rem;
						font-size: 1.25rem;
						font-weight: 600;
					}

					.short-url {
						color: var(--primary-color);
						font-weight: 600;
						text-decoration: none;
						font-size: 1.1rem;
						transition: color 0.3s ease;
					}

					.short-url:hover {
						color: var(--secondary-color);
						text-decoration: underline;
					}

					.original-url {
						color: var(--muted-color);
						text-decoration: none;
						font-size: 0.95rem;
						transition: color 0.3s ease;
						word-break: break-all;
						display: block;
						margin: 0.5rem 0;
					}

					.original-url:hover {
						color: var(--dark-color);
					}

					.url-status {
						display: inline-block;
						padding: 0.25rem 0.75rem;
						border-radius: 20px;
						font-size: 0.8rem;
						font-weight: 600;
						text-transform: uppercase;
						letter-spacing: 0.5px;
						margin: 0.5rem 0;
					}

					.status-active {
						background-color: rgba(40, 167, 69, 0.1);
						color: var(--success-color);
					}

					.status-expired {
						background-color: rgba(231, 76, 60, 0.1);
						color: var(--danger-color);
					}

					.status-inactive {
						background-color: rgba(108, 117, 125, 0.1);
						color: var(--muted-color);
					}

					.url-stats {
						display: grid;
						grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
						gap: 1rem;
						margin-top: 1rem;
						padding-top: 1rem;
						border-top: 1px solid var(--border-color);
					}

					.url-stats .stat-item:first-child {
						order: 2;
					}

					.url-stats .stat-item:nth-child(2) {
						order: 1;
					}

					.url-stats .stat-item:nth-child(3) {
						order: 3;
					}

					.stat-item {
						display: flex;
						flex-direction: column;
						align-items: flex-start;
					}

					.stat-item.click-count-item {
						grid-column: 1 / -1;
					}

					.stat-label {
						font-size: 0.85rem;
						color: var(--muted-color);
						font-weight: 500;
						text-transform: uppercase;
						letter-spacing: 0.5px;
						margin-bottom: 0.25rem;
					}

					.stat-value {
						font-size: 1.1rem;
						font-weight: 600;
						color: var(--dark-color);
					}

					.click-count {
						color: var(--info-color);
					}

					.url-actions {
						display: flex;
						gap: 0.75rem;
						flex-shrink: 0;
					}

					.action-btn {
						background: none;
						border: 2px solid var(--border-color);
						cursor: pointer;
						padding: 1rem;
						border-radius: 8px;
						transition: all 0.3s ease;
						position: relative;
						font-size: 1.1rem;
						width: 50px;
						height: 50px;
						display: flex;
						align-items: center;
						justify-content: center;
						background-color: white;
					}

					.action-btn:hover {
						transform: translateY(-2px);
						box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
					}

					.action-btn[title]:hover::after {
						content: attr(title);
						position: absolute;
						bottom: 120%;
						left: 50%;
						transform: translateX(-50%);
						background-color: #333;
						color: white;
						padding: 0.5rem 0.75rem;
						border-radius: 6px;
						font-size: 0.8rem;
						white-space: nowrap;
						z-index: 1000;
						margin-bottom: 5px;
						box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
					}

					.action-btn[title]:hover::before {
						content: '';
						position: absolute;
						bottom: 120%;
						left: 50%;
						transform: translateX(-50%);
						border: 6px solid transparent;
						border-top-color: #333;
						z-index: 1000;
						margin-bottom: -1px;
					}

					.extend-btn {
						color: var(--success-color);
						border-color: var(--success-color);
					}

					.extend-btn:hover {
						background-color: var(--success-color);
						color: white;
					}

					.analytics-btn {
						color: var(--info-color);
						border-color: var(--info-color);
					}

					.analytics-btn:hover {
						background-color: var(--info-color);
						color: white;
					}

					.regenerate-btn {
						color: var(--warning-color);
						border-color: var(--warning-color);
					}

					.regenerate-btn:hover {
						background-color: var(--warning-color);
						color: white;
					}

					.delete-btn {
						color: var(--danger-color);
						border-color: var(--danger-color);
					}

					.delete-btn:hover {
						background-color: var(--danger-color);
						color: white;
					}

					.no-urls-message {
						text-align: center;
						padding: 3rem 2rem;
						background-color: #d4edda;
						color: var(--success-color);
						border-radius: 12px;
						margin: 2rem 0;
						font-size: 1.1rem;
					}

					.no-urls-message i {
						font-size: 2rem;
						margin-bottom: 1rem;
						display: block;
					}

					/* Pagination Styles */
					.pagination {
						display: flex;
						justify-content: center;
						margin-top: 3rem;
						gap: 0.5rem;
					}

					.page-btn {
						padding: 0.75rem 1.25rem;
						border: 2px solid var(--border-color);
						background-color: white;
						border-radius: 8px;
						cursor: pointer;
						font-weight: 500;
						transition: all 0.3s ease;
					}

					.page-btn.active {
						background-color: var(--primary-color);
						color: white;
						border-color: var(--primary-color);
					}

					.page-btn:hover:not(.active) {
						background-color: var(--light-color);
						border-color: var(--primary-color);
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
						padding: 2.5rem;
						border-radius: 12px;
						width: 100%;
						max-width: 500px;
						box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
					}

					.modal-content h2 {
						margin: 0 0 1.5rem;
						color: var(--dark-color);
					}

					.modal-content p {
						color: var(--muted-color);
						margin-bottom: 1.5rem;
						line-height: 1.6;
					}

					.modal-form-group {
						margin-bottom: 1.5rem;
					}

					.modal-form-group label {
						display: block;
						margin-bottom: 0.5rem;
						font-weight: 600;
						color: var(--dark-color);
					}

					.modal-form-group input,
					.modal-form-group select {
						width: 100%;
						padding: 0.75rem;
						border: 1px solid var(--border-color);
						border-radius: 4px;
						font-size: 1rem;
					}

					.modal-actions {
						display: flex;
						justify-content: flex-end;
						gap: 1rem;
						margin-top: 1.5rem;
					}

					.modal-btn {
						padding: 0.75rem 1.5rem;
						border-radius: 8px;
						cursor: pointer;
						font-weight: 600;
						border: none;
						transition: all 0.3s ease;
					}

					.modal-cancel {
						background-color: var(--light-color);
						color: var(--dark-color);
						border: 2px solid var(--border-color);
					}

					.modal-cancel:hover {
						background-color: var(--border-color);
					}

					.modal-confirm {
						background-color: var(--danger-color);
						color: white;
					}

					.modal-confirm:hover {
						background-color: #c0392b;
					}

					/* Custom Alias Input */
					.custom-alias-input {
						margin-top: 1rem;
					}

					.custom-alias-input label {
						display: block;
						margin-bottom: 0.5rem;
						font-weight: 600;
						color: var(--dark-color);
					}

					.custom-alias-input input {
						width: 100%;
						padding: 0.75rem;
						border: 1px solid var(--border-color);
						border-radius: 4px;
						font-size: 1rem;
					}

					/* Responsive Design */
					@media (max-width: 992px) {
						.search-line {
							grid-template-columns: 1fr 1fr;
						}

						.search-line:first-child .form-group:last-child {
							grid-column: 1 / -1;
						}
					}

					@media (max-width: 768px) {
						.main-content {
							padding: 1rem;
						}

						.search-line {
							grid-template-columns: 1fr;
						}

						.search-actions {
							justify-content: stretch;
						}

						.search-actions .btn {
							flex: 1;
						}

						.results-header {
							flex-direction: column;
							gap: 1rem;
							align-items: stretch;
						}

						.url-card {
							padding: 1.5rem;
						}

						.url-card-header {
							flex-direction: column;
							gap: 1rem;
						}

						.url-actions {
							justify-content: flex-start;
						}

						.action-btn {
							width: 45px;
							height: 45px;
							font-size: 1rem;
						}

						.url-stats {
							grid-template-columns: repeat(2, 1fr);
						}
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
							<h1>View All URLs</h1>
						</div>

						<!-- Search and Filter Section -->
						<div class="search-section">
							<form class="search-form" id="searchForm">
								<!-- Line 1: Search Term, URL Status, Created Date -->
								<div class="search-line">
									<!-- Search Term -->
									<div class="form-group">
										<label for="searchTerm">Search Term</label>
										<input type="text" id="searchTerm" name="searchTerm" class="form-input"
											placeholder="Original or Short URL" value="${param.searchTerm}">
									</div>

									<!-- URL Status -->
									<div class="form-group">
										<label for="status">URL Status</label>
										<select id="status" name="status" class="form-select">
											<option value="">All Statuses</option>
											<option value="ACTIVE" ${param.status=='ACTIVE' ? 'selected' : '' }>Active
											</option>
											<option value="EXPIRED" ${param.status=='EXPIRED' ? 'selected' : '' }>
												Expired</option>
										</select>
									</div>

									<!-- Created Date Range -->
									<div class="form-group">
										<label>Created Date</label>
										<div class="date-range">
											<input type="date" name="fromDate" class="form-input"
												value="${param.fromDate}">
											<input type="date" name="toDate" class="form-input" value="${param.toDate}">
										</div>
									</div>
								</div>

								<!-- Line 2: Click Count, Sort By, Sort Order -->
								<div class="search-line">
									<!-- Click Count Range -->
									<div class="form-group">
										<label>Click Count</label>
										<div class="clicks-range">
											<input type="number" name="minClicks" class="form-input" placeholder="Min"
												value="${param.minClicks}" min="0">
											<span class="range-separator">to</span>
											<input type="number" name="maxClicks" class="form-input" placeholder="Max"
												value="${param.maxClicks}" min="0">
										</div>
									</div>

									<!-- Sort By -->
									<div class="form-group">
										<label for="sortField">Sort By</label>
										<select id="sortField" name="sortField" class="form-select">
											<option value="createdAt" ${empty param.sortField ||
												param.sortField=='createdAt' ? 'selected' : '' }>Created Date</option>
											<option value="clickCount" ${param.sortField=='clickCount' ? 'selected' : ''
												}>Click Count</option>
										</select>
									</div>

									<!-- Sort Order -->
									<div class="form-group">
										<label for="sortOrder">Sort Order</label>
										<select id="sortOrder" name="sortOrder" class="form-select">
											<option value="DESC" ${empty param.sortOrder || param.sortOrder=='DESC'
												? 'selected' : '' }>Descending</option>
											<option value="ASC" ${param.sortOrder=='ASC' ? 'selected' : '' }>Ascending
											</option>
										</select>
									</div>
								</div>

								<!-- Search Actions -->
								<div class="search-actions">
									<button type="reset" class="btn btn-secondary"
										onclick="resetSearch()">Reset</button>
									<button type="submit" class="btn btn-primary">Search</button>
								</div>
							</form>
						</div>

						<!-- Results Header -->
						<div class="results-header">
							<div class="results-count">
								<c:choose>
									<c:when test="${not empty allUrls}">
										Showing ${fn:length(allUrls)} of ${totalUrls} URLs
									</c:when>
									<c:otherwise>
										No URLs found
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<!-- URL Cards -->
						<div class="url-cards">
							<c:choose>
								<c:when test="${not empty allUrls}">
									<c:forEach items="${allUrls}" var="url">
										<div class="url-card">
											<div class="url-card-header">
												<div class="url-info">
													<h3>
														<a href="/${url.shortCode}" target="_blank" class="short-url">
															shortlink.com/${url.shortCode}
														</a>
													</h3>
													<a href="${url.originalUrl}" target="_blank" class="original-url">
														<i class="fas fa-external-link-alt"
															style="margin-right: 0.5rem; font-size: 0.8rem;"></i>
														<c:out value="${fn:substring(url.originalUrl, 0, 80)}" />
														<c:if test="${fn:length(url.originalUrl) > 80}">...</c:if>
													</a>

													<span class="url-status status-${url.status}">
														<c:choose>
															<c:when test="${url.status == 'active'}">
																<i class="fas fa-check-circle"></i> Active
															</c:when>
															<c:when test="${url.status == 'expired'}">
																<i class="fas fa-times-circle"></i> Expired
															</c:when>
															<c:otherwise>
																<i class="fas fa-pause-circle"></i> Inactive
															</c:otherwise>
														</c:choose>
													</span>

													<div class="url-stats">
														<div class="stat-item">
															<span class="stat-label">Created</span>
															<span class="stat-value">
																<i class="fas fa-calendar"
																	style="margin-right: 0.5rem;"></i>
																${url.createdAt != null ? url.createdAt : 'N/A'}
															</span>
														</div>

														<c:if test="${url.expiresAt != null}">
															<div class="stat-item">
																<span class="stat-label">Expires</span>
																<span class="stat-value">
																	<i class="fas fa-clock"
																		style="margin-right: 0.5rem;"></i>
																	${url.expiresAt}
																</span>
															</div>
														</c:if>

														<div class="stat-item click-count-item">
															<span class="stat-label">Total Clicks</span>
															<span class="stat-value click-count">
																<i class="fas fa-mouse-pointer"
																	style="margin-right: 0.5rem;"></i>
																${url.clickCount}
															</span>
														</div>
													</div>
												</div>
												<div class="url-actions">
													<button class="action-btn extend-btn" title="Extend URL Expiry"
														onclick="extendUrlExpiry('${url.id}')">
														<i class="fas fa-calendar-plus"></i>
													</button>
													<button class="action-btn analytics-btn" title="View Analytics"
														onclick="window.open('/url-analytics/${url.id}', '_blank', 'noopener,noreferrer')">
														<i class="fas fa-chart-bar"></i>
													</button>
													<button class="action-btn regenerate-btn" title="Regenerate URL"
														onclick="regenerateUrl('${url.id}')">
														<i class="fas fa-sync-alt"></i>
													</button>
													<button class="action-btn delete-btn" title="Delete URL"
														onclick="confirmDelete('${url.id}')">
														<i class="fas fa-trash"></i>
													</button>
												</div>
											</div>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<div class="no-urls-message">
										<i class="fas fa-search"></i>
										<div>No URLs found matching your search criteria.</div>
									</div>
								</c:otherwise>
							</c:choose>
						</div>

						<!-- Pagination -->
						<c:if test="${not empty allUrls and totalPages > 1}">
							<div class="pagination">
								<c:choose>
									<c:when test="${currentPage > 1}">
										<button class="page-btn" onclick="goToPage(${currentPage - 1})">
											<i class="fas fa-chevron-left"></i> Previous
										</button>
									</c:when>
									<c:otherwise>
										<button class="page-btn" disabled>
											<i class="fas fa-chevron-left"></i> Previous
										</button>
									</c:otherwise>
								</c:choose>

								<c:forEach begin="1" end="${totalPages}" var="i">
									<button class="page-btn ${currentPage == i ? 'active' : ''}"
										onclick="goToPage(${i})">
										${i}
									</button>
								</c:forEach>

								<c:choose>
									<c:when test="${currentPage < totalPages}">
										<button class="page-btn" onclick="goToPage(${currentPage + 1})">
											Next <i class="fas fa-chevron-right"></i>
										</button>
									</c:when>
									<c:otherwise>
										<button class="page-btn" disabled>
											Next <i class="fas fa-chevron-right"></i>
										</button>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
					</div>
				</div>

				<!-- Delete Confirmation Modal -->
				<div id="deleteModal" class="modal">
					<div class="modal-content">
						<h2><i class="fas fa-exclamation-triangle"
								style="color: var(--danger-color); margin-right: 0.5rem;"></i>Confirm Deletion</h2>
						<p>Are you sure you want to delete this short URL? This action cannot be undone and all
							analytics data will be permanently lost.</p>
						<div class="modal-actions">
							<button class="modal-btn modal-cancel" onclick="closeModal()">Cancel</button>
							<button class="modal-btn modal-confirm" onclick="deleteUrl()">Delete URL</button>
						</div>
					</div>
				</div>

				<!-- Regenerate URL Modal -->
				<div id="regenerateModal" class="modal">
					<div class="modal-content">
						<h2><i class="fas fa-sync-alt"
								style="color: var(--warning-color); margin-right: 0.5rem;"></i>Regenerate Short URL</h2>
						<p>Generating a new short code will immediately deactivate the existing one.
							You'll need to update any shared links, and all associated data such as click analytics will
							be permanently removed.</p>

						<div class="modal-form-group">
							<label for="customAlias">Custom Alias (optional):</label>
							<input type="text" id="customAlias" name="customAlias" placeholder="Enter custom alias">
						</div>

						<div class="modal-actions">
							<button class="modal-btn modal-cancel" onclick="closeModal()">Cancel</button>
							<button class="modal-btn" style="background-color: var(--warning-color); color: white;"
								onclick="confirmRegenerate()">
								Regenerate URL
							</button>
						</div>
					</div>
				</div>

				<!-- Updated Extend URL Expiry Modal -->
				<div id="extendExpiryModal" class="modal">
					<div class="modal-content">
						<h2><i class="fas fa-calendar-plus"
								style="color: var(--success-color); margin-right: 0.5rem;"></i>Extend URL Expiry</h2>
						<p>Select how many days you want to extend this URL's expiry date:</p>

						<div class="modal-form-group">
							<label for="extensionDays">Extension Days:</label>
							<select id="extensionDays" class="form-select">
								<option value="7">7 days</option>
								<option value="14">14 days</option>
								<option value="30" selected>30 days</option>
								<option value="60">60 days</option>
								<option value="90">90 days</option>
							</select>
						</div>

						<div class="modal-actions">
							<button class="modal-btn modal-cancel" onclick="closeModal()">Cancel</button>
							<button class="modal-btn" style="background-color: var(--success-color); color: white;"
								onclick="confirmExtendExpiry()">
								Extend Expiry
							</button>
						</div>
					</div>
				</div>

				<script>
					// Enhanced JavaScript code for search operations
					let currentUrlId = null;
					let currentPage = ${ empty param.page ?0: param.page - 1 };
					let urlsPerPage = ${ empty param.urlsPerPage ?10: param.urlsPerPage };

					function closeModal() {
						document.querySelectorAll('.modal').forEach(modal => {
							modal.style.display = 'none';
						});
						currentUrlId = null;
					}

					function confirmDelete(urlId) {
						console.log("Deleting URL ID:", urlId);

						if (!urlId || urlId === 'null' || urlId === 'undefined') {
							console.error("Invalid URL ID provided for deletion:", urlId);
							alert("Error: Invalid URL ID. Cannot delete URL.");
							return;
						}

						currentUrlId = urlId;
						document.getElementById('deleteModal').style.display = 'flex';
					}

					function deleteUrl() {
						if (!currentUrlId || currentUrlId === 'null' || currentUrlId === 'undefined') {
							console.error("No valid URL ID set for deletion:", currentUrlId);
							alert("Error: No valid URL ID. Cannot delete URL.");
							return;
						}

						console.log("Attempting to delete URL with ID:", currentUrlId);

						// Pass URL ID in request body
						fetch('/api/url/deleteUrl', {
							method: 'DELETE',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
							body: JSON.stringify({
								urlId: currentUrlId
							})
						})
							.then(response => {
								if (!response.ok) {
									return response.text().then(text => {
										throw new Error(text || 'Deletion failed');
									});
								}
								return response.json();
							})
							.then(data => {
								console.log("Delete successful:", data);
								alert("URL deleted successfully!");
								performSearch();
							})
							.catch(error => {
								console.error('Deletion error:', error);
								alert(error.message || 'Failed to delete URL');
							})
							.finally(() => {
								closeModal();
							});
					}

					function regenerateUrl(urlId) {
						console.log("Regenerating URL ID:", urlId);

						if (!urlId || urlId === 'null' || urlId === 'undefined') {
							console.error("Invalid URL ID provided for regeneration:", urlId);
							alert("Error: Invalid URL ID. Cannot regenerate URL.");
							return;
						}

						currentUrlId = urlId;
						document.getElementById('customAlias').value = ''; // Clear previous input
						document.getElementById('regenerateModal').style.display = 'flex';
					}

					function confirmRegenerate() {
						if (!currentUrlId || currentUrlId === 'null' || currentUrlId === 'undefined') {
							console.error("No valid URL ID set for regeneration:", currentUrlId);
							alert("Error: No valid URL ID. Cannot regenerate URL.");
							return;
						}

						const customAlias = document.getElementById('customAlias').value.trim();
						const requestBody = {
							urlId: currentUrlId
						};

						if (customAlias) {
							requestBody.customAlias = customAlias;
						}

						console.log("Attempting to regenerate URL with ID:", currentUrlId, "and alias:", customAlias);

						fetch('/api/url/regenerateUrl', {
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
							body: JSON.stringify(requestBody)
						})
							.then(response => {
								if (!response.ok) {
									return response.json().then(errorData => {
										// Check if the response has a message
										const errorMessage = errorData.message || 'Regeneration failed';
										throw new Error(errorMessage);
									});
								}
								return response.json();
							})
							.then(data => {
								console.log("Regenerate response:", data);
								if (data.status === "SUCCESS") {
									alert(data.message || "URL regenerated successfully!");
									performSearch();
								} else {
									throw new Error(data.message || "Failed to regenerate URL");
								}
							})
							.catch(error => {
								console.error('Regeneration error:', error);
								alert(error.message || 'Failed to regenerate URL');
							})
							.finally(() => {
								closeModal();
							});
					}

					function extendUrlExpiry(urlId) {
						console.log("Extending URL expiry for ID:", urlId);

						if (!urlId || urlId === 'null' || urlId === 'undefined') {
							console.error("Invalid URL ID provided for expiry extension:", urlId);
							alert("Error: Invalid URL ID. Cannot extend expiry.");
							return;
						}

						currentUrlId = urlId;
						document.getElementById('extendExpiryModal').style.display = 'flex';
					}

					function confirmExtendExpiry() {
						if (!currentUrlId || currentUrlId === 'null' || currentUrlId === 'undefined') {
							console.error("No valid URL ID set for expiry extension:", currentUrlId);
							alert("Error: No valid URL ID. Cannot extend expiry.");
							return;
						}

						const extensionDays = document.getElementById('extensionDays').value;

						console.log("Attempting to extend URL expiry with ID:", currentUrlId, "for", extensionDays, "days");

						fetch('/api/url/extendExpiry', {
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							credentials: 'include',
							body: JSON.stringify({
								urlId: currentUrlId,
								extensionDays: parseInt(extensionDays)
							})
						})
							.then(response => {
								if (!response.ok) {
									return response.json().then(errorData => {
										const errorMessage = errorData.message || 'Expiry extension failed';
										throw new Error(errorMessage);
									});
								}
								return response.json();
							})
							.then(data => {
								console.log("Extend expiry response:", data);
								if (data.status === "SUCCESS") {
									alert(data.message || "URL expiry extended successfully!");
									performSearch();
								} else {
									throw new Error(data.message || "Failed to extend URL expiry");
								}
							})
							.catch(error => {
								console.error('Extend expiry error:', error);
								alert(error.message || 'Failed to extend URL expiry');
							})
							.finally(() => {
								closeModal();
							});
					}

					// Search and filter functions
					function resetSearch() {
						document.getElementById('searchForm').reset();
						currentPage = 1;
						performSearch();
					}

					function performSearch() {
						console.log("Search started");
						showLoadingIndicator();

						const searchParams = {
							searchTerm: document.getElementById('searchTerm').value.trim() || undefined,
							status: document.getElementById('status').value || undefined,
							fromDate: document.querySelector('input[name="fromDate"]').value || undefined,
							toDate: document.querySelector('input[name="toDate"]').value || undefined,
							minClicks: document.querySelector('input[name="minClicks"]').value ?
								parseInt(document.querySelector('input[name="minClicks"]').value) : undefined,
							maxClicks: document.querySelector('input[name="maxClicks"]').value ?
								parseInt(document.querySelector('input[name="maxClicks"]').value) : undefined,
							sortField: document.getElementById('sortField').value || undefined,
							sortOrder: document.getElementById('sortOrder').value || undefined,
							page: currentPage,
							size: urlsPerPage
						};

						// Clean undefined/null values
						Object.keys(searchParams).forEach(key => {
							if (searchParams[key] === undefined || searchParams[key] === null) {
								delete searchParams[key];
							}
						});

						console.log("Final search params:", searchParams);

						const searchUrl = `/api/url/search`;

						fetch(searchUrl, {
							method: 'POST',
							headers: {
								'Content-Type': 'application/json',
								'Accept': 'application/json'
							},
							credentials: 'include',
							body: JSON.stringify(searchParams)
						})
							.then(async response => {
								const data = await response.json();
								if (!response.ok) {
									throw new Error(data.message || `HTTP error! status: ${response.status}`);
								}
								return data;
							})
							.then(response => {
								console.log("API Response:", JSON.stringify(response, null, 2));
								if (response.status === "SUCCESS") {
									updateSearchResults(response.data);
								} else {
									throw new Error(response.message || "Search failed");
								}
							})
							.catch(error => {
								console.error('Search error:', error);
								showErrorMessage(error.message || 'Failed to perform search. Please try again.');
							})
							.finally(() => {
								hideLoadingIndicator();
							});
					}

					function updateSearchResults(data) {
						console.log("Updating UI with data:", data);

						const resultsCount = document.querySelector('.results-count');
						const urlCardsContainer = document.querySelector('.url-cards');
						const paginationContainer = document.querySelector('.pagination');

						// Check if required elements exist
						if (!urlCardsContainer) {
							console.error('URL cards container not found');
							return;
						}

						if (data.content && data.content.length > 0) {
							if (resultsCount) {
								resultsCount.textContent = 'Showing ' + data.numberOfElements + ' of ' + data.totalElements + ' URLs';
							}
							urlCardsContainer.innerHTML = data.content.map(url => createUrlCard(url)).join('');

							// Update pagination only if container exists
							if (paginationContainer) {
								if (data.totalPages > 1) {
									updatePagination(data);
									paginationContainer.style.display = 'flex';
								} else {
									paginationContainer.style.display = 'none';
								}
							}
						} else {
							if (resultsCount) {
								resultsCount.textContent = 'No URLs found';
							}
							urlCardsContainer.innerHTML = '<div class="no-urls-message">' +
								'<i class="fas fa-search"></i>' +
								'<div>No URLs found matching your search criteria.</div>' +
								'</div>';

							if (paginationContainer) {
								paginationContainer.style.display = 'none';
							}
						}
					}

					function createUrlCard(url) {
						// Ensure URL data exists and has valid ID
						if (!url || !url.id) {
							console.error("Invalid URL object or missing ID:", url);
							return '';
						}

						const statusClass = url.status ? 'status-' + url.status.toLowerCase() : '';
						const statusText = url.status || 'UNKNOWN';
						const statusIcon = getStatusIcon(url.status);
						const shortCode = url.shortCode || '';
						const originalUrl = url.originalUrl || '#';
						const displayUrl = url.originalUrl ? (url.originalUrl.length > 80 ?
							url.originalUrl.substring(0, 80) + '...' : url.originalUrl) : 'N/A';
						const createdAt = url.createdAt || 'N/A';
						const clickCount = url.clickCount || 0;
						const urlId = url.id;  // We already validated this exists

						console.log("Creating card for URL ID:", urlId); // Debug log

						// Build expires section conditionally
						let expiresSection = '';
						if (url.expiresAt) {
							expiresSection = '<div class="stat-item">' +
								'<span class="stat-label">Expires</span>' +
								'<span class="stat-value">' +
								'<i class="fas fa-clock"></i>' +
								url.expiresAt +
								'</span>' +
								'</div>';
						}

						return '<div class="url-card">' +
							'<div class="url-card-header">' +
							'<div class="url-info">' +
							'<h3>' +
							'<a href="/url/' + shortCode + '" target="_blank" class="short-url">' +
							'shortlink.com/' + shortCode +
							'</a>' +
							'</h3>' +
							'<a href="' + originalUrl + '" target="_blank" class="original-url">' +
							'<i class="fas fa-external-link-alt"></i>' +
							displayUrl +
							'</a>' +
							'<span class="url-status ' + statusClass + '">' +
							'<i class="fas ' + statusIcon + '"></i> ' + statusText +
							'</span>' +
							'<div class="url-stats">' +
							'<div class="stat-item">' +
							'<span class="stat-label">Created</span>' +
							'<span class="stat-value">' +
							'<i class="fas fa-calendar"></i>' +
							createdAt +
							'</span>' +
							'</div>' +
							expiresSection +
							'<div class="stat-item">' +
							'<span class="stat-label">Clicks</span>' +
							'<span class="stat-value">' +
							'<i class="fas fa-mouse-pointer"></i>' +
							clickCount +
							'</span>' +
							'</div>' +
							'</div>' +
							'</div>' +
							'<div class="url-actions">' +
							'<button class="action-btn extend-btn" title="Extend URL Expiry" onclick="extendUrlExpiry(\'' + urlId + '\')">' +
							'<i class="fas fa-calendar-plus"></i>' +
							'</button>' +
							'<button class="action-btn analytics-btn" title="View Analytics" onclick="window.open(\'/url-analytics/' + urlId + '\', \'_blank\')"> ' +
							'<i class="fas fa-chart-bar"></i>' +
							'</button>' +
							'<button class="action-btn regenerate-btn" title="Regenerate URL" onclick="regenerateUrl(\'' + urlId + '\')">' +
							'<i class="fas fa-sync-alt"></i>' +
							'</button>' +
							'<button class="action-btn delete-btn" title="Delete URL" onclick="confirmDelete(\'' + urlId + '\')">' +
							'<i class="fas fa-trash"></i>' +
							'</button>' +
							'</div>' +
							'</div>' +
							'</div>';
					}

					function getStatusIcon(status) {
						switch (status) {
							case 'ACTIVE': return 'fa-check-circle';
							case 'EXPIRED': return 'fa-times-circle';
							default: return 'fa-pause-circle';
						}
					}

					function updatePagination(data) {
						const paginationContainer = document.querySelector('.pagination');
						if (!paginationContainer) {
							console.warn('Pagination container not found');
							return;
						}

						if (data.totalPages > 1) {
							let paginationHtml = '';

							// Previous button - show if not on first page
							if (data.number > 0) {
								paginationHtml += '<button class="page-btn" onclick="goToPage(' + (data.number - 1) + ')">' +
									'<i class="fas fa-chevron-left"></i> Previous' +
									'</button>';
							}

							// Page numbers (convert to 1-based for display)
							const startPage = Math.max(0, data.number - 2);
							const endPage = Math.min(data.totalPages - 1, data.number + 2);

							for (let i = startPage; i <= endPage; i++) {
								const isActive = data.number === i ? 'active' : '';
								paginationHtml += '<button class="page-btn ' + isActive + '" onclick="goToPage(' + i + ')">' +
									(i + 1) + // Display as 1-based
									'</button>';
							}

							// Next button - show if not on last page
							if (data.number < data.totalPages - 1) {
								paginationHtml += '<button class="page-btn" onclick="goToPage(' + (data.number + 1) + ')">' +
									'Next <i class="fas fa-chevron-right"></i>' +
									'</button>';
							}

							paginationContainer.innerHTML = paginationHtml;
							paginationContainer.style.display = 'flex';
						} else {
							paginationContainer.style.display = 'none';
						}
					}

					function goToPage(page) {
						currentPage = page;
						// Update URL without reloading
						const url = new URL(window.location);
						url.searchParams.set('page', page + 1); // Store as 1-based in URL
						window.history.pushState({}, '', url);
						performSearch();
					}

					function showLoadingIndicator() {
						const urlCardsContainer = document.querySelector('.url-cards');
						if (urlCardsContainer) {
							urlCardsContainer.innerHTML = '<div class="loading-indicator" style="text-align: center; padding: 2rem;">' +
								'<i class="fas fa-spinner fa-spin" style="font-size: 2rem; color: var(--primary-color);"></i>' +
								'<div style="margin-top: 1rem;">Loading...</div>' +
								'</div>';
						}
					}

					function hideLoadingIndicator() {
						// Loading indicator will be replaced by search results
					}

					function showErrorMessage(message) {
						const urlCardsContainer = document.querySelector('.url-cards');
						if (urlCardsContainer) {
							urlCardsContainer.innerHTML = '<div class="error-message" style="text-align: center; padding: 2rem; color: var(--danger-color);">' +
								'<i class="fas fa-exclamation-triangle" style="font-size: 2rem; margin-bottom: 1rem;"></i>' +
								'<div>' + message + '</div>' +
								'</div>';
						}
					}

					// Close modals when clicking outside
					window.onclick = function (event) {
						if (event.target.className === 'modal') {
							closeModal();
						}
					};

					// Initialize the page
					// Initialize the page
					document.addEventListener('DOMContentLoaded', function () {
						// Initialize current values from URL parameters or defaults
						const urlParams = new URLSearchParams(window.location.search);
						currentPage = urlParams.has('page') ? parseInt(urlParams.get('page')) - 1 : 0; // Convert to zero-based

						// Set default page size
						urlsPerPage = 10;

						// Prevent default form submission and use AJAX instead
						document.getElementById('searchForm').addEventListener('submit', function (e) {
							e.preventDefault();
							currentPage = 0; // Reset to first page (zero-based) on new search
							performSearch();
						});

						// Auto-submit form when sort options change
						document.getElementById('sortField').addEventListener('change', function () {
							currentPage = 0; // Reset to first page (zero-based)
							performSearch();
						});

						document.getElementById('sortOrder').addEventListener('change', function () {
							currentPage = 0; // Reset to first page (zero-based)
							performSearch();
						});

						// Initial search to load data
						performSearch();
					});
				</script>
			</body>

			</html>