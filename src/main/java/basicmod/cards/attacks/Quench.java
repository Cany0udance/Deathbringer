package basicmod.cards.attacks;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Quench extends BaseCard {
    public static final String ID = makeID("Quench");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2  // Energy cost
    );
    private List<TooltipInfo> customTooltips = null;

    public Quench() {
        super(ID, info);
        this.tags.add(CardTags.HEALING);
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        this.tags.add(Deathbringer.Enums.UMBRA);
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ShadowUtility.triggerGeneralShadowEffect(this); // Trigger Shadow effect if applicable
    }

    public void triggerHalfEffect() {
        // No effect when played normally or for half effect
    }

    public void triggerShadowplayEffect() {
        // Gain a random potion
        AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        // Exhaust only when Shadowplayed
        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
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
            upgradeBaseCost(0); // Reduce cost to 0 when upgraded
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Quench();
    }
}
