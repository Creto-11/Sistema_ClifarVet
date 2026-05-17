package com.puenteblanco.pb.controller.ai;

import com.puenteblanco.pb.dto.request.AiChatRequestDto;
import com.puenteblanco.pb.dto.response.AiChatResponseDto;
import com.puenteblanco.pb.services.interfaces.AiAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @PostMapping("/public/chat")
    public ResponseEntity<AiChatResponseDto> publicChat(
            @RequestBody AiChatRequestDto dto) {

        String response = aiAssistantService.askPublicAssistant(dto.getMessage());

        return ResponseEntity.ok(new AiChatResponseDto(response));
    }

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponseDto> privateChat(
            @RequestBody AiChatRequestDto dto) {

        String response = aiAssistantService.askPrivateAssistant(dto.getMessage());

        return ResponseEntity.ok(new AiChatResponseDto(response));
    }
}