package deathbringer.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import deathbringer.actions.DiscardShadowCardsAction;
import deathbringer.actions.ReturnRandomShadowCardsAction;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SkeletalFinger extends BaseCard {
    public static final String ID = makeID("SkeletalFinger");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );
    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int SHADOW_CARDS = 1;
    private static final int UPGRADE_PLUS_SHADOW_CARDS = 1;

    public SkeletalFinger() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = SHADOW_CARDS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ReturnRandomShadowCardsAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_SHADOW_CARDS);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SkeletalFinger();
    }
}