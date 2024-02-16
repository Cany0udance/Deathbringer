package basicmod.cards.skills;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.effects.NightlightEffect;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class Nightlight extends BaseCard {
    public static final String ID = makeID("Nightlight");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int TEMP_DEX = 2;  // Temporary Dexterity
    private static final int HALF_TEMP_DEX = 1; // Half Temporary Dexterity for Shadowplay
    private static final int WEAK_AMOUNT = 1;  // Apply 1 Weak
    private static final int UPG_WEAK_AMOUNT = 1;  // Upgrade Weak amount
    private static boolean vfxPlayed = false;
    private List<TooltipInfo> customTooltips = null;

    public Nightlight() {
        super(ID, info);
        setMagic(WEAK_AMOUNT);  // Set the Weak effect values
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        this.tags.add(Deathbringer.Enums.UMBRA);
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyTemporaryDexterity(p, TEMP_DEX);  // Gain Temporary Dexterity
        ShadowUtility.triggerGeneralShadowEffect(this);  // Trigger Shadow effect
    }

    private void applyTemporaryDexterity(AbstractPlayer p, int amount) {
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, amount), amount));
        addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, amount), amount));
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        applyTemporaryDexterity(p, HALF_TEMP_DEX);  // Gain half the Temporary Dexterity
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        // Play VFX if not played
        if (!vfxPlayed) {
            this.addToBot(new VFXAction(new NightlightEffect(), 2.0F));
            vfxPlayed = true;
        }
        // Apply Weak to ALL enemies
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false), this.magicNumber));
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
            upgradeMagicNumber(UPG_WEAK_AMOUNT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Nightlight();
    }

    public static void resetNightlightVFX() {
        vfxPlayed = false;
    }
}
