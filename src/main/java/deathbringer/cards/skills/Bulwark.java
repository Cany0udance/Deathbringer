package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class Bulwark extends BaseCard {
    public static final String ID = makeID("Bulwark");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 7;

    public Bulwark() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numEnemies = 0; // Initialize to zero
        Iterator<AbstractMonster> var4 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var4.hasNext()) {
            AbstractMonster monster = var4.next();
            if (!monster.isDeadOrEscaped()) {
                numEnemies++;
                addToBot(new GainBlockAction(p, p, block));
            }
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new Bulwark();
    }
}
