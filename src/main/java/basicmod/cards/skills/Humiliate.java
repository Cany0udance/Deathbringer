package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Humiliate extends BaseCard {
    public static final String ID = makeID("Humiliate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1  // Energy cost
    );

    private static final int STRENGTH_LOSS = 1; // Base Strength loss
    private static final int UPGRADE_PLUS_STRENGTH_LOSS = 1; // Upgrade to 2 Strength loss

    public Humiliate() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = STRENGTH_LOSS;
        this.exhaust = true; // Card exhausts after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Strength loss to all enemies and gain Strength for each enemy affected
        int enemiesAffected = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -this.magicNumber), -this.magicNumber));
                enemiesAffected++;
            }
        }

        // Gain Strength for each enemy affected
        if (enemiesAffected > 0) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, enemiesAffected), enemiesAffected));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH_LOSS);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Humiliate();
    }
}