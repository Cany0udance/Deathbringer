/*

package basicmod.cards.powers;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.ThrivePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Thrive extends BaseCard {
    public static final String ID = makeID("Thrive");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int HEAL_AMOUNT = 5;
    private static final int UPG_HEAL_AMOUNT = 8;

    public Thrive() {
        super(ID, info);
        setMagic(HEAL_AMOUNT, UPG_HEAL_AMOUNT);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ThrivePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_HEAL_AMOUNT - HEAL_AMOUNT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Thrive();
    }
}

*/