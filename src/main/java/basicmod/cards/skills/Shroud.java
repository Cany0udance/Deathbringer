package basicmod.cards.skills;

import basicmod.actions.ShroudAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Shroud extends BaseCard {
    public static final String ID = makeID("Shroud");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 2;

    public Shroud() {
        super(ID, info);

        setBlock(BLOCK, UPG_BLOCK);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ShroudAction(this, 2));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ShroudAction(this, 2));
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, block / 2));
        addToBot(new ShroudAction(this, 1));
    }


    @Override
    public AbstractCard makeCopy() {
        return new Shroud();
    }
}