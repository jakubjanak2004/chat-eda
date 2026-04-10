package dto.response;
import java.io.InputStream;

public record PictureDTO(InputStream stream, String contentType) {}
