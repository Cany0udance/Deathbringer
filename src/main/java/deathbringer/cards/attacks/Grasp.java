package deathbringer.cards.attacks;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Grasp extends BaseCard {
    public static final String ID = makeID("Grasp");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 9;
    private static final int UPG_DAMAGE = 12;

    private static final int POISON = 4;
    private static final int UPG_POISON = 5;

    public Grasp() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = POISON;  // Set the magic number
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                float currentHealthPercent = ((float) m.currentHealth / m.maxHealth) * 100;
                if (currentHealthPercent <= 50) {
                    addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));  // Use the magic number
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Grasp();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_POISON - POISON);  // Upgrade the magic number
            initializeDescription();
        }
    }
}
