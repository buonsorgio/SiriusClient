package dev.sirius.client.cape;

import dev.sirius.client.SiriusClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeManager {

    private static final String CAPE_URL = "https://sirius-client.dev/capes/%s.png";
    private static final ResourceLocation DEFAULT_CAPE = new ResourceLocation("sirius", "textures/cape/default_cape.png");

    private final File capeDir;
    private final Map<UUID, ResourceLocation> capeCache = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> fetchAttempted = new ConcurrentHashMap<>();

    public CapeManager(File siriusDir) {
        this.capeDir = new File(siriusDir, "capes");
        if (!capeDir.exists()) {
            capeDir.mkdirs();
        }
    }

    public ResourceLocation getCapeTexture(UUID playerUUID) {
        if (capeCache.containsKey(playerUUID)) {
            return capeCache.get(playerUUID);
        }

        if (!fetchAttempted.containsKey(playerUUID)) {
            fetchAttempted.put(playerUUID, true);
            fetchCapeAsync(playerUUID);
        }

        return DEFAULT_CAPE;
    }

    private void fetchCapeAsync(UUID playerUUID) {
        Thread thread = new Thread(() -> {
            try {
                String urlStr = String.format(CAPE_URL, playerUUID.toString().replace("-", ""));
                File cacheFile = new File(capeDir, playerUUID.toString() + ".png");

                // Check local cache first
                if (cacheFile.exists()) {
                    ResourceLocation loc = registerCapeTexture(playerUUID, cacheFile);
                    if (loc != null) {
                        capeCache.put(playerUUID, loc);
                        return;
                    }
                }

                // Try downloading
                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "SiriusClient/" + SiriusClient.VERSION);

                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedImage image = ImageIO.read(is);
                    is.close();

                    if (image != null) {
                        ImageIO.write(image, "png", cacheFile);
                        ResourceLocation loc = registerCapeTexture(playerUUID, cacheFile);
                        if (loc != null) {
                            capeCache.put(playerUUID, loc);
                        }
                    }
                } else {
                    // Use default cape
                    capeCache.put(playerUUID, DEFAULT_CAPE);
                }

                conn.disconnect();
            } catch (IOException e) {
                // Network error — fall back to default
                capeCache.put(playerUUID, DEFAULT_CAPE);
                SiriusClient.logger.debug("Could not fetch cape for " + playerUUID + ": " + e.getMessage());
            }
        }, "SiriusCapeFetcher-" + playerUUID);

        thread.setDaemon(true);
        thread.start();
    }

    private ResourceLocation registerCapeTexture(UUID playerUUID, File capeFile) {
        try {
            ResourceLocation location = new ResourceLocation("sirius", "capes/" + playerUUID.toString());
            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

            ThreadDownloadImageData textureData = new ThreadDownloadImageData(
                    capeFile, null, DEFAULT_CAPE, new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    return image;
                }

                @Override
                public void skinAvailable() {}
            });

            textureManager.loadTexture(location, textureData);
            return location;
        } catch (Exception e) {
            SiriusClient.logger.error("Failed to register cape texture", e);
            return null;
        }
    }

    public ResourceLocation getDefaultCape() {
        return DEFAULT_CAPE;
    }

    public void clearCache() {
        capeCache.clear();
        fetchAttempted.clear();
    }
}
