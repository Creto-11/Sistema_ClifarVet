package com.puenteblanco.pb.config.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AiConfig {

    private static final Logger log = LoggerFactory.getLogger(AiConfig.class);

    @Value("classpath:docs/*.*")
    private Resource[] docResources;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    @Bean
    public CommandLineRunner initVectorStore(VectorStore vectorStore) {
        return args -> {
            try {
                List<Document> allDocuments = new ArrayList<>();
                for (Resource resource : docResources) {
                    String filename = resource.getFilename();
                    if (filename != null && filename.endsWith(".txt")) {
                        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                        Document doc = new Document(content);
                        doc.getMetadata().put("filename", filename);
                        allDocuments.add(doc);
                        log.info("Leído documento TXT: {}", filename);
                    } else if (filename != null && filename.endsWith(".pdf")) {
                        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
                        allDocuments.addAll(pdfReader.get());
                        log.info("Leído documento PDF: {}", filename);
                    }
                }

                if (!allDocuments.isEmpty()) {
                    TokenTextSplitter splitter = new TokenTextSplitter();
                    List<Document> splitDocuments = splitter.apply(allDocuments);
                    
                    // Al guardar en vectorStore se generan los embeddings llamando a la API de Gemini
                    vectorStore.add(splitDocuments);
                    log.info("Documentos cargados y procesados en VectorStore con éxito.");
                }
            } catch (Exception e) {
                log.warn("No se pudo inicializar VectorStore. Asegúrate de que tu GEMINI_API_KEY sea válida. Error: {}", e.getMessage());
            }
        };
    }
}
