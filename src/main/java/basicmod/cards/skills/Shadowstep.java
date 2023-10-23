package basicmod.cards.skills;

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
import static basemod.BaseMod.logger;

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

    public Shadowstep() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);

        // Add "Shadow" keyword to this card
        this.keywords.add("shadow");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        logger.info("Using Shadowstep.");
        // Gain block
        addToBot(new GainBlockAction(p, p, block));

        // Trigger Shadow Effect
        ShadowUtility.triggerGeneralShadowEffect(this);

        // Draw cards
        addToBot(new DrawCardAction(p, DRAW));

    }

    public void triggerFullEffect() {
        // Halve the effectiveness of this card when it's a Shadow card
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
        addToBot(new DrawCardAction(AbstractDungeon.player, DRAW ));
    }

    public void triggerHalfEffect() {
        // Halve the effectiveness of this card when it's a Shadow card
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block / 2));
        addToBot(new DrawCardAction(AbstractDungeon.player, DRAW / 2));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Shadowstep();
    }
}
