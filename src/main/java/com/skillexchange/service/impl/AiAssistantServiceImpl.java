package com.skillexchange.service.impl;

import com.skillexchange.dto.AiChatRequest;
import com.skillexchange.dto.AiChatResponse;
import com.skillexchange.entity.KnowledgeDoc;
import com.skillexchange.repository.KnowledgeDocRepository;
import com.skillexchange.service.AiAssistantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of AiAssistantService using Retrieval-Augmented Generation (RAG).
 * 1. Retrieves relevant verified documentation chunks from MySQL knowledge_docs table.
 * 2. Grounds the response strictly within retrieved content to prevent hallucinations.
 * 3. Never bypasses security or application business workflows.
 */
@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private static final Logger logger = LoggerFactory.getLogger(AiAssistantServiceImpl.class);

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "in", "on", "at", "to", "for", "with", "is", "are", "do", "how", "what",
            "where", "can", "i", "my", "me", "you", "your", "please", "help", "about", "this", "that", "it"
    );

    @Autowired
    private KnowledgeDocRepository knowledgeDocRepository;

    @Override
    @Transactional(readOnly = true)
    public AiChatResponse askAssistant(AiChatRequest request) {
        String rawQuestion = request.getQuestion().trim();
        logger.info("[RAG QUERY] User asked: '{}'", rawQuestion);

        // 1. Term Tokenization & Query Preprocessing
        List<String> keywords = Arrays.stream(rawQuestion.toLowerCase().split("[^a-zA-Z0-9]+"))
                .filter(term -> term.length() > 2 && !STOP_WORDS.contains(term))
                .collect(Collectors.toList());

        if (keywords.isEmpty()) {
            return new AiChatResponse(
                    rawQuestion,
                    "Please ask a specific question about using the Skill Exchange Platform (e.g., adding skills, sending requests, matching, or messaging).",
                    Collections.emptyList(),
                    0.0
            );
        }

        // 2. Retrieval Phase: Fetch candidate knowledge chunks from knowledge base
        List<KnowledgeDoc> allDocs = knowledgeDocRepository.findAll();
        Map<KnowledgeDoc, Double> docScores = new HashMap<>();

        for (KnowledgeDoc doc : allDocs) {
            double score = computeRelevanceScore(doc, keywords, rawQuestion.toLowerCase());
            if (score > 0.15) {
                docScores.put(doc, score);
            }
        }

        // 3. Rank retrieved documents
        List<Map.Entry<KnowledgeDoc, Double>> rankedDocs = docScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .collect(Collectors.toList());

        // 4. Grounded Generation Phase
        if (rankedDocs.isEmpty()) {
            logger.info("[RAG NO MATCH] No relevant context found in knowledge base for '{}'", rawQuestion);
            return new AiChatResponse(
                    rawQuestion,
                    "I am the Skill Exchange Assistant, but I couldn't find specific documentation covering your query in the platform knowledge base. "
                            + "You can ask me about: adding skills to your profile, discovering learning partners, sending and accepting exchange requests, or using the messaging feature.",
                    Collections.emptyList(),
                    0.0
            );
        }

        // Construct grounded answer from retrieved context
        KnowledgeDoc topDoc = rankedDocs.get(0).getKey();
        double confidence = Math.min(1.0, rankedDocs.get(0).getValue());

        List<String> sources = rankedDocs.stream()
                .map(e -> e.getKey().getTitle() + " (" + e.getKey().getCategory() + ")")
                .collect(Collectors.toList());

        StringBuilder answerBuilder = new StringBuilder();
        answerBuilder.append(topDoc.getContent());

        // If there is a complementary second document, synthesize additional note
        if (rankedDocs.size() > 1 && rankedDocs.get(1).getValue() > 0.4) {
            KnowledgeDoc secondaryDoc = rankedDocs.get(1).getKey();
            answerBuilder.append("\n\nRelated Tip (").append(secondaryDoc.getTitle()).append("): ")
                    .append(secondaryDoc.getContent());
        }

        logger.info("[RAG GENERATED] Grounded response generated using source '{}' with confidence {:.2f}",
                topDoc.getTitle(), confidence);

        return new AiChatResponse(
                rawQuestion,
                answerBuilder.toString(),
                sources,
                confidence
        );
    }

    private double computeRelevanceScore(KnowledgeDoc doc, List<String> keywords, String queryLower) {
        double score = 0.0;
        String titleLower = doc.getTitle().toLowerCase();
        String contentLower = doc.getContent().toLowerCase();
        String docKeywordsLower = (doc.getKeywords() != null ? doc.getKeywords() : "").toLowerCase();
        String topicLower = doc.getTopic().toLowerCase();

        // Exact phrase match bonus
        if (contentLower.contains(queryLower) || titleLower.contains(queryLower)) {
            score += 1.0;
        }

        for (String kw : keywords) {
            if (titleLower.contains(kw)) {
                score += 0.5; // High weight for title match
            }
            if (topicLower.contains(kw)) {
                score += 0.4;
            }
            if (docKeywordsLower.contains(kw)) {
                score += 0.3;
            }
            if (contentLower.contains(kw)) {
                score += 0.15;
            }
        }

        return score;
    }
}
