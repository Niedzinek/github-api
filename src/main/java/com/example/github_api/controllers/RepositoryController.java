package com.example.github_api.controllers;

import com.example.github_api.Services.RepositoryService;
import com.example.github_api.dto.RepositoryDto;
import com.example.github_api.exceptionHandlers.ErrorResponse;
import com.example.github_api.exceptionHandlers.ServerErrorException;
import com.example.github_api.exceptionHandlers.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
public class RepositoryController {
    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "{username}")
    Mono<List<RepositoryDto>> readAllRepositories(@PathVariable String username) {

        return repositoryService.findAllRepositoriesByUsername(username);
//                .onErrorResume(UserNotFoundException.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)))
//                .onErrorResume(ServerErrorException.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)))
//                .onErrorResume(RuntimeException.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    }

