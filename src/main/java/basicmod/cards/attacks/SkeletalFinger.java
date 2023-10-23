package basicmod.cards.attacks;

import basicmod.actions.SkeletalFingerAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SkeletalFinger extends BaseCard {
    public static final String ID = makeID("SkeletalFinger");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            0 // Energy cost
    );

    private static final int DAMAGE = 1;
    private static final int UPG_DAMAGE = 0;

    public SkeletalFinger() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SkeletalFingerAction(m, p, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), upgraded));
    }


    @Override
    public AbstractCard makeCopy() {
        return new SkeletalFinger();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
