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

        // Initialize list of Shadow cards
        shadowCardList.add(new Mantle());
        shadowCardList.add(new Shroud());
        shadowCardList.add(new Protrusion());
        shadowCardList.add(new Eclipse());
        shadowCardList.add(new Liability());
        shadowCardList.add(new Injection());
        shadowCardList.add(new Admire());
        shadowCardList.add(new Expurgate());
        shadowCardList.add(new Shadowstep());
        shadowCardList.add(new VanishingAct());
        shadowCardList.add(new Sanctuary());
        shadowCardList.add(new SubconsciousKiller());
        shadowCardList.add(new ShadowStrike());
        shadowCardList.add(new ShadowDefend());
        shadowCardList.add(new ConcealedBlade());
        shadowCardList.add(new Slipup());
        shadowCardList.add(new Intuition());

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                gainBlockForShadowCards();
                this.isDone = true;
            }
        });
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            gainBlockForShadowCards();
        }
    }

    private void gainBlockForShadowCards() {
        int blockGain = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            for (AbstractCard shadowCard : shadowCardList) {
                if (c.cardID.equals(shadowCard.cardID)) {
                    blockGain += amount;
                }
            }
        }
        if (blockGain > 0) {
            addToBot(new GainBlockAction(owner, blockGain));
        }
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        for (AbstractCard shadowCard : shadowCardList) {
            if (card.cardID.equals(shadowCard.cardID)) {
                return false;
            }
        }
        return true;
    }
}
