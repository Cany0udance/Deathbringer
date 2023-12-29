package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class Admire extends BaseCard {
    public static final String ID = makeID("Admire");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 6;
    private static final int PLATED_ARMOR = 4;
    private static final int UPG_PLATED_ARMOR = 2;

    private static final int DRAW = 1;

    public Admire() {
        super(ID, info);
        setDamage(DAMAGE);
        this.magicNumber = this.baseMagicNumber = PLATED_ARMOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int initialHP = m.currentHealth;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.currentHealth == initialHP) {
                    addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber), magicNumber));
                    addToBot(new DrawCardAction(p, DRAW));
                }
                this.isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ShadowUtility.triggerGeneralShadowEffect(Admire.this);
                this.isDone = true;
            }
        });
    }

    public void triggerFullEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    int initialHP = m.currentHealth;
                    int calculatedDamage = damage;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            if (m.currentHealth == initialHP) {
                                addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber), magicNumber));
                                addToBot(new DrawCardAction(p, DRAW));
                            }
                            this.isDone = true;
                        }
                    });
                }
                this.isDone = true;
            }
        });
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    int initialHP = m.currentHealth;
                    int calculatedDamage = damage / 2;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            if (m.currentHealth == initialHP) {
                                addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber / 2), magicNumber / 2));
                            }
                            this.isDone = true;
                        }
                    });
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_PLATED_ARMOR);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Admire();
    }
}