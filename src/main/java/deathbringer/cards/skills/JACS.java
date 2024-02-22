package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class JACS extends BaseCard {
    public static final String ID = makeID("JACS");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int POISON_GAINED = 3;
    private static final int STRENGTH_GAIN = 2;
    private static final int UPG_STRENGTH_GAIN = 1; // Increase by 1 for a total of 3

    public JACS() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, POISON_GAINED), POISON_GAINED));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRENGTH_GAIN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new JACS();
    }
}
