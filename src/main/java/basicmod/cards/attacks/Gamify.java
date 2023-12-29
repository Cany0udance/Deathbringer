package basicmod.cards.attacks;

import basicmod.actions.UpgradeBulwarkAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static basemod.BaseMod.logger;

import java.util.ArrayList;

public class Gamify extends BaseCard {
    public static final String ID = makeID("Gamify");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL, // Changed from ATTACK to SKILL
            CardRarity.COMMON,
            CardTarget.SELF, // Changed to SELF as it benefits the player
            1
    );

    private static final int TEMP_VIGOR = 3;
    private static final int UPG_TEMP_VIGOR = 4;

    public Gamify() {
        super(ID, info);
        magicNumber = baseMagicNumber = TEMP_VIGOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numEnemies = 0;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                numEnemies++;
            }
        }

        int totalVigor = this.magicNumber * numEnemies; // Calculate total Vigor

        // Apply the Vigor to the player
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, totalVigor), totalVigor));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Gamify();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_TEMP_VIGOR - TEMP_VIGOR);
            initializeDescription();
        }
    }
}
