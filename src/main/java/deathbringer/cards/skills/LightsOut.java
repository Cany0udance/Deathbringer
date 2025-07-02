
package deathbringer.cards.skills;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import deathbringer.cards.BaseCard;
import deathbringer.cards.attacks.Quench;
import deathbringer.character.Deathbringer;
import deathbringer.effects.ShadowEffect;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.powers.LivelihoodPower;
import deathbringer.powers.TradePower;
import deathbringer.relics.OminousNote;
import deathbringer.relics.TornCloth;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LightsOut extends BaseCard implements ShadowEffectable {
    public static final String ID = makeID("LightsOut");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2  // Energy cost set to -2 for unplayable cards
    );

    private List<TooltipInfo> customTooltips = null;
    private static boolean isProcessingLightsOut = false; // Prevent infinite recursion

    public LightsOut() {
        super(ID, info);
        this.tags.add(Deathbringer.Enums.SHADOW);
        this.tags.add(Deathbringer.Enums.SHADOWPLAY);
        setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png",
                "deathbringer/images/character/cardback/shadowskill_p.png");
        setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png",
                "deathbringer/images/character/cardback/shadowenergyorb_p.png");
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Do nothing because it's unplayable
    }

    @Override
    public void triggerHalfEffect() {
        // LightsOut has no half effect since it's unplayable
    }

    @Override
    public void triggerShadowplayEffect() {
        // Prevent infinite recursion
        if (isProcessingLightsOut) {
            return;
        }

        isProcessingLightsOut = true;
        try {
            triggerLightsOutEffect();
        } finally {
            isProcessingLightsOut = false;
        }
    }

    private void triggerLightsOutEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        // Create a snapshot of all shadow cards except LightsOut cards to prevent recursion
        List<AbstractCard> shadowCardsToTrigger = new ArrayList<>();
        for (AbstractCard card : p.hand.group) {
            if (card.tags.contains(Deathbringer.Enums.SHADOW) &&
                    !card.cardID.equals(LightsOut.ID) &&
                    card != this) {
                shadowCardsToTrigger.add(card);
            }
        }

        // Trigger each shadow card's effects
        for (AbstractCard card : shadowCardsToTrigger) {
            // Double-check the card is still in hand
            if (!p.hand.contains(card)) {
                continue;
            }

            // Add VFX
            float cardX = card.hb.cX;
            float cardY = card.hb.cY;
            addToBot(new VFXAction(new ShadowEffect(cardX, cardY), 0.1f));

            // Trigger half effect
            if (card instanceof ShadowEffectable) {
                ((ShadowEffectable) card).triggerHalfEffect();
            }

            // Trigger shadowplay effect if applicable
            if (card.tags.contains(Deathbringer.Enums.SHADOWPLAY)) {
                if (card instanceof ShadowEffectable) {
                    ((ShadowEffectable) card).triggerShadowplayEffect();
                }
            }

            // Increment shadowplay counter
            deathbringer.Deathbringer.shadowplaysThisCombat++;

            // Schedule card discard (except for Quench)
            if (!card.cardID.equals(Quench.ID)) {
                final AbstractCard cardToDiscard = card;
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (p.hand.contains(cardToDiscard)) {
                            p.hand.moveToDiscardPile(cardToDiscard);
                            cardToDiscard.triggerOnManualDiscard();
                        }
                        this.isDone = true;
                    }
                });
            }

            // Apply secondary effects (Torn Cloth, Ominous Note, etc.)
            applySecondaryEffectsForCard(card);
        }
    }

    private void applySecondaryEffectsForCard(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;

        // Torn Cloth (Penumbra effect) - but only trigger once per turn
        if (card.tags.contains(Deathbringer.Enums.PENUMBRA)) {
            if (p.hasRelic(TornCloth.ID)) {
                // Check if relic is already grayed out (used this turn)
                AbstractRelic tornCloth = p.getRelic(TornCloth.ID);
                if (tornCloth != null && !tornCloth.grayscale) {
                    addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                    tornCloth.flash();
                    tornCloth.grayscale = true;
                }
            }
        }

        // Other secondary effects (Ominous Note, Trade Power, etc.)
        if (p.hasRelic(OminousNote.ID)) {
            applyOminousNoteEffect();
        }

        if (p.hasPower(TradePower.POWER_ID)) {
            int amount = p.getPower(TradePower.POWER_ID).amount;
            addToBot(new GainBlockAction(p, p, amount));
        }

        if (p.hasPower(LivelihoodPower.POWER_ID)) {
            applyLivelihoodEffect(card);
        }
    }

    private void applyOminousNoteEffect() {
        ArrayList<AbstractMonster> targets = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
        targets.removeIf(m -> m.isDying || m.isDead || m.halfDead);

        if (!targets.isEmpty()) {
            AbstractMonster target = targets.get(AbstractDungeon.cardRandomRng.random(0, targets.size() - 1));
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player,
                    new PoisonPower(target, AbstractDungeon.player, 2), 2));

            OminousNote relic = (OminousNote) AbstractDungeon.player.getRelic(OminousNote.ID);
            if (relic != null) {
                relic.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            }
        }
    }

    private void applyLivelihoodEffect(AbstractCard excludeCard) {
        AbstractPlayer p = AbstractDungeon.player;
        int livelihoodAmount = p.getPower(LivelihoodPower.POWER_ID).amount;

        for (int i = 0; i < livelihoodAmount; i++) {
            List<AbstractCard> upgradableCards = p.hand.group.stream()
                    .filter(card -> card.canUpgrade() && card != excludeCard && card != this)
                    .collect(Collectors.toList());

            if (!upgradableCards.isEmpty()) {
                AbstractCard cardToUpgrade = upgradableCards.get(
                        AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1)
                );
                cardToUpgrade.upgrade();
                cardToUpgrade.superFlash();
            }
        }
    }

    // Static method to reset the recursion guard (call this at start of combat/turn)
    public static void resetRecursionGuard() {
        isProcessingLightsOut = false;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (this.customTooltips == null) {
            this.customTooltips = new ArrayList<>();
            TooltipInfo shadowTooltip = new TooltipInfo("Shadow",
                    "#yShadow #ycards #yare #ydesignated #yby #ya #ydifferent #ycard #ybackground #yand #yenergy #yicon. NL NL " +
                            "Whenever you play a Shadow card, another random Shadow card in your hand has its actions triggered at half effectiveness, then is discarded.");
            this.customTooltips.add(shadowTooltip);
        }
        return this.customTooltips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.tags.add(Deathbringer.Enums.UMBRA);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightsOut();
    }
}