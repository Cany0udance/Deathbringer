package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.VirulencePower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class GiftFromBelow extends BaseCard {
    public static final String ID = makeID("GiftFromBelow");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            3  // Energy cost
    );

    private static final int GAIN = 1;
    private static final int UPG_GAIN = 1; // Increase by 1 for a total of 3

    public GiftFromBelow() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = GAIN;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new VirulencePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_GAIN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GiftFromBelow();
    }
}