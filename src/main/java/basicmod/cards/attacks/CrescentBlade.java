package basicmod.cards.attacks;

import basemod.helpers.CardModifierManager;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static basicmod.Deathbringer.shadowplaysThisCombat;

public class CrescentBlade extends BaseCard {
    public static final String ID = makeID("CrescentBlade");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            2  // Energy cost
    );

    private static final int BASE_DAMAGE = 6;
    private static final int ADDITIONAL_DAMAGE = 2; // Base additional damage for each Shadowplay
    private static final int UPGRADE_PLUS_ADDITIONAL_DAMAGE = 1; // Upgrade to 3 additional damage for each Shadowplay

    public CrescentBlade() {
        super(ID, info);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = ADDITIONAL_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalDamage = this.baseDamage + (basicmod.Deathbringer.shadowplaysThisCombat * this.magicNumber);
        this.calculateCardDamage(m); // Update the damage based on current shadowplays
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += basicmod.Deathbringer.shadowplaysThisCombat * this.magicNumber;
        super.applyPowers();
        this.baseDamage = originalBaseDamage; // Reset baseDamage to its original value
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += basicmod.Deathbringer.shadowplaysThisCombat * this.magicNumber;
        super.calculateCardDamage(mo);
        this.baseDamage = originalBaseDamage; // Reset baseDamage to its original value
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ADDITIONAL_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrescentBlade();
    }
}