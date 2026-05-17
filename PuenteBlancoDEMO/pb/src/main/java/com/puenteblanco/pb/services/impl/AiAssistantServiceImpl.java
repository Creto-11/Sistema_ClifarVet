package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.services.interfaces.AiAssistantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAssistantServiceImpl implements AiAssistantService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @PostConstruct
    public void cargarManualEnVectorStore() {
        try {
            ClassPathResource resource = new ClassPathResource("manual/manual_veterinario.txt");

            String contenido = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);

            List<Document> documentos = dividirManual(contenido);

            vectorStore.add(documentos);

            System.out.println("Manual veterinario cargado correctamente en el VectorStore.");

        } catch (Exception e) {
            System.err.println("No se pudo cargar el manual veterinario para RAG: " + e.getMessage());
        }
    }

    private List<Document> dividirManual(String contenido) {
        List<Document> documentos = new ArrayList<>();

        String[] bloques = contenido.split("\\n\\n");

        for (int i = 0; i < bloques.length; i++) {
            String bloque = bloques[i].trim();

            if (!bloque.isBlank()) {
                documentos.add(new Document(bloque));
            }
        }

        return documentos;
    }

    @Override
    public String askPublicAssistant(String userMessage) {

        String systemPrompt = """
                Eres el asistente virtual de la Clínica Veterinaria Puente Blanco.

                Debes responder SOLO con base en el contexto recuperado del manual veterinario.

                Puedes responder sobre:
                - servicios de la clínica
                - orientación básica sobre mascotas
                - vacunas
                - desparasitación
                - primeros auxilios veterinarios generales
                - cuándo acudir presencialmente a la clínica

                No puedes:
                - diagnosticar definitivamente
                - recetar medicamentos
                - inventar información
                - reemplazar al veterinario

                Si el contexto no contiene información suficiente, responde:
                "Para una orientación segura, se recomienda acudir a la Clínica Veterinaria Puente Blanco."

                Si el caso parece grave, recomienda atención presencial inmediata.
                """;

        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .query(userMessage)
                        .topK(3)
                        .build())
                .build();

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .advisors(advisor)
                .call()
                .content();
    }

    @Override
    public String askPrivateAssistant(String userMessage) {

        String systemPrompt = """
                Eres el asistente virtual de la Clínica Veterinaria Puente Blanco.

                El usuario está autenticado en el sistema.

                Debes responder usando el contexto recuperado del manual veterinario.
                No diagnostiques definitivamente.
                No recetes medicamentos.
                No reemplaces al veterinario.

                Si es una emergencia, recomienda acudir presencialmente de inmediato.
                """;

        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .query(userMessage)
                        .topK(3)
                        .build())
                .build();

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .advisors(advisor)
                .call()
                .content();
    }
}