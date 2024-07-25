# GitHub API Integration Service

<p style="color: blue; font-size: 20px;">Overview</p>

This project provides a RESTful API to fetch GitHub repository information for a given username. It retrieves repositories, their branches, and the latest commit for each branch, returning the data in a structured JSON format.

<p style="color: green; font-size: 38px;">Features</p>

- Fetches all repositories for a given GitHub username.
- Excludes forked repositories.
- Retrieves branches for each repository.
- Retrieves the latest commit for each branch.
- Provides meaningful error messages in JSON format for not found users and server errors.
