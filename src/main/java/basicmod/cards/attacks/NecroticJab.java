package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.NecrosisPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NecroticJab extends BaseCard {
    public static final String ID = makeID("NecroticJab");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );

    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int POISON_PER_TURN = 1;
    private static final int UPGRADE_PLUS_POISON = 1;

    public NecroticJab() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = POISON_PER_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage twice
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));

        // Apply NecrosisPower
        addToBot(new ApplyPowerAction(m, p, new NecrosisPower(m, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NecroticJab();
    }
}
