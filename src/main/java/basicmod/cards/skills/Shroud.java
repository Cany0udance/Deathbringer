package basicmod.cards.skills;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import java.util.ArrayList;
import java.util.List;

public class Shroud extends BaseCard {
    public static final String ID = makeID("Shroud");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 2;

    private static final int SHROUDDAMALL = 4;
    private static final int UPG_SHROUDDAMALL = 2;
    private List<TooltipInfo> customTooltips = null;

    public Shroud() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);

        this.baseMagicNumber = SHROUDDAMALL;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, block / 2));
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(new CleaveEffect()));
        } else {
            this.addToBot(new VFXAction(p, new CleaveEffect(), 0.2F));
        }
        this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(magicNumber, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE, true));
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
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(SHROUDDAMALL - UPG_SHROUDDAMALL);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Shroud();
    }
}