package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;

public class TunnelVision extends BaseCard {
    public static final String ID = makeID("TunnelVision");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 14;
    private static final int UPG_DAMAGE = 18;

    private static final int VULNERABLE = 2;
    private static final int UPG_VULNERABLE = 3;

    public TunnelVision() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        baseMagicNumber = VULNERABLE;
        magicNumber = baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage and apply Vulnerable.
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));

        // Discard non-adjacent cards
        int thisCardIndex = p.hand.group.indexOf(this);
        for (int i = 0; i < p.hand.size(); i++) {
            if (Math.abs(thisCardIndex - i) > 1) { // check for non-adjacent cards
                AbstractCard cardToDiscard = p.hand.group.get(i);
                addToBot(new DiscardSpecificCardAction(cardToDiscard));
            }
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        addToBot(new DiscardSpecificCardAction(this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TunnelVision();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_VULNERABLE - VULNERABLE);
            initializeDescription();
        }
    }
}
