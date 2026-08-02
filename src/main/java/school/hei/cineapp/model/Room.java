package school.hei.cineapp.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Room(String id, String number, int capacity) {}
