# Week 15 Demonstration Script & Viva Voce Guide
**Skill Exchange Platform | B.Tech 3rd Year CSE Capstone**

---

## Part 1: Step-by-Step Demonstration Script (17 Steps)

Follow this exact walkthrough during your project demonstration to show every module working end-to-end:

### Step 1: Landing Page
- Open `http://localhost:5173`.
- Show the clean hero header, explain the dual-persona barter concept, the 4-step workflow, and popular skill categories.
- Highlight the quick demo accounts box designed for evaluation.

### Step 2: User Registration & Login
- Click **Get Started Free** / **Register**.
- Fill in: Full Name (e.g. `Kiran Kumar`), unique email (`kiran@example.com`), password (`password123`), mode (`ONLINE`), and bio.
- Click **Register & Create Profile**.
- Observe the immediate JWT token generation, persistent session creation, and redirect to the **Dashboard**.
- Demonstrate logout, and then use the **Quick Demo Button** on `/login` to sign in as **Rahul Sharma** (`rahul@example.com`).

### Step 3 & 4: Adding Skills (Offered & Wanted)
- Navigate to **My Skills** (`/my-skills`).
- Show the two distinct sections: **Skills I Teach (Offered)** and **Skills I Want (Wanted)**.
- Click **+ Add New Skill**.
- Add a new skill: Select `Python`, choose role `Skill I can TEACH (Offered)`, proficiency `ADVANCED`, and click **Add to My Profile**.
- Show that it immediately appears in your listed skills and updates the database.

### Step 5: Skill Discovery & Peer Search
- Click **Discovery** in the navbar (`/discovery`).
- Search for `"React"` or `"Design"`, or filter by category **Web Development** or proficiency **Advanced**.
- Point out that **Priya Patel** appears with her offered skills and wanted skills clearly marked.

### Step 6: Algorithmic Matching Recommendations
- Click **Matches** in the navbar (`/matches`).
- Show that the Java matching algorithm calculated a **100% Compatibility Match** with **Priya Patel**!
- Point out the algorithmic rationale banner:
  > *"Perfect Match: Priya Patel teaches React.js (which you want to learn), and wants to learn Java (which you can teach)."*

### Step 7: Sending a Skill Exchange Request
- On Priya's match card, click **Propose Skill Exchange**.
- Select the skill you want to learn (`React.js`) and the skill you offer to teach (`Java`).
- Enter an introductory note: *"Hi Priya! Let's swap Java and React sessions."*
- Click **Send Skill Exchange Proposal**.
- Show that the request status is `PENDING` under **Sent Requests** (`/requests/outgoing`).

### Step 8 & 9: Logging in as Recipient & Accepting Request
- Log out, and click **Login as Priya** (`priya@example.com`).
- Notice the **Alerts badge** in the navbar has an unread notification: *"New Skill Exchange Request from Rahul Sharma"*.
- Navigate to **Requests** (`/requests/incoming`).
- Click **Accept Exchange**.
- The status immediately updates to `ACCEPTED`, and a direct chat room is automatically created.

### Step 10: In-App Chat Communication
- Click **Open Chat** or navigate to **Messages** (`/conversations`).
- Notice the active thread with **Rahul Sharma**.
- Send a message: *"Hi Rahul! I've accepted your exchange request. When are you free to start?"*
- Observe the instant message bubble update and timestamp persistence.

### Step 11: Marking Exchange Completed & Viewing History
- Open **Exchange Details** (`/exchanges/{id}`).
- Click **Mark Exchange as Completed &#10003;**.
- Show that the status transitions to `COMPLETED` and is permanently recorded in your history.

### Step 12: Administrator Dashboard & Moderation
- Log out, and click **Login as Administrator** (`admin@skillexchange.com`).
- Observe the system-wide metrics: Total Users, Active Users, Catalog Skills, Completed Barters, and Total Messages.
- Click **Manage Users** (`/admin/users`) &rarr; toggle an account's status between **Active** and **Disabled**.
- Click **Moderate Skills** (`/admin/skills`) &rarr; show disabling a skill or adding a new master skill to the catalog.

### Step 13: Grounded AI Assistant (RAG Engine)
- Click **AI Guide** in the navbar (`/ai-assistant`).
- Click one of the quick chips: *"How does the partner matching algorithm work?"*.
- Point out:
  1. The grounded answer synthesized directly from `knowledge_docs`.
  2. The **Grounding Sources** cited: `How to Find a Learning Partner & Matching Algorithm (DISCOVERY)`.
  3. The confidence score metric (e.g. `100%`).
