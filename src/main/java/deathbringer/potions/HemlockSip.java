package deathbringer.potions;

import basemod.BaseMod;
import deathbringer.character.Deathbringer;
import deathbringer.powers.VirulencePower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static deathbringer.Deathbringer.makeID;

public class HemlockSip extends BasePotion {
    public static final String ID = makeID(HemlockSip.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(0, 200, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(0, 230, 23);
    private static final Color SPOTS_COLOR = null;

    public HemlockSip() {
        super(ID, 4, PotionRarity.COMMON, PotionSize.MOON, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Deathbringer.Enums.DEATHBRINGER;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("deathbringer:virulence")), BaseMod.getKeywordDescription("deathbringer:virulence")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            target = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, 1), 1));
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new VirulencePower(target, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }

}
