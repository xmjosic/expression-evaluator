package hr.xmjosic.expressionevaluator;

import java.io.File;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

public class FileUtil {

  @SneakyThrows
  public static String readResourceFromClasspath(String filePath) {
    File file = new ClassPathResource(filePath).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }
}
