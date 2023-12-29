package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class Shroud extends BaseCard {
    public static final String ID = makeID("Shroud");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 2;

    private static final int SHROUDDAMALL = 4;
    private static final int UPG_SHROUDDAMALL = 2;

    public Shroud() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);

        this.baseMagicNumber = SHROUDDAMALL;
        this.magicNumber = this.baseMagicNumber;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, block));
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, block / 2));
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(new CleaveEffect()));
        } else {
            this.addToBot(new VFXAction(p, new CleaveEffect(), 0.2F));
        }
        this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(magicNumber, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(SHROUDDAMALL - UPG_SHROUDDAMALL);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Shroud();
    }
}