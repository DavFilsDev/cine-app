package school.hei.cineapp.endpoint.event.consumer.model;

import school.hei.cineapp.PojaGenerated;
import school.hei.cineapp.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
