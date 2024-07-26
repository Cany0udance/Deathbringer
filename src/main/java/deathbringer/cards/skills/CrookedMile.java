package deathbringer.cards.skills;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;

public class CrookedMile extends BaseCard {
    public static final String ID = makeID("CrookedMile");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1  // Energy cost
    );

    public CrookedMile() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExpertiseAction(p, 10)); // Draws cards until the hand is full

        // Queue an action to increase the cost of all cards in hand after drawing
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : p.hand.group) {
                    if (c.cost >= 0) { // Check to ensure the card has a cost that can be increased
                        c.cost += 1;
                        c.costForTurn += 1; // Increase the cost for this turn as well
                        c.isCostModified = true; // Flag the card as having a modified cost
                        c.superFlash(Color.GOLD.cpy());
                    }
                }
                this.isDone = true; // Mark this action as complete
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // Makes the card cost 0 when upgraded
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrookedMile();
    }
}