- Test an out-of-scope question (e.g., *"What is the stock price of Apple?"*) to show that the system gracefully clarifies it only assists with the Skill Exchange Platform.

### Step 14: Database Persistence
- Demonstrate that if the backend or frontend is refreshed, all users, skills, requests, and messages remain persisted in the relational database.

### Step 15: Demonstrating REST APIs & Actuator
- Open a browser tab or Postman:
  - `http://localhost:8080/actuator/health` &rarr; returns `{"status":"UP"}`.
  - `http://localhost:8080/api/skills/categories` &rarr; returns cached JSON categories array.

### Step 16: Automated Test Verification
- In terminal, execute:
  ```bash
  cd backend
  mvn test
  ```
- Show all 6 tests passing with `BUILD SUCCESS`.

### Step 17: Docker Containerization
- Show `docker-compose.yml` and explain how MySQL 8, Spring Boot, and React are containerized for deployment.

---

## Part 2: Likely Viva Voce Questions & Model Answers

### Q1: Why did you choose a dual persona model instead of separate teacher and student accounts?
**Answer**: In real-world peer learning, anyone with knowledge in one domain (e.g., Java) might simultaneously be a beginner in another domain (e.g., UI/UX design or French). Creating separate accounts would force users to maintain two logins. The dual-persona model allows every user to maintain both an `OFFERED` skills list and a `WANTED` skills list within the same normalized user profile.

### Q2: How does your matching algorithm calculate recommendations?
**Answer**: The matching engine in `MatchingServiceImpl.java` operates in Java:
1. It retrieves User A's wanted skills ($W_A$) and offered skills ($O_A$).
2. For each active peer User B, it retrieves their offered skills ($O_B$) and wanted skills ($W_B$).
3. It checks for intersections:
   - If $(W_A \cap O_B) \neq \emptyset$ AND $(O_A \cap W_B) \neq \emptyset$, it assigns a **100% Perfect Match** (reciprocal barter).
   - If only $(W_A \cap O_B) \neq \emptyset$, it assigns a **75% Match** (direct learning opportunity).
   - If they share skill categories, it assigns a **50% Category Match**.
4. It attaches an explainable rationale string to each recommendation.

### Q3: How is JWT authentication implemented and verified in Spring Security?
**Answer**:
1. When a user logs in via `/api/auth/login`, Spring Security's `AuthenticationManager` verifies the BCrypt-hashed password.
2. Upon success, `JwtUtils` signs a token with HMAC-SHA256 containing the user's email, ID, role, and expiration timestamp.
3. On subsequent requests, the React client attaches the token as `Authorization: Bearer <token>`.
4. `AuthTokenFilter` (an extension of `OncePerRequestFilter`) intercepts the request, validates the signature and expiration, and populates `SecurityContextHolder`.

### Q4: How is Retrieval-Augmented Generation (RAG) implemented without third-party frameworks?
**Answer**:
1. **Knowledge Base**: We maintain verified documentation passages in the `knowledge_docs` MySQL table.
2. **Retrieval**: When a query arrives at `/api/ai/chat`, `AiAssistantServiceImpl` tokenizes the query, removes stop words, and searches the knowledge base, calculating term-overlap and title-match relevance scores.
3. **Grounded Generation**: The response is assembled strictly from the highest-ranking knowledge document, citing the exact document title and category as the grounding source. If the query is outside platform documentation, it gracefully informs the user without hallucinating.

### Q5: Where did you implement Caching and Asynchronous Processing?
**Answer**:
- **Caching**: Configured via `@EnableCaching` in Spring Boot. In `SkillServiceImpl.java`, the method `getCategories()` is annotated with `@Cacheable("skillCategories")` because skill categories are read frequently by dropdown menus. When a new skill is created, `@CacheEvict(allEntries = true)` invalidates the cache.
- **Asynchronous Processing**: Configured via `@EnableAsync`. In `NotificationServiceImpl.java`, `sendAsyncNotification()` is marked with `@Async`. When exchange requests or chat messages occur, notification creation runs on a background worker thread (`ThreadPoolTaskExecutor`), preventing blocking on the main HTTP response thread.
