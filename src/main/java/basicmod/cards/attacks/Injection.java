package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import static basemod.BaseMod.logger;

public class Injection extends BaseCard {
    public static final String ID = makeID("Injection");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 2;

    private static final int POISON = 3;
    private static final int UPG_POISON = 2;

    public Injection() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = POISON;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dealDamageAndApplyPoison(p, m, damage, this.magicNumber);
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void dealDamageAndApplyPoison(AbstractPlayer p, AbstractMonster m, int dealDamage, int applyPoison) {
        logger.info("Entering dealDamageAndApplyPoison method");
        int initialHP = m.currentHealth;
        logger.info("Initial HP of monster: " + initialHP);

        if (m.hasPower("Vulnerable")) {
            dealDamage *= 1.5;
        }

        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                logger.info("Inside AbstractGameAction update method");
                if (m.currentHealth < initialHP) {
                    logger.info("Monster took damage, applying poison");
                    addToTop(new ApplyPowerAction(m, p, new PoisonPower(m, p, applyPoison), applyPoison));
                }
                this.isDone = true;
            }
        });
        addToTop(new DamageAction(m, new DamageInfo(p, dealDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void triggerFullEffect() {
        logger.info("Triggering Full Effect");
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    dealDamageAndApplyPoison(p, m, damage, magicNumber);
                }
                this.isDone = true;
            }
        });
    }

    public void triggerHalfEffect() {
        logger.info("Triggering Half Effect");
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    dealDamageAndApplyPoison(p, m, damage /2, magicNumber /2);
                }
                this.isDone = true;
            }
        });
    }

    public void triggerShadowplayEffect() {
        logger.info("Triggering Shadowplay Effect");
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    dealDamageAndApplyPoison(p, m, damage, magicNumber);
                }
                this.isDone = true;
            }
        });
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            upgradeMagicNumber(UPG_POISON);  // Upgrading magicNumber for poison
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Injection();
    }
}
