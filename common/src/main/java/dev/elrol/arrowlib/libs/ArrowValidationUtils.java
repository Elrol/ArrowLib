package dev.elrol.arrowlib.libs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Mod lifecycle management validation utilities.
 * Connects to a remote storage resource check to authenticate module deployment configurations.
 */
public class ArrowValidationUtils {

    /**
     * Queries a remote network configuration resource map file target list to see if a mod ID's lifecycle authorization is enabled.
     *
     * If the remote data mapping structure returns explicit "DISABLED" parameters for the tracking ID, a runtime exception is thrown,
     * immediately halting loading states.
     *
     * @param modId Unique string identification key tag of the module being verified.
     * @throws RuntimeException if network IO validation breaks, or if authorization states evaluate to absolute failure configurations.
     */
    public static void checkStatus(String modId) {
        String status = "ENABLED";

        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(ArrowConstants.URLs.STATUS).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Minecraft Mod)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                JsonObject apiResponse = JsonParser.parseReader(reader).getAsJsonObject();

                JsonObject filesNode = apiResponse.getAsJsonObject("files");
                if (filesNode != null && filesNode.has("commission_status.json")) {
                    JsonObject fileDetails = filesNode.getAsJsonObject("commission_status.json");
                    String rawJsonContent = fileDetails.get("content").getAsString();

                    JsonObject commissionsJson = JsonParser.parseString(rawJsonContent).getAsJsonObject();

                    if (commissionsJson.has(modId)) {
                        status = commissionsJson.get(modId).getAsString();
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to check status of " + modId, ex);
        }

        if (status.equalsIgnoreCase("DISABLED")) {
            throw new RuntimeException("Mod [" + modId + "] authorization failed. Please contact the developer.");
        }
    }

}