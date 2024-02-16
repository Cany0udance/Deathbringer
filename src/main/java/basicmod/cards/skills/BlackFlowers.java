package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.effects.BlackFlowersEffect;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class BlackFlowers extends BaseCard {
    public static final String ID = makeID("BlackFlowers");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -1  // Energy cost for X-cost cards
    );

    private static final int POISON_AMOUNT = 7;
    private static final int UPGRADE_PLUS_POISON = 3;  // Increase Poison amount by 3 when upgraded

    public BlackFlowers() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = POISON_AMOUNT;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;

        // Check if the card is free to play (like from a relic or effect)
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        // Apply Poison X times
        if (effect > 0) {
            // Play VFX if effect is at least 4
            if (effect >= 4) {
                addToBot(new VFXAction(new BlackFlowersEffect(), 1.0F));
            }

            // Apply Poison X times
            for (int i = 0; i < effect; i++) {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new ApplyPowerAction(mo, p, new PoisonPower(mo, p, this.magicNumber), this.magicNumber));
                }
            }
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackFlowers();
    }
}
