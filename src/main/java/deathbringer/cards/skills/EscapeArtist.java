package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.EscapeArtistPower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EscapeArtist extends BaseCard {
    public static final String ID = makeID("EscapeArtist");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public EscapeArtist() {
        super(ID, info, true);
        this.baseMagicNumber = 3;  // Duration of the debuff
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EscapeArtistPower(p, this.magicNumber), this.magicNumber));
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EscapeArtist();
    }
}
