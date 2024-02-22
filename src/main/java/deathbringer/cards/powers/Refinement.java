package deathbringer.cards.powers;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.RefinementPower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Refinement extends BaseCard {
    public static final String ID = makeID("Refinement");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    public Refinement() {
        super(ID, info);
        this.baseMagicNumber = 2;  // Additional damage to Outburst
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RefinementPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);  // Increase the additional damage to 175 when upgraded
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Refinement();
    }
}
