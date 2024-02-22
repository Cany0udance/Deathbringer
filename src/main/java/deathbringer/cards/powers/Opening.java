package deathbringer.cards.powers;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.OpeningPower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Opening extends BaseCard {
    public static final String ID = makeID("Opening");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int VIGOR_AMOUNT = 6; // Static amount, no longer a magic number
    private static final int STRENGTH_AMOUNT = 1;
    private static final int UPG_STRENGTH_AMOUNT = 2;

    public Opening() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = STRENGTH_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new OpeningPower(p, this.magicNumber, VIGOR_AMOUNT), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRENGTH_AMOUNT - STRENGTH_AMOUNT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Opening();
    }
}
