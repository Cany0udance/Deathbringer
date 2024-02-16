package basicmod.cards.attacks;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

public class ConcealedBlade extends BaseCard {
    public static final String ID = makeID("ConcealedBlade");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0
    );

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 3;
    private List<TooltipInfo> customTooltips = null;

    public ConcealedBlade() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("basicmod/images/character/cardback/shadowattack.png", "basicmod/images/character/cardback/shadowattack_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    int calculatedDamage = damage;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
                    int calculatedDamage = damage / 2;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                this.isDone = true;
            }
        });
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, 1), 1));
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
            upgradeDamage(UPG_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ConcealedBlade();
    }
}
