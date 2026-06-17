package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardModule extends Module {

    private final Setting<Boolean> showNumbers;
    private final Setting<Boolean> customBackground;

    public ScoreboardModule() {
        super("Scoreboard", "Custom styled scoreboard", Category.HUD, 0);
        showNumbers = addSetting(new Setting<>("Show Numbers", true));
        customBackground = addSetting(new Setting<>("Custom Background", true));
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return;

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> filteredScores = scores.stream()
                .filter(s -> s.getPlayerName() != null && !s.getPlayerName().startsWith("#"))
                .collect(Collectors.toList());

        if (filteredScores.size() > 15) {
            filteredScores = new ArrayList<>(filteredScores.subList(filteredScores.size() - 15, filteredScores.size()));
        }

        if (!customBackground.getValue()) return;

        FontRenderer fr = mc.fontRendererObj;
        int maxWidth = fr.getStringWidth(objective.getDisplayName());

        for (Score score : filteredScores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String line = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            if (showNumbers.getValue()) {
                line += ": " + score.getScorePoints();
            }
            maxWidth = Math.max(maxWidth, fr.getStringWidth(line));
        }

        int x = getHudX();
        int y = getHudY();
        int width = maxWidth + 12;
        int height = (filteredScores.size() + 1) * 12 + 6;

        setHudWidth(width);
        setHudHeight(height);

        RenderUtil.drawRoundedRect(x, y, width, height, 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 200));

        // Title
        int titleWidth = fr.getStringWidth(objective.getDisplayName());
        fr.drawStringWithShadow(objective.getDisplayName(), x + (width - titleWidth) / 2f, y + 4, ColorUtil.DARK_RED);

        // Scores
        int lineY = y + 16;
        for (int i = filteredScores.size() - 1; i >= 0; i--) {
            Score score = filteredScores.get(i);
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String line = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());

            fr.drawStringWithShadow(line, x + 4, lineY, ColorUtil.WHITE);

            if (showNumbers.getValue()) {
                String points = String.valueOf(score.getScorePoints());
                fr.drawStringWithShadow(points, x + width - fr.getStringWidth(points) - 4, lineY, ColorUtil.DARK_RED);
            }

            lineY += 12;
        }
    }
}
