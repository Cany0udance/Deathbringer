package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.StranglePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SewingNeedle extends BaseCard {
    public static final String ID = makeID("SewingNeedle");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            0 // Energy cost
    );

    private static final int DAMAGE = 1;
    private static final int STRANGLE = 2;
    private static final int UPG_STRANGLE = 1; // Upgrade amount

    public SewingNeedle() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(STRANGLE, UPG_STRANGLE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Check if multiDamage is not null before proceeding
        if (multiDamage != null) {
            addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.POISON));
        } else {
            // Handle the null case - perhaps log an error or provide an alternative action
            System.out.println("Warning: multiDamage is null.");
        }

        // Check if the room and the monsters list are not null
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                // Check if each monster is not null before applying actions
                if (monster != null) {
                    addToBot(new ApplyPowerAction(monster, p, new StranglePower(monster, magicNumber), magicNumber));
                } else {
                    // Handle the null case for a monster
                    System.out.println("Warning: Encountered a null monster.");
                }
            }
        } else {
            // Handle the null case for the room or monsters list
            System.out.println("Warning: Current room or monsters list is null.");
        }
    }



    @Override
    public AbstractCard makeCopy() {
        return new SewingNeedle();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRANGLE);
            initializeDescription();
        }
    }
}
