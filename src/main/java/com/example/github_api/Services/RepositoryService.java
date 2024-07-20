package com.example.github_api.Services;

import com.example.github_api.dto.BranchDto;
import com.example.github_api.dto.RepositoryDto;
import com.example.github_api.models.Branch;
import com.example.github_api.models.Commit;
import com.example.github_api.models.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryService {

    private final WebClient webClient;

    public RepositoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public List<RepositoryDto> FindAllRepositoriesByUsername(String username){
        List<Repository> repositories = webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    if(clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return Mono.error(new RuntimeException("An error occured"));
                    }
                })
                .bodyToFlux(Repository.class)
                .filter(repo -> !repo.isFork())
                .collectList()
                .block();

        repositories.forEach(repo -> {
            List<Branch> branches = webClient.get()
                    .uri("/repos/{owner}/{repo}/branches", repo.getOwner().getLogin(), repo.getName())
                    .retrieve()
                    .bodyToFlux(Branch.class)
                    .collectList()
                    .block();

            branches.forEach(branch -> {
                Commit commit = webClient.get()
                        .uri("/repos/{owner}/{repo}/commits/{branch}", repo.getOwner().getLogin(), repo.getName(), branch.getName())
                        .retrieve()
                        .bodyToMono(Commit.class)
                        .block();
                branch.setLastCommit(commit);

            });

            repo.setBranches(branches);
        });
        List<RepositoryDto> repositoryDtoList = new ArrayList<>();
        repositories.forEach(repo -> {
            RepositoryDto dto = new RepositoryDto();
            dto.setRepositoryName(repo.getName());
            dto.setOwnerLogin(repo.getOwner().getLogin());

            List<BranchDto> branchDtoList = new ArrayList<>();
            repo.getBranches().forEach(branch -> {
                BranchDto branchDto = new BranchDto();
                branchDto.setName(branch.getName());
                branchDto.setLastCommitSha(branch.getLastCommit().getSha());
                branchDtoList.add(branchDto);
            });

            dto.setBranchList(branchDtoList);
            repositoryDtoList.add(dto);
        });



        return repositoryDtoList;
    }



}
