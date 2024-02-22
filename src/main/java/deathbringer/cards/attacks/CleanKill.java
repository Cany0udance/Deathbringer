package deathbringer.cards.attacks;

import deathbringer.actions.CleanKillAction;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static deathbringer.Deathbringer.CLEANKILL_KEY;

public class CleanKill extends BaseCard {
    public static final String ID = makeID("CleanKill");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // Energy cost
    );

    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 15;

    public CleanKill() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CleanKillAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        CardCrawlGame.sound.play(CLEANKILL_KEY);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CleanKill();
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
