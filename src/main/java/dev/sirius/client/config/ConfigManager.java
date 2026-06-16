package dev.sirius.client.config;

import com.google.gson.*;
import dev.sirius.client.SiriusClient;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.ModuleManager;
import dev.sirius.client.module.setting.Setting;

import java.io.*;

public class ConfigManager {

    private final File configFile;
    private final Gson gson;

    public ConfigManager(File siriusDir) {
        this.configFile = new File(siriusDir, "config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save(ModuleManager moduleManager) {
        try {
            JsonObject root = new JsonObject();
            JsonObject modulesObj = new JsonObject();

            for (Module module : moduleManager.getModules()) {
                JsonObject moduleObj = new JsonObject();
                moduleObj.addProperty("enabled", module.isEnabled());
                moduleObj.addProperty("keybind", module.getKeybind());
                moduleObj.addProperty("hudX", module.getHudX());
                moduleObj.addProperty("hudY", module.getHudY());

                JsonObject settingsObj = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    Object value = setting.getValue();
                    if (value instanceof Boolean) {
                        settingsObj.addProperty(setting.getName(), (Boolean) value);
                    } else if (value instanceof Number) {
                        settingsObj.addProperty(setting.getName(), (Number) value);
                    } else if (value instanceof String) {
                        settingsObj.addProperty(setting.getName(), (String) value);
                    }
                }
                moduleObj.add("settings", settingsObj);
                modulesObj.add(module.getName(), moduleObj);
            }

            root.add("modules", modulesObj);

            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(root, writer);
            }
        } catch (IOException e) {
            SiriusClient.logger.error("Failed to save config", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void load(ModuleManager moduleManager) {
        if (!configFile.exists()) return;

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            if (root == null || !root.has("modules")) return;

            JsonObject modulesObj = root.getAsJsonObject("modules");

            for (Module module : moduleManager.getModules()) {
                if (!modulesObj.has(module.getName())) continue;

                JsonObject moduleObj = modulesObj.getAsJsonObject(module.getName());

                if (moduleObj.has("keybind")) {
                    module.setKeybind(moduleObj.get("keybind").getAsInt());
                }
                if (moduleObj.has("hudX")) {
                    module.setHudX(moduleObj.get("hudX").getAsInt());
                }
                if (moduleObj.has("hudY")) {
                    module.setHudY(moduleObj.get("hudY").getAsInt());
                }

                if (moduleObj.has("settings")) {
                    JsonObject settingsObj = moduleObj.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (!settingsObj.has(setting.getName())) continue;
                        JsonElement element = settingsObj.get(setting.getName());
                        try {
                            if (setting.isBoolean()) {
                                ((Setting<Boolean>) setting).setValue(element.getAsBoolean());
                            } else if (setting.getValue() instanceof Integer) {
                                ((Setting<Integer>) setting).setValue(element.getAsInt());
                            } else if (setting.getValue() instanceof Float) {
                                ((Setting<Float>) setting).setValue(element.getAsFloat());
                            } else if (setting.getValue() instanceof Double) {
                                ((Setting<Double>) setting).setValue(element.getAsDouble());
                            } else if (setting.isString()) {
                                ((Setting<String>) setting).setValue(element.getAsString());
                            }
                        } catch (Exception e) {
                            SiriusClient.logger.warn("Failed to load setting: " + setting.getName(), e);
                        }
                    }
                }

                if (moduleObj.has("enabled") && moduleObj.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }
            }
        } catch (Exception e) {
            SiriusClient.logger.error("Failed to load config", e);
        }
    }
}
