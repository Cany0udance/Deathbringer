package deathbringer.cards.skills;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.ArrayList;
import java.util.List;

public class SubconsciousKiller extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("SubconsciousKiller");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2
    );
    private static final int STRENGTH_GAIN = 2;
    private static final int UPG_STRENGTH_GAIN = 3;
    private List<TooltipInfo> customTooltips = null;

    public SubconsciousKiller() {
        super(ID, info);
        this.cost = -2;
        this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                "deathbringer/images/character/cardback/shadowskill_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerHalfEffect() {
    }

    @Override
    public void triggerShadowplayEffect() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new VigorPower(AbstractDungeon.player, 5), 5));
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
            upgradeMagicNumber(UPG_STRENGTH_GAIN - STRENGTH_GAIN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SubconsciousKiller();
    }
}