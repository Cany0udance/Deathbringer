package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Mantle extends BaseCard {
    public static final String ID = makeID("Mantle");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            2  // Energy cost
    );

    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 4;  // Upgrades to 12

    private static final int ENERGY_GAIN = 1;
    private static final int UPG_ENERGY_GAIN = 0;  // No upgrade for Energy

    public Mantle() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        this.magicNumber = this.baseMagicNumber = ENERGY_GAIN;  // Energy gain set
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));

        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    public void triggerHalfEffect() {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block / 2));
    }

    public void triggerShadowplayEffect() {
        addToBot(new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Mantle();
    }
}
