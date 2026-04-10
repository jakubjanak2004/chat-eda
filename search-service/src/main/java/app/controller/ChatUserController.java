package app.controller;

import app.service.ChatUserService;
import dto.response.ChatUserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
public class ChatUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatUserController.class);
    private final ChatUserService chatUserService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ChatUserDTO>> getUsersNotMe(@RequestParam(required = false) String query, Principal principal, @ParameterObject Pageable pageable) {
        LOGGER.info("GET /users?query={}&page={}&size={}&sort={}", query, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.ok(chatUserService.getUsersNotUsername(query, principal.getName(), pageable));
    }
}
