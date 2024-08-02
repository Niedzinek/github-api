package com.example.github_api.Services;

import com.example.github_api.dto.BranchDto;
import com.example.github_api.dto.RepositoryDto;
import com.example.github_api.exceptionHandlers.ServerErrorException;
import com.example.github_api.exceptionHandlers.UserNotFoundException;
import com.example.github_api.models.Branch;
import com.example.github_api.models.Commit;
import com.example.github_api.models.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private final WebClient webClient;

    public RepositoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public Mono<List<RepositoryDto>> findAllRepositoriesByUsername(String username){
        return fetchRepositories(username)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::enrichRepositoryWithBranches)
                .collectList()
                .map(this::toRepositoryDtos);
    }

    private Mono<List<Repository>> fetchRepositories(String username){
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    if(clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UserNotFoundException("User not found"));
                    }else {
                        return Mono.error(new RuntimeException("An error occured"));
                    }
                })
                .bodyToFlux(Repository.class)
                .filter(repo ->!repo.isFork())
                .collectList()
                .switchIfEmpty(Mono.error(new ServerErrorException("Failed to fetch repositories")));
    }

    private Mono<List<Branch>> fetchBranches(Repository repo){
        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", repo.getOwner().getLogin(), repo.getName())
                .retrieve()
                .bodyToFlux(Branch.class)
                .collectList();
    }

    private Mono<Commit> fetchCommit(Repository repo, Branch branch){
        return webClient.get()
                .uri("/repos/{owner}/{repo}/commits/{branch}",repo.getOwner().getLogin(), repo.getName(), branch.getName())
                .retrieve()
                .bodyToMono(Commit.class);
    }

    private Mono<Branch> enrichBranchWithCommit(Repository repo, Branch branch){
        return fetchCommit(repo, branch)
                .map(commit -> {
                    branch.setLastCommit(commit);
                    return branch;
                });
    }

    private Mono<Repository> enrichRepositoryWithBranches(Repository repo){
        return fetchBranches(repo)
                .flatMapMany(Flux::fromIterable)
                .flatMap(branch -> enrichBranchWithCommit(repo, branch))
                .collectList()
                .map(branches -> {
                    repo.setBranches(branches);
                    return repo;
                });
    }


private List<RepositoryDto> toRepositoryDtos(List<Repository> repositories){
    return repositories.stream()
            .map(repo -> {
                RepositoryDto repositoryDto = new RepositoryDto();
                repositoryDto.setRepositoryName(repo.getName());
                repositoryDto.setOwnerLogin(repo.getOwner().getLogin());

                List<BranchDto> branches = repo.getBranches()
                        .stream()
                        .map(branch -> {
                            BranchDto branchDto = new BranchDto();
                            branchDto.setName(branch.getName());
                            branchDto.setLastCommitSha(branch.getLastCommit().getSha());
                            return branchDto;
                        })
                        .collect(Collectors.toList());
                repositoryDto.setBranchList(branches);
                return repositoryDto;
            })
            .collect(Collectors.toList());
    }
}





