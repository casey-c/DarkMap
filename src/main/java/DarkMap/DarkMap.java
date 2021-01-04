package DarkMap;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class DarkMap implements PostInitializeSubscriber {
    public static void initialize() {
        new DarkMap();
    }

    public DarkMap() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerModBadge(new Texture("DarkMap/badge.png"), "Dark Map", "ojb", "Gives the map a dark theme", null);
    }
}
