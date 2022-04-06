package fr.triedge.fm.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    public static boolean isImage(String path) throws IOException {
        String contentType = Files.probeContentType(Paths.get(path));
        return (contentType != null && contentType.toLowerCase().contains("image"));
    }
}
