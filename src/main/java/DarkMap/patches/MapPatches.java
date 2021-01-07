package DarkMap.patches;

import DarkMap.DarkMap;
import DarkMap.utils.ExtraColors;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.vfx.MapCircleEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MapPatches {
    // Background image tint
    @SpirePatch( clz = DungeonMap.class, method = SpirePatch.CONSTRUCTOR )
    public static class MapBaseColorPatch {
        @SpirePostfixPatch
        public static void Postfix(DungeonMap map) {
            Color baseMapColor = (Color) ReflectionHacks.getPrivate(map, DungeonMap.class, "baseMapColor");
            baseMapColor.set(ExtraColors.mapBg);
        }
    }

    // Map Edge colors
    @SpirePatch( clz = MapRoomNode.class, method = SpirePatch.CONSTRUCTOR )
    public static class MapEdgeColorPatches {
        @SpirePostfixPatch
        public static void Postfix(MapRoomNode node) {
            //Color baseMapColor = (Color) ReflectionHacks.getPrivate(map, DungeonMap.class, "baseMapColor");
            //Color edgeColor = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "AVAILABLE_COLOR");
            ReflectionHacks.setPrivateStaticFinal(MapRoomNode.class, "AVAILABLE_COLOR", ExtraColors.MAP_EDGE_AVAILABLE);
            //baseMapColor.set(ExtraColors.mapBg);
        }
    }

    // --------------------------------------------------------------------------------
    // Map Nodes
    @SpirePatch(clz = MapRoomNode.class, method = "render")
    public static class MapRoomNodeOutlinePatch {
        private static int loc = 0;
        private static final String replaceBG = "{" + "if (!"+ DarkMap.class.getName() + ".isColoredMapEnabled()) " + "sb.setColor(" + ExtraColors.class.getName() + ".MAP_ICON_BG);" + "$_ = $proceed($$); " + "}";
        private static final String replaceFG = "{" + "if (!"+ DarkMap.class.getName() + ".isColoredMapEnabled()) " + "sb.setColor(" + ExtraColors.class.getName() + ".MAP_ICON_FG);" + "$_ = $proceed($$); " + "}";
        private static final String replaceCircle = "{" + "if (!"+ DarkMap.class.getName() + ".isColoredMapEnabled()) " + "sb.setColor(" + ExtraColors.class.getName() + ".MAP_CIRCLE);" + "$_ = $proceed($$); " + "}";

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        if (loc == 0 || loc == 1)
                            m.replace(replaceBG);
                        else if (loc == 2 || loc == 3)
                            m.replace(replaceFG);
                        else if (loc == 4 || loc == 5)
                            m.replace(replaceCircle);
                        ++loc;
                    }
                }
            };
        }
    }

    // --------------------------------------------------------------------------------

    // Boss icon
    @SpirePatch(clz = DungeonMap.class, method = "renderBossIcon")
    public static class BossIconPatch {
        private static boolean isFirst = true;

        // Boss outline
        public static Color getColorBG(float alpha) {
            return new Color(
                    ExtraColors.BOSS_BG.r,
                    ExtraColors.BOSS_BG.g,
                    ExtraColors.BOSS_BG.b,
                    alpha );
        }

        // Boss main color
        public static Color getColorFG(float alpha) {
            return new Color(
                    ExtraColors.BOSS_FG.r,
                    ExtraColors.BOSS_FG.g,
                    ExtraColors.BOSS_FG.b,
                    alpha );
        }

        private static final String replaceBG = "{ " + "$1 = " + BossIconPatch.class.getName() + ".getColorBG(this.bossNodeColor.a);" + "$_ = $proceed($$); }";
        private static final String replaceFG = "{ " + "$1 = " + BossIconPatch.class.getName() + ".getColorFG(this.bossNodeColor.a);" + "$_ = $proceed($$); }";

        public static ExprEditor Instrument() {

            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("setColor")) {
                        if (isFirst) {
                            m.replace( replaceBG );
                            isFirst = false;
                        }
                        else
                            m.replace(replaceFG);
                    }
                }
            };
        }
    }

    // Map circling effect
    @SpirePatch(clz = MapCircleEffect.class, method = "render")
    public static class MapCircleEffectPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("setColor")) {
                        m.replace(
                                "{ " +
                                        "$1 = " + ExtraColors.class.getName() + ".MAP_CIRCLE;"
                                        + "$_ = $proceed($$); }"
                        );
                    }
                }
            };
        }
    }
}
