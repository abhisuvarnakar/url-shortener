<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<!DOCTYPE html>
		<html>

		<head>
			<title>Profile - Shortly</title>
			<style>
				:root {
					--primary-color: #6c63ff;
					--secondary-color: #5a52d6;
					--light-color: #f8f9fa;
					--dark-color: #343a40;
					--success-color: #28a745;
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

				.profile-card {
					background-color: white;
					padding: 2rem;
					border-radius: 8px;
					box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
					max-width: 800px;
					margin: 0 auto;
				}

				.profile-card h2 {
					margin-top: 0;
					color: var(--dark-color);
					border-bottom: 1px solid #eee;
					padding-bottom: 1rem;
				}

				/* Profile Display Section (now at top) */
				.profile-display {
					margin-bottom: 3rem;
					padding: 1.5rem;
					background-color: #f9f9f9;
					border-radius: 8px;
					border: 1px solid #eee;
				}

				.profile-display h3 {
					margin-top: 0;
					color: var(--dark-color);
					border-bottom: 1px solid #eee;
					padding-bottom: 0.75rem;
				}

				.profile-field {
					margin-bottom: 1rem;
					display: flex;
				}

				.profile-label {
					font-weight: 500;
					min-width: 120px;
					color: var(--dark-color);
				}

				.profile-value {
					flex: 1;
				}

				/* Form Section (now below display) */
				.form-section {
					margin-bottom: 2.5rem;
				}

				.form-section h3 {
					color: var(--dark-color);
					margin-bottom: 1.5rem;
				}

				.form-grid {
					display: grid;
					grid-template-columns: 1fr 1fr;
					gap: 2rem;
				}

				.form-group {
					margin-bottom: 1.5rem;
				}

				.form-group label {
					display: block;
					margin-bottom: 0.75rem;
					font-weight: 500;
					color: var(--dark-color);
				}

				.form-control {
					width: 100%;
					padding: 0.75rem;
					border: 1px solid #ddd;
					border-radius: 4px;
					font-size: 1rem;
					transition: border-color 0.3s;
				}

				.form-control:focus {
					outline: none;
					border-color: var(--primary-color);
				}

				.form-fullwidth {
					grid-column: span 2;
				}

				textarea.form-control {
					min-height: 100px;
					resize: vertical;
				}

				.update-btn {
					background-color: var(--primary-color);
					color: white;
					border: none;
					border-radius: 4px;
					padding: 0.75rem 1.5rem;
					font-size: 1rem;
					font-weight: 500;
					cursor: pointer;
					transition: background-color 0.3s;
					display: block;
					width: 200px;
					margin: 2rem auto 0;
				}

				.update-btn:hover {
					background-color: var(--secondary-color);
				}

				/* Responsive adjustments */
				@media (max-width: 768px) {
					.form-grid {
						grid-template-columns: 1fr;
						gap: 1.5rem;
					}

					.form-fullwidth {
						grid-column: span 1;
					}

					.profile-field {
						flex-direction: column;
					}

					.profile-label {
						margin-bottom: 0.25rem;
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

					<div class="profile-card">
						<h2>Profile Details</h2>

						<!-- Profile Display Section (now at top) -->
						<div class="profile-display" id="profileDisplay">
							<div class="form-grid">
								<div class="profile-field">
									<span class="profile-label">First Name:</span>
									<span class="profile-value" id="displayFirstName"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Last Name:</span>
									<span class="profile-value" id="displayLastName"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Email:</span>
									<span class="profile-value" id="displayEmail"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Date of Birth:</span>
									<span class="profile-value" id="displayBirthDate"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Gender:</span>
									<span class="profile-value" id="displayGender"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Phone:</span>
									<span class="profile-value" id="displayPhone"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">City:</span>
									<span class="profile-value" id="displayCity"></span>
								</div>
								<div class="profile-field">
									<span class="profile-label">Country:</span>
									<span class="profile-value" id="displayCountry"></span>
								</div>
								<div class="profile-field form-fullwidth">
									<span class="profile-label">Bio:</span>
									<span class="profile-value" id="displayBio"></span>
								</div>
							</div>
						</div>

						<h2>Update Profile</h2>

						<!-- Update Form Section (now below display) -->
						<form id="profileForm" action="/api/user/updateUser" method="PUT">
							<!-- Personal Info Section -->
							<div class="form-section">
								<h3>Personal Information</h3>
								<div class="form-grid">
									<div class="form-group">
										<label for="firstName">First Name</label>
										<input type="text" id="firstName" name="firstName" class="form-control"
											value="${user.firstName}" required>
									</div>
									<div class="form-group">
										<label for="lastName">Last Name</label>
										<input type="text" id="lastName" name="lastName" class="form-control"
											value="${user.lastName}" required>
									</div>
								</div>
							</div>

							<!-- Demographics Section -->
							<div class="form-section">
								<h3>Demographics</h3>
								<div class="form-grid">
									<div class="form-group">
										<label for="birthDate">Date of Birth</label>
										<input type="date" id="birthDate" name="birthDate" class="form-control"
											value="${user.birthDate}">
									</div>
									<div class="form-group">
										<label for="gender">Gender</label>
										<select id="gender" name="gender" class="form-control">
											<option value="">Select Gender</option>
											<option value="MALE" ${user.gender=='MALE' ? 'selected' : '' }>Male</option>
											<option value="FEMALE" ${user.gender=='FEMALE' ? 'selected' : '' }>Female
											</option>
											<option value="OTHER" ${user.gender=='OTHER' ? 'selected' : '' }>Other
											</option>
										</select>
									</div>
								</div>
							</div>

							<!-- Contact Section -->
							<div class="form-section">
								<h3>Contact Information</h3>
								<div class="form-grid">
									<div class="form-group">
										<label for="phone">Phone Number</label>
										<input type="tel" id="phone" name="phone" class="form-control"
											value="${user.phone}">
									</div>
									<div class="form-group">
										<label for="city">City</label>
										<input type="text" id="city" name="city" class="form-control"
											value="${user.city}">
									</div>
								</div>
							</div>

							<!-- Location Section -->
							<div class="form-section">
								<h3>Location</h3>
								<div class="form-group form-fullwidth">
									<label for="country">Country</label>
									<select id="country" name="country" class="form-control">
										<option value="">Select Country</option>
										<!-- Countries will be loaded via our API -->
									</select>
								</div>
							</div>

							<!-- Bio Section -->
							<div class="form-section">
								<h3>About You</h3>
								<div class="form-group form-fullwidth">
									<label for="bio">Bio</label>
									<textarea id="bio" name="bio" class="form-control">${user.bio}</textarea>
								</div>
							</div>

							<button type="submit" class="update-btn">
								<i class="fas fa-save"></i> Update Profile
							</button>
						</form>
					</div>
				</div>
			</div>

			<script>
				document.addEventListener('DOMContentLoaded', async function () {
                    try {
                        const response = await fetch('/api/user/profile-page-data', {
                            headers: {
                                'Authorization': `Bearer ${getAuthToken()}`
                            },
                            credentials: 'include',
                        });

                        if (!response.ok) {
                            throw new Error('Failed to load profile data');
                        }

                        const data = await response.json();
                        const { user, countries } = data;

                        populateDisplaySection(user);
                        populateFormFields(user);
                        populateCountriesDropdown(countries, user.country);

                    } catch (error) {
                        console.error('Initialization error:', error);
                        alert('Failed to load profile data. Please refresh the page.');
                    }

                    document.getElementById('profileForm').addEventListener('submit', handleFormSubmit);
                });

                // Helper functions
                function populateDisplaySection(user) {
                    document.getElementById('displayFirstName').textContent = user.firstName;
                    document.getElementById('displayLastName').textContent = user.lastName;
                    document.getElementById('displayEmail').textContent = user.email;
                    document.getElementById('displayBirthDate').textContent = user.birthDate || '';
                    document.getElementById('displayGender').textContent = user.gender || '';
                    document.getElementById('displayPhone').textContent = user.phone || '';
                    document.getElementById('displayCity').textContent = user.city || '';
                    document.getElementById('displayCountry').textContent = user.countryName || '';
                    document.getElementById('displayBio').textContent = user.bio || '';
                }

                function populateFormFields(user) {
                    document.getElementById('firstName').value = user.firstName || '';
                    document.getElementById('lastName').value = user.lastName || '';
                    document.getElementById('birthDate').value = user.birthDate || '';
                    document.getElementById('gender').value = user.gender || '';
                    document.getElementById('phone').value = user.phone || '';
                    document.getElementById('city').value = user.city || '';
                    document.getElementById('bio').value = user.bio || '';
                }

                function populateCountriesDropdown(countries, userCountryCode) {
                    const countrySelect = document.getElementById('country');

                    // Sort countries by name
                    countries.sort((a, b) => a.name.localeCompare(b.name));

                    // Add country options
                    countries.forEach(country => {
                        const option = document.createElement('option');
                        option.value = country.code;
                        option.textContent = country.name;
                        if (userCountryCode === country.code) {
                            option.selected = true;
                        }
                        countrySelect.appendChild(option);
                    });
                }

                async function handleFormSubmit(e) {
                    e.preventDefault();

                    const form = e.target;
                    const submitBtn = form.querySelector('button[type="submit"]');

                    try {
                        // Show loading state
                        submitBtn.disabled = true;
                        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Updating...';

                        const response = await fetch(form.action, {
                            method: 'PUT',
                            body: JSON.stringify({
                                firstName: form.firstName.value,
                                lastName: form.lastName.value,
                                birthDate: form.birthDate.value,
                                gender: form.gender.value,
                                phone: form.phone.value,
                                city: form.city.value,
                                country: form.country.value,
                                bio: form.bio.value
                            }),
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            credentials: 'include'
                        });

                        if (!response.ok) {
                            const error = await response.json();
                            throw new Error(error.message || 'Update failed');
                        }

                        const updatedUser = await response.json();

                        // Refresh displayed data
                        populateDisplaySection(updatedUser);
                        alert('Profile updated successfully!');

                    } catch (error) {
                        console.error('Update error:', error);
                        alert(error.message || 'Failed to update profile. Please try again.');
                    } finally {
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = '<i class="fas fa-save"></i> Update Profile';
                    }
                }
			</script>
		</body>

		</html>