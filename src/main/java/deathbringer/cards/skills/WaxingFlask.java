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
import com.megacrit.cardcrawl.powers.PoisonPower;

import java.util.ArrayList;
import java.util.List;

public class WaxingFlask extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("WaxingFlask");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            3
    );
    private static final int BLOCK = 10;
    private static final int POISON_AMOUNT = 10;
    private List<TooltipInfo> customTooltips = null;

    public WaxingFlask() {
        super(ID, info);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = POISON_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));
        if (upgraded) {
            ShadowUtility.triggerGeneralShadowEffect(this);
        }
    }

    @Override
    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        int halfBlock = this.block / 2;
        addToBot(new GainBlockAction(p, p, halfBlock));
        addToBot(new GainBlockAction(p, p, halfBlock));

        AbstractMonster randomTarget = AbstractDungeon.getRandomMonster();
        if (randomTarget != null) {
            int halfPoison = this.magicNumber / 2;
            addToBot(new ApplyPowerAction(randomTarget, p, new PoisonPower(randomTarget, p, halfPoison), halfPoison));
            addToBot(new ApplyPowerAction(randomTarget, p, new PoisonPower(randomTarget, p, halfPoison), halfPoison));
        }
    }

    @Override
    public void triggerShadowplayEffect() {
        // No shadowplay effect for this card
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (this.upgraded && this.customTooltips == null) {
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
            setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                    "deathbringer/images/character/cardback/shadowskill_p.png");
            setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                    "deathbringer/images/character/cardback/shadowenergyorb_p.png");
            this.tags.add(Deathbringer.Enums.SHADOW);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WaxingFlask();
    }
}