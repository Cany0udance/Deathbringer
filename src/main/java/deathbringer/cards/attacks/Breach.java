package deathbringer.cards.attacks;

import basemod.helpers.TooltipInfo;
import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import java.util.ArrayList;
import java.util.List;

public class Breach extends BaseCard {
    public static final String ID = makeID("Breach");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            1  // Energy cost
    );

    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DAMAGE = 4;  // Upgrade to 16 damage
    private static final int BASE_BLOCK = 15;
    private static final int UPGRADE_PLUS_BLOCK = 5;  // Upgrade to 20 block
    private List<TooltipInfo> customTooltips = null;

    public Breach() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseBlock = BASE_BLOCK;
        this.isMultiDamage = true;  // Important for hitting all enemies
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        this.tags.add(Deathbringer.Enums.PENUMBRA);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowattack.png", "deathbringer/images/character/cardback/shadowattack_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png", "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage to ALL enemies
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));

        ShadowUtility.triggerGeneralShadowEffect(this);  // Trigger Shadow effect
    }

    public void triggerHalfEffect() {
        // Calculate half damage for each enemy
        int[] halfDamage = new int[this.multiDamage.length];
        for (int i = 0; i < this.multiDamage.length; i++) {
            halfDamage[i] = this.multiDamage[i] / 2;
        }
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, halfDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void triggerShadowplayEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        // Calculate Block gain considering Dexterity
        int dexterity = p.hasPower(DexterityPower.POWER_ID) ? p.getPower(DexterityPower.POWER_ID).amount : 0;
        int totalBlock = this.baseBlock + dexterity;

        addToBot(new GainBlockAction(p, p, totalBlock));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (this.customTooltips == null) {
            this.customTooltips = new ArrayList<>();
            TooltipInfo shadowTooltip = new TooltipInfo("Shadow", "#yShadow #ycards #yare #ydesignated #yby #ya #ydifferent #ycard #ybackground #yand #yenergy #yicon. NL NL Whenever you play a Shadow card, another random Shadow card in your hand has its actions triggered at half effectiveness, then is discarded.");
            this.customTooltips.add(shadowTooltip);
        }
        return this.customTooltips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Breach();
    }
}