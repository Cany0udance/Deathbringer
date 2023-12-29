package basicmod.cards.skills;

import basicmod.actions.UpgradeHideAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

import static basicmod.cards.FlavorConstants.FLAVOR_BOX_COLOR;
import static basicmod.cards.FlavorConstants.FLAVOR_TEXT_COLOR;

public class Hide extends BaseCard {
    public static final String ID = makeID("Hide");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            0  // Energy cost
    );

    private static final int BLOCK_PER_SHADOW = 7;
    private static final int UPG_BLOCK_PER_SHADOW = 8;

    public Hide() {
        super(ID, info);
        setBlock(BLOCK_PER_SHADOW, UPG_BLOCK_PER_SHADOW);
        if (!upgraded) {
            FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
            FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int shadowCards = 0;
        List<String> EXCEPTION_CARD_IDS = Arrays.asList("Deathbringer:Imprudence", "Deathbringer:Hide", "Deathbringer:UnknownDepths", "Deathbringer:Trade", "Deathbringer:Arsenal");

        for (AbstractCard c : p.hand.group) {
            if (c.keywords.contains("deathbringer:shadow") && !EXCEPTION_CARD_IDS.contains(c.cardID)) {
                shadowCards++;
            }
        }

        // Applying block one chunk at a time
        for (int i = 0; i < shadowCards; i++) {
            addToBot(new GainBlockAction(p, p, this.block));
        }

        // Checking for the Progressive condition
        if (!this.upgraded && shadowCards >= 3) {
            addToBot(new UpgradeHideAction(this));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true;  // Retain this card when upgraded
            upgradeBlock(UPG_BLOCK_PER_SHADOW - BLOCK_PER_SHADOW);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            removeFlavorText();
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
        return new Hide();
    }
}
