package basicmod.cards.attacks;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.List;

public class Seethe extends BaseCard {
    public static final String ID = makeID("Seethe");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1  // Energy cost
    );

    private static final int VULNERABLE_AMOUNT = 2;  // Apply 2 Vulnerable
    private static final int STRENGTH_GAIN = 1;  // Base Strength gain for Shadowplay
    private static final int UPGRADE_PLUS_STRENGTH_GAIN = 1;  // Upgrade to 2 Strength gain
    private List<TooltipInfo> customTooltips = null;

    public Seethe() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = STRENGTH_GAIN;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Vulnerable to ALL enemies
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, VULNERABLE_AMOUNT, false), VULNERABLE_AMOUNT));
        }

        ShadowUtility.triggerGeneralShadowEffect(this);  // Trigger Shadow effect
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, VULNERABLE_AMOUNT / 2, false), VULNERABLE_AMOUNT / 2));
        }
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        // Gain Strength on Shadowplay
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
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
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH_GAIN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Seethe();
    }
}
