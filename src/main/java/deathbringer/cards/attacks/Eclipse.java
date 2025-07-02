package deathbringer.cards.attacks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.PoisonPower;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Eclipse extends BaseCard {
    public static final String ID = makeID("Eclipse");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );
    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 15;
    private static final int POISON = 1;

    public Eclipse() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.magicNumber = this.baseMagicNumber = POISON;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply 1 Poison twice
        for (int i = 0; i < 2; i++) {
            addToBot(new ApplyPowerAction(p, p, new PoisonPower(p, p, magicNumber), magicNumber));
        }

        // Deal damage twice
        for (int i = 0; i < 2; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eclipse();
    }
}
