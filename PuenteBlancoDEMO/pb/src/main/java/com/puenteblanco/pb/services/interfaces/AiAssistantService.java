package com.puenteblanco.pb.services.interfaces;

public interface AiAssistantService {

    String askPublicAssistant(String userMessage);

    String askPrivateAssistant(String userMessage);

}