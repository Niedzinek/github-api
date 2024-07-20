package com.example.github_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    private String name;
    private Owner owner;
    private List<Branch> branches;

    @JsonProperty("fork")
    private boolean isFork;
}
