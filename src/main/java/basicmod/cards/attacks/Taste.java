package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.VirulencePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Taste extends BaseCard {
    public static final String ID = makeID("Taste");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int POISON_GAINED = 1;
    private static final int VIRULENCE_GAINED = 2;
    private static final int UPGRADE_PLUS_VIRULENCE = 2; // Gain an additional Virulence when upgraded

    public Taste() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = VIRULENCE_GAINED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, POISON_GAINED), POISON_GAINED));
        addToBot(new ApplyPowerAction(p, p, new VirulencePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VIRULENCE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Taste();
    }
}

