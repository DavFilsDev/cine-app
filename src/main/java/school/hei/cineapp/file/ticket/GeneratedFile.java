package school.hei.cineapp.file.ticket;

import org.springframework.http.MediaType;

public record GeneratedFile(byte[] content, String filename, MediaType contentType) {}
