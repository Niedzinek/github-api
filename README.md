# GitHub API Integration Service

## Overview

This project provides a RESTful API for fetching GitHub repository information for a given username. It retrieves repositories (excluding forks), their branches, and the latest commit for each branch, returning the data in a structured JSON format.

## Features

- Fetches all repositories for a given GitHub username.
- Excludes forked repositories.
- Retrieves branches for each repository.
- Retrieves the latest commit for each branch.
- Provides meaningful error messages in JSON format for not found users and server errors.

## Getting Started

## Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- Git

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Niedzinek/github-api.git
   ```

2. Navigate to the project directory

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

4. To run the application use following command:

   ```bash
   mvn spring-boot:run
   ```

## Usage

- Endpoints

   ```
   GET /{username}
   ```

- Example usage

   ```
   curl -X GET http://localhost:8080/Niedzinek
   ```

- Example response
   - 200 OK

     ```
     [
        {
          "repositoryName": "repository1",
          "ownerLogin": "username",
          "branchList": [
           {
           "name": "main",
           "lastCommitSha": "commit-sha1"
           },
           {
              "name": "dev",
              "lastCommitSha": "commit-sha2"
           }
        ]
        },
        {
          "repositoryName": "repository2",
          "ownerLogin": "username",
          "branchList": [
            {
              "name": "main",
              "lastCommitSha": "commit-sha3"
            }
          ]
        }
     ]
     ```
   - 404 Not Found
     
     ```
     {
        "status": 404,
        "message": "User not found"
     }
     ```
   - 500 Internal error

     ```
     {
        "status": 500,
        "message": "An unexpected error occurred"
     }
     ```


