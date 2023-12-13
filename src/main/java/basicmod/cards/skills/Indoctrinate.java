package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.StranglePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class Indoctrinate extends BaseCard {
    public static final String ID = makeID("Indoctrinate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -1 // X-cost
    );

    private static final int STRANGLE_MULTIPLIER = 6;
    private static final int UPG_STRANGLE_MULTIPLIER = 8;
    private static final int STRENGTH_MULTIPLIER = 2;

    public Indoctrinate() {
        super(ID, info);
        this.baseMagicNumber = STRANGLE_MULTIPLIER;
        this.magicNumber = baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean freeToPlayOnce = false;
        int energyOnUse = -1;

        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(monster, p, new StranglePower(monster, this.magicNumber * effect), this.magicNumber * effect));
                    addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, STRENGTH_MULTIPLIER * effect), STRENGTH_MULTIPLIER * effect));
                }
            }

            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRANGLE_MULTIPLIER - STRANGLE_MULTIPLIER);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Indoctrinate();
    }
}