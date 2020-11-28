package DarkMap.patches;

import DarkMap.utils.ExtraColors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.Legend;
import com.megacrit.cardcrawl.map.LegendItem;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class LegendPatches {
    // Main background color of the entire legend image
    @SpirePatch( clz = Legend.class, method = SpirePatch.CONSTRUCTOR )
    public static class LegendBackgroundColorPatch {
        @SpirePostfixPatch
        public static void Postfix(Legend legend) {
            legend.c = ExtraColors.LEGEND_BG.cpy();
        }
    }

    // Each (icon, Text) pair of the subitems in the legend
    @SpirePatch( clz = LegendItem.class, method = "render" )
    public static class LegendItemPatch {
        @SpirePrefixPatch
        public static void Prefix(LegendItem _item, SpriteBatch _sb, Color c) {
            float alpha = c.a;
            //c.set(ExtraColors.LEGEND_SUB_TEXT);
            c.set(ExtraColors.BOSS_FG);
            c.a = alpha;
        }
    }

    // MAIN LEGEND TEXT
    @SpirePatch(clz = Legend.class, method = "render")
    public static class LegendMainText
    {
        public static Color getColor(float alpha) {
            return new Color(
                    ExtraColors.LEGEND_MAIN_TEXT.r,
                    ExtraColors.LEGEND_MAIN_TEXT.g,
                    ExtraColors.LEGEND_MAIN_TEXT.b,
                    alpha );
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderFontCentered")) {
                        m.replace(
                                "{ " +
                                        "$6 = " + LegendMainText.class.getName() + ".getColor(this.c.a);"
                                        + "$_ = $proceed($$); }"
                        );
                    }
                }
            };
        }
    }
}
