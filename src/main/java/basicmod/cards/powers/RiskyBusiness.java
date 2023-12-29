package basicmod.cards.powers;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.RiskyBusinessPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RiskyBusiness extends BaseCard {
    public static final String ID = makeID("RiskyBusiness");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int DRAW_AMOUNT = 1;

    public RiskyBusiness() {
        super(ID, info);
        setMagic(DRAW_AMOUNT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RiskyBusinessPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RiskyBusiness();
    }
}
