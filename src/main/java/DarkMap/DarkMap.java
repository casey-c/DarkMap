package DarkMap;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModList;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class DarkMap implements PostInitializeSubscriber {
    private static boolean COLORED_MAP_ENABLED;

    public static void initialize() {
        new DarkMap();
    }

    public DarkMap() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerModBadge(new Texture("DarkMap/badge.png"), "Dark Map", "ojb", "Gives the map a dark theme", null);

        // Cache this so we don't have to search through all mods everytime
        COLORED_MAP_ENABLED = Loader.isModLoaded("coloredmap");

        System.out.println("Colored map enabled: " + COLORED_MAP_ENABLED);
    }

    public static boolean isColoredMapEnabled() {
        return COLORED_MAP_ENABLED;
    }
}
