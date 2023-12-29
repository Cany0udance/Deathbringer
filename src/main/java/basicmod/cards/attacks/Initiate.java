package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

public class Initiate extends BaseCard {
    public static final String ID = makeID("Initiate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1 // Energy cost
    );

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 11; // New constant for upgraded damage
    private static final int MULTIPLIER = 2; // Double damage
    private static final int UPG_MULTIPLIER = 3; // Triple damage when
    private static final int OUTBURST = 3;
    private static final int UPG_OUTBURST = 1; // Upgrade amount

    public Initiate() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(OUTBURST, UPG_OUTBURST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
      //  int finalDamage = this.damage;

        if (m.currentHealth == m.maxHealth) {
         //   finalDamage *= upgraded ? UPG_MULTIPLIER : MULTIPLIER;
            addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, magicNumber)));
        }

        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPG_DAMAGE - DAMAGE); // Update the damage when upgraded
            upgradeMagicNumber(UPG_OUTBURST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Initiate();
    }
}