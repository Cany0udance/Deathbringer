package basicmod.cards.attacks;

import basicmod.actions.OnslaughtAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class Onslaught extends BaseCard {
    public static final String ID = makeID("Onslaught");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2 // Energy cost
    );

    private static final int DAMAGE = 18;
    private static final int UPG_DAMAGE = 22;
    private static final int HP_LOSS = 4;

    public Onslaught() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new OnslaughtAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), HP_LOSS, 1));
    }


    @Override
    public AbstractCard makeCopy() {
        return new Onslaught();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }
}
