package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Quench extends BaseCard {
    public static final String ID = makeID("Quench");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2  // Updating the cost to 2 as per your description
    );

    private static final int BASE_DAMAGE = 20;
    private static final int POTION_MULTIPLIER = 20; // Increase by 20 for each potion
    private static final int UPG_POTION_MULTIPLIER = 30; // Increase by 30 when upgraded


    public Quench() {
        super(ID, info);
        setDamage(BASE_DAMAGE);  // Use this instead
        this.baseMagicNumber = this.magicNumber = POTION_MULTIPLIER;
    }


    @Override
    public void applyPowers() {
        int potionsUsed = basicmod.Deathbringer.getPotionCount();
        this.baseDamage = BASE_DAMAGE + this.magicNumber * potionsUsed; // Use magicNumber
        super.applyPowers();
        this.isDamageModified = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_POTION_MULTIPLIER - POTION_MULTIPLIER); // Upgrade magicNumber
            this.rawDescription = "Deal !D! damage. NL Increase the damage dealt by !M! for each potion drank this combat.";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Quench();
    }
}