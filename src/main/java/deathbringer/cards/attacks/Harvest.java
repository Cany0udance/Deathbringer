package deathbringer.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import deathbringer.actions.HarvestAction;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Harvest extends BaseCard {
    public static final String ID = makeID("Harvest");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2 // Energy cost
    );
    private static final int DAMAGE = 15;
    private static final int UPG_DAMAGE = 5;
    private static final int DRAW = 3;

    public Harvest() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.isDying || m.currentHealth <= 0) {
                    addToTop(new DrawCardAction(magicNumber));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Harvest();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            initializeDescription();
        }
    }
}
