package com.puenteblanco.pb.service.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatAssistantService {

    private static final Logger log = LoggerFactory.getLogger(ChatAssistantService.class);
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatAssistantService(ChatModel chatModel, VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(chatModel).build();
        this.vectorStore = vectorStore;
    }

    public String chat(String userMessage) {
        try {
            // 1. Buscar contexto relevante en la base vectorial
            List<Document> similarDocuments = vectorStore.similaritySearch(
                    SearchRequest.query(userMessage).withTopK(3)
            );

            String context = similarDocuments.stream()
                    .map(Document::getContent)
                    .collect(Collectors.joining("\n\n"));

            // 2. Construir el system prompt con las restricciones
            String systemText = """
                Eres ClifarBot, el asistente virtual oficial de la Clínica Veterinaria CLIFARVET.
                Tu tarea es ayudar a los clientes usando EXCLUSIVAMENTE la información provista en la sección CONTEXTO.
                
                REGLAS ESTRICTAS:
                1. NUNCA emitas un diagnóstico médico.
                2. NUNCA recetes ni sugieras medicamentos.
                3. Si la pregunta requiere un diagnóstico, medicamentos, o es sobre síntomas preocupantes, debes invitar amablemente al usuario a agendar una cita o acudir a emergencias.
                4. Si la respuesta no se encuentra en el CONTEXTO, di "Lo siento, no tengo esa información. Por favor comunícate con la clínica directamente."
                5. Sé amable, conciso y profesional.
                
                CONTEXTO:
                {context}
                """;

            // 3. Llamar a Gemini y obtener la respuesta usando el fluent API
            return chatClient.prompt()
                    .system(s -> s.text(systemText).param("context", context))
                    .user(userMessage)
                    .call()
                    .content();
                    
        } catch (Exception e) {
            log.error("Error al procesar el chat: ", e);
            return "Lo siento, mis sistemas de IA están actualmente en mantenimiento o no se ha configurado la clave API correctamente.";
        }
    }
}
