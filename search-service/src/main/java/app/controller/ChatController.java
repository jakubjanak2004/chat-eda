package app.controller;

import app.service.ChatService;
import app.service.MessageService;
import dto.response.ChatDTO;
import dto.response.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private final MessageService messageService;
    private final ChatService chatService;

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ChatDTO>> getChatsForMe(@RequestParam(required = false) String query, Principal principal, @ParameterObject Pageable pageable) {
        LOGGER.info("GET /chats/me?query={}&page={}&size={}&sort={}", query, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.ok(chatService.getChatsForUsername(query, principal.getName(), pageable));
    }

    @GetMapping(value = "/{chatId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MessageDTO>> getMessagesForChat(@PathVariable UUID chatId, @RequestParam(required = false) String query, @ParameterObject Pageable pageable) {
        LOGGER.info("GET /chats/{}/messages?page={}&size={}&sort={}", chatId, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.ok(messageService.getMessagesForChat(query, chatId, pageable));
    }
}

