package basicmod.potions;

import basemod.BaseMod;
import basicmod.actions.DrawRandomShadowCardsAction;
import basicmod.character.Deathbringer;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.Deathbringer.makeID;

public class LiquidCongregation extends BasePotion {
    public static final String ID = makeID(LiquidCongregation.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(0, 0, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(0, 0, 23);
    private static final Color SPOTS_COLOR = null;

    public LiquidCongregation() {
        super(ID, 0, PotionRarity.RARE, PotionSize.EYE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Deathbringer.Enums.DEATHBRINGER;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("deathbringer:shadow")), BaseMod.getKeywordDescription("deathbringer:shadow")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            target = AbstractDungeon.player;
            this.addToBot(new ShuffleAllAction());
            this.addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, false));
            addToBot(new DrawRandomShadowCardsAction(target, BaseMod.MAX_HAND_SIZE));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0];
    }

}