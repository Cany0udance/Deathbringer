package deathbringer.cards.skills;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.List;

public class ShadowDefend extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("ShadowDefend");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 3;
    private static final int VULNERABLE = 1;
    private static final int UPG_VULNERABLE = 1;
    private List<TooltipInfo> customTooltips = null;

    public ShadowDefend() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        this.magicNumber = this.baseMagicNumber = VULNERABLE;
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                "deathbringer/images/character/cardback/shadowskill_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    @Override
    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, this.block / 2));
    }

    @Override
    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (m != null) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
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
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(UPG_VULNERABLE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowDefend();
    }
}