package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class SufferWithMe extends BaseCard {
    public static final String ID = makeID("SufferWithMe");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
    CardType.SKILL,
    CardRarity.COMMON,
    CardTarget.ENEMY,
            1  // Energy cost
            );

    private static final int POISON_APPLIED = 6;

    public SufferWithMe() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = POISON_APPLIED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, 1), 1));
        if (!upgraded) {
            // Apply Poison to a single target
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
        } else {
            // Apply Poison to all enemies
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(mo, p, new PoisonPower(mo, p, this.magicNumber), this.magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.target = CardTarget.ALL_ENEMY;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SufferWithMe();
    }
}
