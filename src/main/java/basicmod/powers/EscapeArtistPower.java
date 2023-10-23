package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static basicmod.Deathbringer.makeID;

public class EscapeArtistPower extends BasePower {
    public static final String POWER_ID = makeID("EscapeArtistPower");

    // Add these variables to keep track of the total strength and energy gain
    public int totalStrengthGain;
    public int totalEnergyGain;

    public EscapeArtistPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        this.totalStrengthGain = 6;
        this.totalEnergyGain = 2;
        updateDescription();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_ENTANGLED", 0.05F);
    }


    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]);
        sb.append(this.amount);
        sb.append(DESCRIPTIONS[1]);
        sb.append(this.totalStrengthGain);
        sb.append(DESCRIPTIONS[2]);

        for (int i = 0; i < this.totalEnergyGain; ++i) {
            sb.append("[E] ");
        }

        sb.append(".");
        this.description = sb.toString();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.amount--;
        updateDescription();
        if (this.amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    // Override the onRemove method to apply the effects when the Power is removed
    @Override
    public void onRemove() {
        flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, totalStrengthGain), totalStrengthGain));
        addToBot(new GainEnergyAction(totalEnergyGain));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.totalStrengthGain += 6;
        this.totalEnergyGain += 2;
        updateDescription();
    }

    public boolean canPlayCard(AbstractCard card) {
        return card.type != AbstractCard.CardType.ATTACK;
    }
}
