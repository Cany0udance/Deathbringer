package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Cobra extends BaseCard {
    public static final String ID = makeID("Cobra");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // Energy cost
    );

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 2;

    private static final int MULTIPLIER = 3;
    private static final int UPG_MULTIPLIER = 1;

    public Cobra() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MULTIPLIER, UPG_MULTIPLIER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        int strangleStacks = 0;

        // Check if the monster has Strangle power
        AbstractPower stranglePower = m.getPower("Deathbringer:Strangle"); // Replace with the actual ID of your Strangle power

        if (stranglePower != null) {
            strangleStacks = stranglePower.amount;

            // Remove all stacks of Strangle
            addToBot(new RemoveSpecificPowerAction(m, p, "Deathbringer:Strangle")); // Replace with the actual ID of your Strangle power

            // Apply Poison with quadruple or quintuple stacks
            int poisonStacks = strangleStacks * magicNumber;
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poisonStacks), poisonStacks));
        }

        this.exhaust = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cobra();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            upgradeMagicNumber(UPG_MULTIPLIER);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
