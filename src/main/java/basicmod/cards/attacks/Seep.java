package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.effects.SeepEffect;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Seep extends BaseCard {
    public static final String ID = makeID("Seep");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DAMAGE = 5;  // Upgrade to 15 damage

    public Seep() {
        super(ID, info);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int poisonStacks = 0;
        // Check for Poison on the player
        if (p.hasPower(PoisonPower.POWER_ID)) {
            poisonStacks = p.getPower(PoisonPower.POWER_ID).amount;
        }

        // Apply VFX if the player has any Poison
        if (poisonStacks > 0) {
            addToBot(new VFXAction(new SeepEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.5F));
            for (int i = 0; i < poisonStacks; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Seep();
    }
}
