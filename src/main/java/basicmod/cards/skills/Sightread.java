package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Sightread extends BaseCard {
    public static final String ID = makeID("Sightread");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY, // Target is an enemy
            1
    );

    private static final int STR_DEX = 2;
    private static final int UPG_STR_DEX = 1;

    public Sightread() {
        super(ID, info);
        setMagic(STR_DEX, UPG_STR_DEX);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.currentHealth >= p.currentHealth) {
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
        } else {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        }
        this.exhaust = true; // The card will exhaust after use
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPG_STR_DEX);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sightread();
    }
}
