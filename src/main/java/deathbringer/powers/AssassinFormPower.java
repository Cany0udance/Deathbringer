package deathbringer.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import deathbringer.actions.AssassinFormAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import deathbringer.character.Deathbringer;
import deathbringer.util.ShadowUtility;

import static deathbringer.Deathbringer.makeID;

public class AssassinFormPower extends BasePower {
    public static final String POWER_ID = makeID("AssassinFormPower");
    private boolean flashPower;

    public AssassinFormPower(AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[0] + " #b" + amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        final int capturedAmount = this.amount;
        final AssassinFormPower capturedPower = this;

        // Add a delayed action that will execute after other turn-start effects
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ShadowUtility.resetFirstShadowplayTrigger();
                flashPower = false;
                addToBot(new AssassinFormAction(capturedAmount, capturedPower));
                this.isDone = true;
            }
        });
    }


    public void setFlash() {
        this.flashPower = true;
        this.flash();
    }
}