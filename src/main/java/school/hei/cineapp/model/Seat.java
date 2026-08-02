package school.hei.cineapp.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Seat(String id, String number, String roomId) {}
