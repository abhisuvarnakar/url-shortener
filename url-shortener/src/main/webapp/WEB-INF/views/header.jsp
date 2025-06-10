<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
		<style>
			.navbar {
				background-color: white;
				box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
				padding: 1rem 0;
				position: sticky;
				top: 0;
				z-index: 1000;
				transition: all 0.3s ease;
			}

			.nav-container {
				max-width: 1400px;
				margin: 0 auto;
				padding: 0 2rem;
				display: flex;
				justify-content: space-between;
				align-items: center;
			}

			.logo {
				font-size: 1.8rem;
				font-weight: 700;
				color: #6c63ff;
				text-decoration: none;
				display: flex;
				align-items: center;
				gap: 0.5rem;
			}

			.logo-icon {
				font-size: 1.5rem;
			}

			.nav-links {
				display: flex;
				gap: 2rem;
				align-items: center;
			}

			.nav-links a {
				color: #333;
				text-decoration: none;
				font-weight: 500;
				padding: 0.5rem 0;
				position: relative;
				transition: color 0.3s ease;
			}

			.nav-links a::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				width: 0;
				height: 2px;
				background-color: #6c63ff;
				transition: width 0.3s ease;
			}

			.nav-links a:hover {
				color: #6c63ff;
			}

			.nav-links a:hover::after {
				width: 100%;
			}

			.auth-buttons {
				display: flex;
				gap: 1rem;
			}

			/* Responsive styles */
			@media (max-width: 768px) {
				.nav-container {
					padding: 0 1rem;
					flex-direction: column;
					gap: 1rem;
				}

				.nav-links {
					width: 100%;
					justify-content: space-around;
					gap: 0.5rem;
					flex-wrap: wrap;
				}
			}
		</style>

		<div class="navbar">
			<div class="nav-container">
				<a href="/" class="logo">
					<i class="fas fa-link logo-icon"></i>
					<span>ShortLink</span>
				</a>

				<div class="nav-links" id="nav-links">
					<a href="/">Home</a>

					<sec:authorize access="isAuthenticated()">
						<!-- Links for authenticated users -->
						<a href="/dashboard">Dashboard</a>
						<a href="/profile">Profile</a>
						<a href="/logout">Logout</a>
					</sec:authorize>

					<sec:authorize access="!isAuthenticated()">
						<!-- Links for anonymous users -->
						<a href="/signin">Sign In</a>
						<a href="/signup">Sign Up</a>
					</sec:authorize>
				</div>
			</div>
		</div>