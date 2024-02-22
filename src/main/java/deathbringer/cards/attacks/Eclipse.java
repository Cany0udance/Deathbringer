package deathbringer.cards.attacks;

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

    private static final int DAMAGE = 16;
    private static final int UPG_DAMAGE = 6;

    public Eclipse() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void triggerOnManualDiscard() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPlayer p = AbstractDungeon.player;
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (AbstractCard c : p.drawPile.group) {
                    temp.addToBottom(c);
                }

                p.drawPile.clear();

                for (AbstractCard c : p.discardPile.group) {
                    p.drawPile.addToBottom(c);
                }

                p.discardPile.clear();

                for (AbstractCard c : temp.group) {
                    p.discardPile.addToBottom(c);
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eclipse();
    }
}
