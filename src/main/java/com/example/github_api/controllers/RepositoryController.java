package com.example.github_api.controllers;

import com.example.github_api.Services.RepositoryService;
import com.example.github_api.dto.RepositoryDto;
import com.example.github_api.models.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryController {
    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "repositories/{username}")
    ResponseEntity<List<RepositoryDto>> readAllRepositories(@PathVariable String username){
        return ResponseEntity.ok(repositoryService.FindAllRepositoriesByUsername(username));
    }
}
