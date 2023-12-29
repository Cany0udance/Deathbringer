package basicmod.cards.skills;

import basicmod.actions.UpgradeBulwarkAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static basicmod.cards.FlavorConstants.FLAVOR_BOX_COLOR;
import static basicmod.cards.FlavorConstants.FLAVOR_TEXT_COLOR;

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
        if (!upgraded) {
            FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
            FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            removeFlavorText();
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

        // Check for the Progressive condition only if the card is unupgraded
        if (!this.upgraded) {
            if (numEnemies >= 4) {
                addToBot(new UpgradeBulwarkAction(this));
            }
        }
    }

    private void removeFlavorText() {
        // Implement logic to remove or hide the flavor text
        // For example, set it to an empty string or null
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, null);
        FlavorText.AbstractCardFlavorFields.textColor.set(this, null);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bulwark();
    }
}
