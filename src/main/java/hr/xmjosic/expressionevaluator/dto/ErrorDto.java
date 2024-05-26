package hr.xmjosic.expressionevaluator.dto;

import java.time.Instant;
import lombok.Builder;

/**
 * used to transfer data about an error
 *
 * @param timestamp the time of the error
 * @param status the http status of the error
 * @param error the type of error
 * @param message the message of the error
 * @param path the path
 */
@Builder
public record ErrorDto(Instant timestamp, int status, String error, String message, String path) {}
