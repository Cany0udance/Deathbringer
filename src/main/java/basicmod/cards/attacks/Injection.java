package basicmod.cards.attacks;

import basemod.helpers.TooltipInfo;
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

import java.util.ArrayList;
import java.util.List;

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
    private List<TooltipInfo> customTooltips = null;

    public Injection() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = POISON;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("basicmod/images/character/cardback/shadowattack.png", "basicmod/images/character/cardback/shadowattack_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dealDamageAndApplyPoison(p, m, damage, this.magicNumber);
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void dealDamageAndApplyPoison(AbstractPlayer p, AbstractMonster m, int dealDamage, int applyPoison) {

        // Check for Vulnerable power and increase damage if present
        if (m.hasPower("Vulnerable")) {
            dealDamage *= 1.5;
        }

        // Damage action
        addToBot(new DamageAction(m, new DamageInfo(p, dealDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

        // Apply poison action
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, applyPoison), applyPoison));
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (m != null) {
            dealDamageAndApplyPoison(p, m, damage / 2, magicNumber / 2);
        }
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (m != null) {
            dealDamageAndApplyPoison(p, m, damage, magicNumber);
        }
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
            upgradeMagicNumber(UPG_POISON);  // Upgrading magicNumber for poison
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Injection();
    }
}
