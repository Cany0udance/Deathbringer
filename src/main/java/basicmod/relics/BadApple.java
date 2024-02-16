package basicmod.relics;

import basicmod.Deathbringer;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BadApple extends BaseRelic implements OnReceivePowerRelic {
    private static final String NAME = "BadApple";
    public static final String ID = Deathbringer.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public BadApple() {
        super(ID, NAME, basicmod.character.Deathbringer.Enums.CARD_COLOR, RARITY, SOUND);
        this.counter = 2; // Initialize counter to 2
    }

    @Override
    public void atTurnStart() {
        this.counter = 2; // Reset the counter to 2 at the start of each turn
        this.grayscale = false; // Ensure the relic is not greyscale at the start of the turn
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target) {
        if (target == AbstractDungeon.player && power.ID.equals(PoisonPower.POWER_ID)) {
            if (this.counter > 0) {
                this.counter--;
                flash(); // Visual effect to indicate relic activation
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));

                if (this.counter == 0) {
                    this.grayscale = true; // Turn greyscale when counter reaches 0
                }
            }
        }
        return true; // Return true to allow the power application to proceed
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BadApple();
    }
}
