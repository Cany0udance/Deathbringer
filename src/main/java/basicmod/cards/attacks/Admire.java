package basicmod.cards.attacks;

import basemod.helpers.TooltipInfo;
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

import java.util.ArrayList;
import java.util.List;

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
    private List<TooltipInfo> customTooltips = null;

    public Admire() {
        super(ID, info);
        setDamage(DAMAGE);
        this.magicNumber = this.baseMagicNumber = PLATED_ARMOR;
        this.tags.add(Deathbringer.Enums.SHADOW);
        setBackgroundTexture("basicmod/images/character/cardback/shadowattack.png", "basicmod/images/character/cardback/shadowattack_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
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
        ShadowUtility.triggerGeneralShadowEffect(this);
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
    public List<TooltipInfo> getCustomTooltips() {
        if (this.customTooltips == null) {
            this.customTooltips = new ArrayList<>();
            TooltipInfo shadowTooltip = new TooltipInfo("Shadow", "#yShadow #ycards #yare #ydesignated #yby #ya #ydifferent #ycard #ybackground #yand #yenergy #yicon. NL NL Whenever you play a Shadow card, another random Shadow card in your hand has its actions triggered at half effectiveness, then is discarded.");
            this.customTooltips.add(shadowTooltip);
        }
        return this.customTooltips;
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