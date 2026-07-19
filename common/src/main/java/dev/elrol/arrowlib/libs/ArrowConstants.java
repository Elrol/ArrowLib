package dev.elrol.arrowlib.libs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;

/**
 * Central configurations, paths, and platform wrappers for ArrowLib.
 */
public class ArrowConstants {

    /** Global Mod ID identifier string. */
    public static final String MODID = "arrowlib";

    /**
     * Factory utility to create a universally structured, human-readable GSON instance.
     * Sets up indentation, line breaks, and turns off character escaping for intuitive JSON editing.
     *
     * @return A pre-configured {@link Gson} instance with pretty-printing activated.
     */
    public static Gson makeGSON() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    /**
     * Executes the passed instructions solely if the project is running inside a development workspace environment.
     * Bypasses execution on user production clients seamlessly.
     *
     * @param runnable Code actions block to perform under development environments.
     */
    public static void debug(Runnable runnable) {
        if(Platform.isDevelopmentEnvironment()) {
            runnable.run();
        }
    }

    /** Inner container referencing online network configurations. */
    public static class URLs {
        /** Remote Gist URL used to check if a mod (primarily commissions) is authorized to run. */
        protected static final String STATUS = "https://api.github.com/gists/25db980d68838b3d70b12dfbaab19e36";
    }
}
