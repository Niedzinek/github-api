package com.example.github_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BranchDto {
    private String name;
    private String lastCommitSha;
}
