package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Discretion extends BaseCard {
    public static final String ID = makeID("Discretion");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );

    private static final int POISON = 12;
    private static final int UPG_POISON = 3;

    public Discretion() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = POISON;
        this.exhaust = true;  // Card will be exhausted when played
        this.isInnate = true; // Card is Innate
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_POISON);  // Upgrading magicNumber for poison
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Discretion();
    }
}
