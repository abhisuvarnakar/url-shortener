<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Sign In - Shortly</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f7fa;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        main {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 2rem;
        }

        .signin-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            padding: 2.5rem;
            width: 100%;
            max-width: 450px;
        }

        .form-title {
            color: #6c63ff;
            text-align: center;
            margin-bottom: 2rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
        }

        .form-group input:focus {
            outline: none;
            border-color: #6c63ff;
            box-shadow: 0 0 0 2px rgba(108, 99, 255, 0.2);
        }

        .error-message {
            color: #e74c3c;
            font-size: 0.875rem;
            margin-top: 0.25rem;
            display: none;
        }

        .submit-btn {
            background-color: #6c63ff;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 0.75rem;
            width: 100%;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 1rem;
        }

        .submit-btn:hover {
            background-color: #5a52d6;
        }

        .signup-link {
            text-align: center;
            margin-top: 1.5rem;
            color: #666;
        }

        .signup-link a {
            color: #6c63ff;
            text-decoration: none;
            font-weight: 500;
        }

        .server-error {
            color: #e74c3c;
            text-align: center;
            margin-bottom: 1.5rem;
            padding: 0.75rem;
            background-color: rgba(231, 76, 60, 0.1);
            border-radius: 4px;
            display: none;
        }

        .success-message {
            color: #155724;
            text-align: center;
            margin-bottom: 1.5rem;
            padding: 0.75rem;
            background-color: #d4edda;
            border-radius: 4px;
            display: none;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <main>
        <div class="signin-container">
            <h1 class="form-title">Sign In to Your Account</h1>

            <div id="successMessage" class="success-message"></div>
            <div id="serverError" class="server-error"></div>

            <form id="signinForm">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                    <div id="emailError" class="error-message">Valid email is required</div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                    <div id="passwordError" class="error-message">Password is required</div>
                </div>

                <button type="submit" class="submit-btn">Sign In</button>
            </form>

            <div class="signup-link">
                Don't have an account? <a href="/signup">Sign Up</a>
            </div>
        </div>
    </main>

    <script>

        window.onload = function() {
            if (document.cookie.includes("token")) {
                        fetch('/logout', {
                            method: 'POST',
                            credentials: 'include'
                        })
                        .then(() => {
                            console.log("Auto-logged out");
                        })
                        .catch(err => console.error("Logout failed", err));
                    }
            document.getElementById('signinForm').reset();
            document.getElementById('successMessage').style.display = 'none';
            document.getElementById('serverError').style.display = 'none';
            document.querySelectorAll('.error-message').forEach(el => {
                el.style.display = 'none';
                el.textContent = '';
            });
        };

        document.getElementById('signinForm').addEventListener('submit', function(e) {
            e.preventDefault();

            document.querySelectorAll('.error-message').forEach(el => el.style.display = 'none');
            document.getElementById('serverError').style.display = 'none';
            document.getElementById('successMessage').style.display = 'none';

            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();

            let isValid = true;
            if (!email) {
                document.getElementById('emailError').style.display = 'block';
                isValid = false;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                document.getElementById('emailError').textContent = 'Please enter a valid email';
                document.getElementById('emailError').style.display = 'block';
                isValid = false;
            }
            if (!password) {
                document.getElementById('passwordError').style.display = 'block';
                isValid = false;
            }
            if (!isValid) return;

            const userData = { email, password };

            const submitBtn = document.querySelector('.submit-btn');
            const originalBtnText = submitBtn.textContent;
            submitBtn.disabled = true;
            submitBtn.textContent = 'Signing in...';

            fetch('/api/auth/signin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            })
            .then(response => response.json().then(data => {
                if (!response.ok) throw data;
                return data;
            }))
            .then(data => {
                if (data.status === 'SUCCESS') {
                    const successElement = document.getElementById('successMessage');
                    successElement.textContent = data.message;
                    successElement.style.display = 'block';

                    setTimeout(() => {
                        window.location.href = '/dashboard';
                    }, 2000);
                } else {
                    const errorElement = document.getElementById('serverError');
                    errorElement.textContent = data.message || 'Login failed. Please try again.';
                    errorElement.style.display = 'block';
                }
            })
            .catch(error => {
                const errorElement = document.getElementById('serverError');
                errorElement.textContent = error.message || 'An unexpected error occurred.';
                errorElement.style.display = 'block';
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.textContent = originalBtnText;
            });
        });
    </script>
</body>
</html>