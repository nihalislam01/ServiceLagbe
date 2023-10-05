package com.javafest.ServiceLagbe.records;

import java.util.List;

public record CreateProjectRequest(String name, String location, List<String> lookingFor) {
}
