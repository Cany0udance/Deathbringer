package basicmod.cards.skills;

import basemod.helpers.TooltipInfo;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Shadowstep extends BaseCard {
    public static final String ID = makeID("Shadowstep");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;
    private static final int DRAW = 2;
    private List<TooltipInfo> customTooltips = null;

    public Shadowstep() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        this.tags.add(Deathbringer.Enums.SHADOW);
        setBackgroundTexture("basicmod/images/character/cardback/shadowskill.png", "basicmod/images/character/cardback/shadowskill_p.png");
        setOrbTexture("basicmod/images/character/cardback/shadowenergyorb.png", "basicmod/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        addToBot(new GainBlockAction(p, p, block));

        // Trigger Shadow Effect
        ShadowUtility.triggerGeneralShadowEffect(this);

        // Draw cards
        addToBot(new DrawCardAction(p, DRAW));

    }

    public void triggerHalfEffect() {
        // Halve the effectiveness of this card when it's a Shadow card
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block / 2));
        addToBot(new DrawCardAction(AbstractDungeon.player, DRAW / 2));
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
    public AbstractCard makeCopy() {
        return new Shadowstep();
    }
}
