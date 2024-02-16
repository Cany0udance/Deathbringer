package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OlReliable extends BaseCard {
    public static final String ID = makeID("OlReliable");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2  // Energy cost
    );

    private static final int DAMAGE = 13;
    private static final int BLOCK = 13;
    private static final int UPGRADE_PLUS_DAMAGE_BLOCK = 3;  // Upgrades to 16 damage and block

    public OlReliable() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        if (!super.canUse(p, m)) {
            return false;
        }

        // Check for adjacent Shadow card using keywords
        boolean foundAdjacentShadow = false;
        int cardIndex = p.hand.group.indexOf(this);

        // Check the previous card if it exists and contains the shadow keyword
        if (cardIndex > 0 && p.hand.group.get(cardIndex - 1).tags.contains(Deathbringer.Enums.SHADOW)) {
            foundAdjacentShadow = true;
        }

        // Check the next card if it exists and contains the shadow keyword
        if (cardIndex < p.hand.group.size() - 1 && p.hand.group.get(cardIndex + 1).tags.contains(Deathbringer.Enums.SHADOW)) {
            foundAdjacentShadow = true;
        }

        return foundAdjacentShadow;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE_BLOCK);
            upgradeBlock(UPGRADE_PLUS_DAMAGE_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OlReliable();
    }
}
