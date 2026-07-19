package dev.elrol.arrowlib.libs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.elrol.arrowlib.Arrowlib;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Automation wrapper utilities for managing configuration read/write operations utilizing JSON files.
 */
public class ArrowJsonUtils {
    /** Master configured static GSON configuration engine. */
    private static final Gson GSON = ArrowConstants.makeGSON();

    /**
     * Encodes a {@link JsonElement} payload string and exports it directly to a local file location.
     * Handles directory generation tree trees cleanly if missing.
     *
     * @param dir  The target disk directory folder destination.
     * @param name The final target text file string name (e.g., "config.json").
     * @param obj  The dynamic structured element payload content ready to be serialized.
     */
    public static void saveToJson(File dir, String name, JsonElement obj) {
        File file = new File(dir, name);

        if(dir.mkdirs()) {
            Arrowlib.LOGGER.warn("{} directory for OSMC created. If this happens more than once, there is an issue.", dir);
        }

        if(!file.exists()) {
            try {
                if(file.createNewFile()) {
                    Arrowlib.LOGGER.warn("New file {} created.", name);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try(FileWriter writer = new FileWriter(file)) {
            GSON.toJson(obj, writer);
            ArrowConstants.debug(() -> Arrowlib.LOGGER.info("Saved File {}", name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads and parses a local file back into a workable {@link JsonElement}.
     * If the target file doesn't exist yet, it exports the supplied default fallback parameter file.
     *
     * @param dir         The target disk directory folder destination.
     * @param name        The target file string name to be parsed.
     * @param defaultJson Optional fallback default template payload context to write out if file is missing.
     * @return The parsed JSON element object read from the file workspace, or the specified fallback option object.
     */
    @Nullable
    public static JsonElement loadFromJson(File dir, String name, @Nullable JsonElement defaultJson) {
        File file = new File(dir, name);

        if(file.exists() && file.length() > 0) {
            try(FileReader reader = new FileReader(file)) {
                JsonElement obj = GSON.fromJson(reader, JsonElement.class);

                if(obj != null && !obj.isJsonNull()) {
                    ArrowConstants.debug(() -> Arrowlib.LOGGER.info("Loaded File {}", name));
                    return obj;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(defaultJson != null) saveToJson(dir, name, defaultJson);
        return defaultJson;

    }
}
