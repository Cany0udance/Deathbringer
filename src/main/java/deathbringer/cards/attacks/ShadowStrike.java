package deathbringer.cards.attacks;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class ShadowStrike extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("ShadowStrike");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;
    private static final int WEAK = 1;
    private static final int UPG_WEAK = 1;

    private List<TooltipInfo> customTooltips = null;

    public ShadowStrike() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = WEAK;
        this.tags.add(CardTags.STRIKE);
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowattack.png",
                "deathbringer/images/character/cardback/shadowattack_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    @Override
    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (m != null) {
            int shadowDamage = ShadowUtility.calculateShadowDamage(this.damage, m);
            addToBot(new DamageAction(m, new DamageInfo(p, shadowDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (m != null) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
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
            upgradeDamage(UPG_DAMAGE);
            upgradeMagicNumber(UPG_WEAK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowStrike();
    }
}