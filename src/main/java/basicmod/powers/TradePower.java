package basicmod.powers;

import basicmod.cards.attacks.*;
import basicmod.cards.skills.*;
import basicmod.cards.statuses.Slipup;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static basicmod.Deathbringer.makeID;

public class TradePower extends BasePower {
    public static final String POWER_ID = makeID("TradePower");
    private ArrayList<AbstractCard> shadowCardList = new ArrayList<>();

    public TradePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

}
