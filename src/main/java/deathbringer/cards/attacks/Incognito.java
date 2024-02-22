package deathbringer.cards.attacks;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Incognito extends BaseCard {
    public static final String ID = makeID("Incognito");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );

    private static final int DAMAGE = 17;
    private static final int UPGRADE_PLUS_DMG = 6;  // Upgrades to 23 damage

    public Incognito() {
        super(ID, info);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        if (!super.canUse(p, m)) {
            return false;
        }

        // Check if every other card in hand is a Shadow card
        for (AbstractCard c : p.hand.group) {
            if (c != this && !(c.tags.contains(Deathbringer.Enums.SHADOW))) {  // Assuming ShadowCard is a common interface or class for Shadow cards
                return false; // Found a non-Shadow card, cannot play this card
            }
        }

        return true; // Only Shadow cards found, can play this card
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
        return new Incognito();
    }
}