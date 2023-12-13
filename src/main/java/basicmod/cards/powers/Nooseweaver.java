package basicmod.cards.powers;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.NooseweaverPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Nooseweaver extends BaseCard {
    public static final String ID = makeID("Nooseweaver");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // Initial Energy cost
    );

    private static final int UPG_COST = 1; // The cost becomes 1 when upgraded

    public Nooseweaver() {
        super(ID, info);
        this.cost = info.baseCost;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new NooseweaverPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Nooseweaver();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }
}