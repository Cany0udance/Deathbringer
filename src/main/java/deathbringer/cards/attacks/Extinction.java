package deathbringer.cards.attacks;

import deathbringer.actions.ExtinctionAction;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Extinction extends BaseCard {
    public static final String ID = makeID("Extinction");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 5;

    public Extinction() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExtinctionAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        this.exhaust = true;
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPG_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Extinction();
    }
}
