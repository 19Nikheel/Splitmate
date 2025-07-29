# 📝 Splitmate Project – Recent Changes
📝 Splitmate – Version 5.0 Changelog
🚀 New Features and Enhancements
✅ Security Improvements

Introduced password protection for groups to restrict unauthorized access.

Added join request approval system:

Users now send a request to join a group.

Group admins can accept or reject requests for enhanced security.

✅ One-Time Login Feature

Implemented 1-time login tokens for improved security:

Prevents multiple entries using compromised user IDs and passwords.

✅ Balance Sheet Algorithm

Integrated greedy algorithm for debt settlement:

Calculates minimum number of settlements required.

Efficiently determines who owes whom, optimizing settlements among group members.

✅ Database Enhancements

Redesigned Balance table to store composite keys for user pairs with proper relational mapping.

Introduced DTO structures for simplified data conversions between frontend and backend.

✅ Tax Distribution Feature

Added logic to distribute leftover tax amounts evenly among all consumers when calculating balances.

✅ Version Control and Codebase

Switched to structured branching strategy using Git and GitHub for better version management.

Resolved merge conflicts and ensured consistent database schema updates.

🔧 Technical Stack Updates
Frontend: Continued development using VS Code.

Backend: Enhanced logic and security layers using IntelliJ IDEA.

Version Control: Implemented strict Git workflows for collaborative stability.

📌 Summary
Splitmate v5 delivers significant security upgrades, optimized balance sheet calculations, and database integrity improvements, setting a strong foundation for scalable future features



✨ Summary of Updates 
Splitmate – Version 4.0 Changelog
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
