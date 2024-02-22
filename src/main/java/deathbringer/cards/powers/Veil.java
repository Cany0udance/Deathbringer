package deathbringer.cards.powers;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.VeilPower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Veil extends BaseCard {
    public static final String ID = makeID("Veil");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2  // Energy cost
    );

    public Veil() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VeilPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);  // Reduce the cost to 2 when upgraded
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Veil();
    }
}
