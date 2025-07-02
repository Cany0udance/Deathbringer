package deathbringer.cards.attacks;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.ArrayList;
import java.util.List;

public class NothingPersonal extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("NothingPersonal");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int DAMAGE = 25;
    private static final int UPGRADE_PLUS_DAMAGE = 5;
    private List<TooltipInfo> customTooltips = null;

    public NothingPersonal() {
        super(ID, info);
        setDamage(DAMAGE);
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        this.tags.add(Deathbringer.Enums.PENUMBRA);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                "deathbringer/images/character/cardback/shadowskill_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    @Override
    public void triggerHalfEffect() {
    }

    @Override
    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster randomTarget = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (randomTarget != null) {
            int calculatedDamage = damage;
            if (randomTarget.hasPower("Vulnerable")) {
                calculatedDamage *= 1.5;
            }
            addToBot(new VFXAction(new VerticalImpactEffect(randomTarget.hb.cX + randomTarget.hb.width / 4.0F,
                    randomTarget.hb.cY - randomTarget.hb.height / 4.0F)));
            addToBot(new DamageAction(randomTarget, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (this.customTooltips == null) {
            this.customTooltips = new ArrayList<>();
            TooltipInfo shadowTooltip = new TooltipInfo("Shadow",
                    "#yShadow #ycards #yare #ydesignated #yby #ya #ydifferent #ycard #ybackground #yand #yenergy #yicon. NL NL " +
                            "Whenever you play a Shadow card, another random Shadow card in your hand has its actions triggered at half effectiveness, then is discarded.");
            this.customTooltips.add(shadowTooltip);
        }
        return this.customTooltips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NothingPersonal();
    }
}