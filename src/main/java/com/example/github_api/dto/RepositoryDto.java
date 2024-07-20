package com.example.github_api.dto;

import com.example.github_api.models.Branch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RepositoryDto {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchDto> branchList;
}
