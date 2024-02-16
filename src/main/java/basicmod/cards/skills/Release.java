package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Release extends BaseCard {
    public static final String ID = makeID("Release");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int POISON_AMOUNT = 5;
    private static final int UPGRADE_PLUS_POISON = 2;  // Increase Poison amount by 2 when upgraded

    public Release() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = POISON_AMOUNT;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply Poison to the player
        addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, this.magicNumber), this.magicNumber));

        // Transfer the player's Poison to all enemies and remove it from the player
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p.hasPower(PoisonPower.POWER_ID)) {
                    int poisonAmount = p.getPower(PoisonPower.POWER_ID).amount;
                    // Remove Poison from the player
                    addToBot(new RemoveSpecificPowerAction(p, p, PoisonPower.POWER_ID));

                    // Apply the same amount of Poison to all enemies
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        addToBot(new ApplyPowerAction(mo, p, new PoisonPower(mo, p, poisonAmount), poisonAmount));
                    }
                }
                this.isDone = true;
            }
        });
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
        return new Release();
    }
}
