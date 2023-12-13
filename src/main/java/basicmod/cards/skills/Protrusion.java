package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class Protrusion extends BaseCard {
    public static final String ID = makeID("Protrusion");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 1;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public Protrusion() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        this.magicNumber = this.baseMagicNumber = MAGIC; // Setting magicNumber for Thorns
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));

        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    public void triggerHalfEffect() {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block / 2));
    }

    public void triggerShadowplayEffect() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(UPG_MAGIC); // Upgrading magicNumber
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Protrusion();
    }
}