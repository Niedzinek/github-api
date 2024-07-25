package com.example.github_api.exceptionHandlers;

public class ErrorResponse {
    private String message;  // Krótkie podsumowanie błędu
    private int statusCode;  // Kod statusu HTTP
//    private String details;  // Dodatkowe szczegóły błędu (opcjonalnie)

    // Konstruktor
//    public ErrorResponse(String message, int statusCode, String details) {
//        this.message = message;
//        this.statusCode = statusCode;
//        this.details = details;
//    }

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }


    // Gettery i Settery
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }
}

