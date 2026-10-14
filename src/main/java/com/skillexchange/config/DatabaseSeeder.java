package com.skillexchange.config;

import com.skillexchange.entity.*;
import com.skillexchange.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial demonstration data on application startup.
 * Provides ready-to-test accounts, skill catalog, complementary matches,
 * an accepted exchange, conversation messages, and RAG knowledge base.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private KnowledgeDocRepository knowledgeDocRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Database already contains data; skipping initialization.");
            return;
        }

        logger.info("Seeding initial capstone demonstration data...");

        // 1. Seed Skills Catalog
        Skill javaSkill = new Skill("Java", "Programming", "Core Java, OOPs, Collections, Multithreading, and Streams API.");
        Skill springBootSkill = new Skill("Spring Boot", "Programming", "REST APIs, Spring Data JPA, Hibernate, and Spring Security.");
        Skill pythonSkill = new Skill("Python", "Programming", "Python syntax, scripting, automation, and introductory machine learning.");
        Skill dsaSkill = new Skill("Data Structures & Algorithms", "Programming", "Arrays, Trees, Graphs, Dynamic Programming, and problem solving.");

        Skill reactSkill = new Skill("React.js", "Web Development", "Functional components, Hooks, State management, and React Router.");
        Skill htmlCssSkill = new Skill("HTML & CSS", "Web Development", "Semantic HTML5, CSS Flexbox, Grid, and responsive web styling.");
        Skill sqlSkill = new Skill("MySQL & SQL", "Web Development", "Database normalization, relational schemas, indexing, and SQL queries.");

        Skill uiUxSkill = new Skill("UI/UX Design", "Design", "User interface wireframing, component design, Figma prototypes, and user experience design.");
        Skill graphicDesignSkill = new Skill("Graphic Design", "Design", "Visual composition, typography, color palettes, and poster creation.");
        Skill photographySkill = new Skill("Photography", "Arts & Media", "DSLR camera settings, lighting, composition, and photo editing.");

        Skill frenchSkill = new Skill("French Language", "Languages", "French pronunciation, conversational grammar, and everyday vocabulary.");
        Skill spanishSkill = new Skill("Spanish Language", "Languages", "Conversational Spanish, verb conjugations, and everyday dialogues.");

        Skill publicSpeakingSkill = new Skill("Public Speaking", "Communication", "Speech structuring, vocal projection, body language, and stage confidence.");
        Skill interviewPrepSkill = new Skill("Technical Interview Prep", "Communication", "System design basics, behavioral STAR method, and mock interviews.");

        List<Skill> allSkills = List.of(
                javaSkill, springBootSkill, pythonSkill, dsaSkill,
                reactSkill, htmlCssSkill, sqlSkill,
                uiUxSkill, graphicDesignSkill, photographySkill,
                frenchSkill, spanishSkill,
                publicSpeakingSkill, interviewPrepSkill
        );
        skillRepository.saveAll(allSkills);

        // 2. Seed Users
        String encodedUserPassword = passwordEncoder.encode("password123");
        String encodedAdminPassword = passwordEncoder.encode("adminpassword123");

        // Administrator Account
        User admin = new User();
        admin.setEmail("admin@skillexchange.com");
        admin.setPasswordHash(encodedAdminPassword);
        admin.setFullName("Platform Administrator");
        admin.setRole("ROLE_ADMIN");
        admin.setBio("Official administrator overseeing platform safety, skill catalog moderation, and user reports.");
        admin.setPreferredLearningMode("ONLINE");
        admin.setActive(true);
        userRepository.save(admin);

        // Regular User 1: Rahul Sharma (teaches Java & Spring Boot; wants React & UI/UX)
        User rahul = new User();
        rahul.setEmail("rahul@example.com");
        rahul.setPasswordHash(encodedUserPassword);
        rahul.setFullName("Rahul Sharma");
        rahul.setRole("ROLE_USER");
        rahul.setBio("3rd Year Computer Science student passionate about Java enterprise architecture and building robust REST services.");
        rahul.setPreferredLearningMode("ONLINE");
        rahul.setActive(true);
        userRepository.save(rahul);

        // Regular User 2: Priya Patel (teaches React & UI/UX; wants Java & Python)
        User priya = new User();
        priya.setEmail("priya@example.com");
        priya.setPasswordHash(encodedUserPassword);
        priya.setFullName("Priya Patel");
        priya.setRole("ROLE_USER");
        priya.setBio("Frontend developer and UI enthusiast who loves clean responsive layouts and component design.");
        priya.setPreferredLearningMode("ONLINE");
        priya.setActive(true);
        userRepository.save(priya);

        // Regular User 3: Ananya Verma (teaches French & Public Speaking; wants Graphic Design)
        User ananya = new User();
        ananya.setEmail("ananya@example.com");
        ananya.setPasswordHash(encodedUserPassword);
        ananya.setFullName("Ananya Verma");
        ananya.setRole("ROLE_USER");
        ananya.setBio("Linguistics and debate society coordinator excited to learn digital graphics and poster illustration.");
        ananya.setPreferredLearningMode("HYBRID");
        ananya.setActive(true);
        userRepository.save(ananya);

        // Regular User 4: Vikram Singh (teaches Graphic Design & Photography; wants French)
        User vikram = new User();
        vikram.setEmail("vikram@example.com");
        vikram.setPasswordHash(encodedUserPassword);
        vikram.setFullName("Vikram Singh");
        vikram.setRole("ROLE_USER");
        vikram.setBio("Digital visual artist and amateur street photographer looking to pick up French for international travel.");
        vikram.setPreferredLearningMode("HYBRID");
        vikram.setActive(true);
        userRepository.save(vikram);

        // 3. Seed User Skills
        // Rahul
        userSkillRepository.save(new UserSkill(rahul, javaSkill, "OFFERED", "ADVANCED", "Over 2 years of Java development and backend systems."));
        userSkillRepository.save(new UserSkill(rahul, springBootSkill, "OFFERED", "INTERMEDIATE", "Experience with Spring Boot REST microservices."));
        userSkillRepository.save(new UserSkill(rahul, reactSkill, "WANTED", "BEGINNER", "Looking to understand React state and component architecture."));
        userSkillRepository.save(new UserSkill(rahul, uiUxSkill, "WANTED", "BEGINNER", "Want to learn Figma to design clean dashboard interfaces."));

        // Priya (Perfect complementary match with Rahul!)
        userSkillRepository.save(new UserSkill(priya, reactSkill, "OFFERED", "ADVANCED", "Comfortable with modern React hooks, router, and SPA architecture."));
        userSkillRepository.save(new UserSkill(priya, uiUxSkill, "OFFERED", "EXPERT", "Certified in UX design and Figma component libraries."));
        userSkillRepository.save(new UserSkill(priya, javaSkill, "WANTED", "INTERMEDIATE", "Need guidance with Java backend frameworks and concurrency."));
        userSkillRepository.save(new UserSkill(priya, pythonSkill, "WANTED", "BEGINNER", "Interested in Python scripting."));

        // Ananya (Complementary match with Vikram)
        userSkillRepository.save(new UserSkill(ananya, frenchSkill, "OFFERED", "EXPERT", "DELF B2 certified French speaker with 4 years practice."));
        userSkillRepository.save(new UserSkill(ananya, publicSpeakingSkill, "OFFERED", "ADVANCED", "President of college debating society."));
        userSkillRepository.save(new UserSkill(ananya, graphicDesignSkill, "WANTED", "BEGINNER", "Eager to learn Adobe Photoshop/Illustrator design techniques."));

        // Vikram (Complementary match with Ananya)
        userSkillRepository.save(new UserSkill(vikram, graphicDesignSkill, "OFFERED", "ADVANCED", "Experienced with visual branding, logo creation, and social graphics."));
        userSkillRepository.save(new UserSkill(vikram, photographySkill, "OFFERED", "INTERMEDIATE", "Street photography and Lightroom color grading."));
        userSkillRepository.save(new UserSkill(vikram, frenchSkill, "WANTED", "BEGINNER", "Looking to learn basic conversational French phrases."));

        // 4. Seed an Active Exchange & Conversation between Rahul and Priya
        ExchangeRequest sampleExchange = new ExchangeRequest(
                rahul,
                priya,
                javaSkill, // Rahul teaches Java
                reactSkill, // Priya teaches React
                "Hi Priya! I saw you are looking to learn Java. I have strong experience in Core Java and Spring Boot, and I'd love to learn React from you in exchange!"
        );
        sampleExchange.setStatus("ACCEPTED");
        ExchangeRequest savedExchange = exchangeRequestRepository.save(sampleExchange);

        Conversation conv = new Conversation(savedExchange);
        Conversation savedConv = conversationRepository.save(conv);

        // Seed messages in conversation
        messageRepository.save(new Message(savedConv, rahul, "Hi Priya! Thanks for accepting the exchange request. Excited to learn React!"));
        messageRepository.save(new Message(savedConv, priya, "Hi Rahul! Great to connect. I've prepared a roadmap for React components and Hooks. When are you free for our first session?"));
        messageRepository.save(new Message(savedConv, rahul, "How about tomorrow at 5 PM on Google Meet? I can walk you through Java Collections and OOP concepts first."));
        messageRepository.save(new Message(savedConv, priya, "Sounds perfect! See you tomorrow at 5 PM."));

        // Seed sample notifications
        notificationRepository.save(new Notification(
                rahul,
                "Exchange Request Accepted",
                "Priya Patel accepted your exchange request! You can now chat and coordinate learning sessions.",
                "REQUEST_ACCEPTED",
                savedExchange.getId()
        ));

        // 5. Seed RAG Knowledge Documents
        knowledgeDocRepository.save(new KnowledgeDoc(
                "How to Add Skills to Your Profile",
                "Skills Management",
                "SKILLS",
                "To add skills to your profile: 1. Log into your account and navigate to 'My Skills' from the navigation bar. 2. Click on 'Add Skill' button. 3. Select whether the skill is 'Offered' (a skill you can teach to others) or 'Wanted' (a skill you want to learn). 4. Choose the skill from the platform catalog, specify your proficiency level (Beginner, Intermediate, Advanced, Expert), and add any relevant notes. 5. Click 'Save Skill'. Your profile will immediately reflect the new skill, and the matching engine will update your recommendations.",
                "add skill, list skill, offered, wanted, proficiency, profile"
        ));

        knowledgeDocRepository.save(new KnowledgeDoc(
                "How to Find a Learning Partner & Matching Algorithm",
                "Discovery & Matching",
                "DISCOVERY",
                "To discover learning partners: 1. Visit the 'Discovery' page to browse and filter active peers by skill name, category, or proficiency. 2. Alternatively, visit the 'Smart Matches' page. The platform's matching algorithm compares your 'Wanted Skills' with other users' 'Offered Skills', and checks if you can teach what they want to learn. A 'Perfect Match' (100% score) occurs when two users have reciprocal complementary skills. Recommendations include a clear explanation of why you were paired.",
                "find partner, search, discovery, matching algorithm, recommendations, complement"
        ));

        knowledgeDocRepository.save(new KnowledgeDoc(
                "How to Send a Skill Exchange Request",
                "Exchange Workflow",
                "EXCHANGE_WORKFLOW",
                "To initiate a skill barter proposal: 1. Navigate to a user's profile or choose them from the Matches page. 2. Click 'Request Exchange'. 3. Select the skill you want to learn from the recipient, and choose the skill you are offering to teach them in return. 4. Write an introductory message explaining your learning goals. 5. Submit the request. The status will start as 'PENDING'. The recipient will receive an in-app notification to review and accept or reject your proposal.",
                "send request, initiate exchange, propose, barter, pending"
        ));

        knowledgeDocRepository.save(new KnowledgeDoc(
                "What Happens After a Request is Accepted",
                "Exchange Workflow",
                "EXCHANGE_WORKFLOW",
                "Once the recipient accepts your exchange request: 1. The exchange status updates to 'ACCEPTED'. 2. A direct chat conversation is automatically created. 3. Both users can access the 'Messages' page to communicate, discuss schedules, share resources, and set up learning sessions. 4. The exchange appears in your active exchanges on your dashboard.",
                "accepted request, chat, messaging, coordination, communication"
        ));

        knowledgeDocRepository.save(new KnowledgeDoc(
                "How to Mark an Exchange as Completed",
                "Exchange Workflow",
                "EXCHANGE_WORKFLOW",
                "When you and your partner have concluded your mutual learning sessions: 1. Open the 'Exchange Details' page from your dashboard or requests list. 2. Click the 'Mark as Completed' button. 3. The status will transition to 'COMPLETED'. 4. The exchange will move to your permanent exchange history. Both participants can refer back to completed exchanges on their profile and dashboard.",
                "complete exchange, finish, mark complete, history"
        ));

        knowledgeDocRepository.save(new KnowledgeDoc(
                "Platform Safety and Non-Monetary Rules",
                "Community Guidelines",
                "RULES",
                "The Skill Exchange Platform operates strictly on a non-monetary, peer-to-peer knowledge sharing model. Users are prohibited from requesting or charging money for lessons, selling external products, or engaging in commercial tutoring. All interactions should remain respectful, professional, and dedicated to mutual education. Inappropriate listings can be removed by the platform administrator.",
                "rules, guidelines, money, payment, safety, policy, free"
        ));

        logger.info("Successfully seeded all demonstration data!");
    }
}
