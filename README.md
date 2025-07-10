# 📝 Splitmate Project – Recent Changes
✨ Summary of Updates
🔒 Backend (Spring Boot)
Implemented JWT token enhancements

Added custom claims (e.g., tokenId, role) during token generation.

Modified generateToken method in JWT utility to embed tokenId dynamically for main and guest users.

Updated token validation logic to extract and validate tokenId securely.

Improved Security Configuration

Updated SecurityFilterChain to handle authenticated and public endpoints clearly.

Added AuthenticationEntryPoint for consistent 401 Unauthorized responses.

Enhanced exception handling to send appropriate errors to the frontend (e.g. Access Denied with proper HTTP status).

CustomUserDetails

Created CustomUserDetails class to store additional user information (e.g., tokenId).

Integrated it into loadUserByUsername logic with role-based differentiation between main and guest users.



🔧 Technical Enhancements
Introduced Builder Pattern for CustomUserDetails instantiation.

Added CORS and CSRF configurations in Spring Security for safe frontend-backend integration.

Improved code readability with meaningful method separations and inline documentation.

💻 Git & Workflow Practices
Practiced feature-based commits with meaningful messages.

Ensured code is pushed to main branch with proper staging (git add .) and verification (git status, git diff) before commits.

✅ Key Learnings
Advanced JWT customization and validation.

Spring Security integration with user roles and token-based authentication.


Effective Git version control workflows.

🔮 Next Steps
Write unit and integration tests for token validation flows.

Enhance security for refresh tokens and session expiry.

Improve UI for error messages and login flows.
