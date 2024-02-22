package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Gauze extends BaseCard {
    public static final String ID = makeID("Gauze");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2 // Initial energy cost
    );

    private static final int BLOCK = 3;
    private static final int UPG_BLOCK = 4;  // Changed from 1 to 3 to reflect the upgraded amount
    private static final int HEAL_AMOUNT = 3;  // Amount of HP to be healed

    private static final int TIMES = 3;  // Number of times to gain Block

    public Gauze() {
        super(ID, info);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new GainBlockAction(p, p, this.block));
        }
        // Assume GainTempHpAction applies the temporary HP. Replace this with the actual class if different.
        addToBot(new AddTemporaryHPAction(p, p, HEAL_AMOUNT));    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);  // Changed this to properly upgrade the Block value
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Gauze();
    }
}

