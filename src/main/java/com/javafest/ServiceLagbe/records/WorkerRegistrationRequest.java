package com.javafest.ServiceLagbe.records;


import java.util.List;

public record WorkerRegistrationRequest(String firstName, String lastName, String number,
                                        String pin, String location, String isAvailable, List<String> posts) {
}
