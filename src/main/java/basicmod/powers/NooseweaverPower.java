package basicmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.Deathbringer.makeID;

public class NooseweaverPower extends BasePower {
    public static final String POWER_ID = makeID("NooseweaverPower");
    private int cardsPlayedThisTurn;

    public NooseweaverPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.cardsPlayedThisTurn = 0;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        cardsPlayedThisTurn++;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && cardsPlayedThisTurn > 0) {  // Check if cards were played
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m, owner, new StranglePower(m, cardsPlayedThisTurn * this.amount), cardsPlayedThisTurn * this.amount));
                }
            }
            cardsPlayedThisTurn = 0; // Reset the counter for the next turn
        }
    }
}