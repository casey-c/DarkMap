package DarkMap;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
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

    }
}
