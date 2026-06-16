package dev.sirius.client.module;

import dev.sirius.client.module.modules.hud.*;
import dev.sirius.client.module.modules.visual.*;
import dev.sirius.client.module.modules.utility.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void registerModules() {
        // HUD modules
        modules.add(new FPSCounter());
        modules.add(new CPSCounter());
        modules.add(new ArmorStatus());
        modules.add(new PotionEffects());
        modules.add(new Coordinates());
        modules.add(new Direction());
        modules.add(new ClockModule());
        modules.add(new PingDisplay());
        modules.add(new Keystrokes());
        modules.add(new ScoreboardModule());
        modules.add(new BossBarModule());

        // Visual modules
        modules.add(new HitColor());
        modules.add(new CustomCrosshair());
        modules.add(new Fullbright());
        modules.add(new TNTTimer());
        modules.add(new CapeModule());

        // Utility modules
        modules.add(new FPSBoost());
        modules.add(new ChatFilter());
        modules.add(new AutoGG());
        modules.add(new Zoom());
        modules.add(new ToggleSprint());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public Module getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream()
                .filter(m -> m.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for (Module module : modules) {
                if (module.getKeybind() == key) {
                    module.toggle();
                }
            }
        }
    }
}
