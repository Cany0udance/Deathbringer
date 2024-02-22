package deathbringer.cards.attacks;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChainedHits extends BaseCard {
    public static final String ID = makeID("ChainedHits");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 5;

    public ChainedHits() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.baseMagicNumber = deathbringer.Deathbringer.chainedHitsCounter;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        deathbringer.Deathbringer.chainedHitsCounter++;
        this.baseMagicNumber++;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChainedHits();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }

    public void resetChainedHitsMagicNumber() {
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }
}