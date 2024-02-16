package basicmod.cards.attacks;

import basicmod.actions.DiscardShadowCardsAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
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

    private static final int DAMAGE_PER_CARD = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    public SkeletalFinger() {
        super(ID, info);
        this.baseDamage = DAMAGE_PER_CARD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardShadowCardsAction(p, m, this.damage));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SkeletalFinger();
    }
}
