package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Humiliate extends BaseCard {
    public static final String ID = makeID("Humiliate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY, // Changed to ALL_ENEMY
            1 // Initial energy cost
    );

    public Humiliate() {
        super(ID, info);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int playerStrength = 0;

        // Get player's current Strength
        if (p.hasPower("Strength")) {
            playerStrength = p.getPower("Strength").amount;
        }

        // Strength multiplier when card is upgraded
        int strengthMultiplier = upgraded ? 2 : 1;

        if (playerStrength != 0) {
            // Remove all Strength from the player
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -playerStrength), -playerStrength));

            // Loop through all monsters in combat
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                // Reduce strength from the enemy (twice as much if upgraded)
                addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, -playerStrength * strengthMultiplier), -playerStrength * strengthMultiplier));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Humiliate();
    }
}

