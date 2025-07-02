package deathbringer.cards.attacks;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class FinishTheJob extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("FinishTheJob");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1
    );
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DMG = 4;
    private List<TooltipInfo> customTooltips = null;

    public FinishTheJob() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        this.tags.add(Deathbringer.Enums.UMBRA);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowattack.png",
                "deathbringer/images/character/cardback/shadowattack_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    @Override
    public void triggerHalfEffect() {
        int[] halfDamage = new int[this.multiDamage.length];
        for (int i = 0; i < halfDamage.length; i++) {
            halfDamage[i] = this.multiDamage[i] / 2;
        }
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, halfDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void triggerShadowplayEffect() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c != this) {
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
            }
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
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FinishTheJob();
    }
}