package deathbringer.util;

import basemod.BaseMod;
import deathbringer.Deathbringer;
import deathbringer.cards.attacks.*;
import deathbringer.cards.skills.*;
import deathbringer.cards.statuses.Slipup;
import deathbringer.effects.ShadowEffect;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.powers.LivelihoodPower;
import deathbringer.powers.TradePower;
import deathbringer.powers.VeilPower;
import deathbringer.relics.OminousNote;
import deathbringer.relics.RepairedCompass;
import deathbringer.relics.ShatteredCompass;
import deathbringer.relics.TornCloth;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.*;
import java.util.stream.Collectors;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShadowUtility {
    private static final Logger logger = LogManager.getLogger(ShadowUtility.class.getName());
    private static final List<String> NON_TRIGGER_CARDS = Arrays.asList(
            Slipup.ID, SubconsciousKiller.ID, Sanctuary.ID
    );

    // Combat state - reset each combat
    private static boolean shatteredTriggered = false;
    private static boolean clothTriggered = false;
    private static boolean firstShadowplayTriggered = false;
    private static Set<AbstractCard> processedCards = new HashSet<>();

    // Reset all combat state
    public static void resetCombatState() {
        shatteredTriggered = false;
        clothTriggered = false;
        firstShadowplayTriggered = false;
        processedCards.clear();
    }

    // Legacy method names for compatibility
    public static void resetProcessedCards() {
        processedCards.clear();
    }

    public static void resetFirstShadowplayTrigger() {
        firstShadowplayTriggered = false;
    }

    // Helper method for calculating shadow damage with vulnerability
    public static int calculateShadowDamage(int baseDamage, AbstractMonster target) {
        int damage = baseDamage / 2;
        if (target != null && target.hasPower("Vulnerable")) {
            damage = (int)(damage * 1.5f);
        }
        return damage;
    }

    public static void triggerGeneralShadowEffect(AbstractCard playedCard) {
        AbstractPlayer p = AbstractDungeon.player;
        List<AbstractCard> availableShadowCards = getAvailableShadowCards(p, playedCard);

        if (availableShadowCards.isEmpty()) {
            handleSingleCardShadow(playedCard);
            return;
        }

        AbstractCard selectedCard = selectShadowCard(availableShadowCards);
        if (selectedCard != null) {
            executeShadowSequence(selectedCard, playedCard);
        }
    }

    private static List<AbstractCard> getAvailableShadowCards(AbstractPlayer p, AbstractCard excludeCard) {
        List<AbstractCard> shadowCards = new ArrayList<>();
        List<AbstractCard> umbraCards = new ArrayList<>();
        List<AbstractCard> penumbraCards = new ArrayList<>();

        for (AbstractCard card : p.hand.group) {
            if (!card.tags.contains(deathbringer.character.Deathbringer.Enums.SHADOW) || card == excludeCard) {
                continue;
            }

            shadowCards.add(card);
            if (card.tags.contains(deathbringer.character.Deathbringer.Enums.UMBRA)) {
                umbraCards.add(card);
            } else if (card.tags.contains(deathbringer.character.Deathbringer.Enums.PENUMBRA)) {
                penumbraCards.add(card);
            }
        }

        // Priority: Umbra > Regular Shadow > Penumbra
        if (!umbraCards.isEmpty()) return umbraCards;

        shadowCards.removeAll(penumbraCards); // Remove penumbra from regular shadows
        if (!shadowCards.isEmpty()) return shadowCards;

        return penumbraCards;
    }

    private static AbstractCard selectShadowCard(List<AbstractCard> cards) {
        if (cards.isEmpty()) return null;
        Collections.shuffle(cards, AbstractDungeon.cardRandomRng.random);
        return cards.get(0);
    }

    private static void executeShadowSequence(AbstractCard shadowCard, AbstractCard playedCard) {
        // Visual effect
        addShadowVFX(shadowCard);

        // Standard shadow effects
        if (!NON_TRIGGER_CARDS.contains(shadowCard.cardID)) {
            triggerCardHalfEffect(shadowCard);
            Deathbringer.shadowplaysThisCombat++;
        }

        if (shadowCard.tags.contains(deathbringer.character.Deathbringer.Enums.SHADOWPLAY)) {
            triggerCardShadowplayEffect(shadowCard);
        }

        // Discard the card (with delay)
        scheduleCardDiscard(shadowCard);

        // Apply secondary effects
        applySecondaryEffects(shadowCard);
    }

    private static void handleSingleCardShadow(AbstractCard playedCard) {
        AbstractPlayer p = AbstractDungeon.player;
        boolean hasShattered = p.hasRelic(ShatteredCompass.ID);
        boolean hasRepaired = p.hasRelic(RepairedCompass.ID);

        if (!hasShattered && !hasRepaired) return;
        if (hasShattered && shatteredTriggered) return;

        addShadowVFX(playedCard);

        if (!NON_TRIGGER_CARDS.contains(playedCard.cardID)) {
            triggerCardHalfEffect(playedCard);
            Deathbringer.shadowplaysThisCombat++;
        }

        // Handle relic effects
        if (hasShattered && !shatteredTriggered) {
            activateRelic(ShatteredCompass.ID);
            shatteredTriggered = true;
        }

        if (hasRepaired) {
            activateRelic(RepairedCompass.ID);
        }

        if (playedCard.tags.contains(deathbringer.character.Deathbringer.Enums.SHADOWPLAY)) {
            triggerCardShadowplayEffect(playedCard);
        }

        applySecondaryEffects(playedCard);
    }

    private static void triggerCardHalfEffect(AbstractCard card) {
        if (card instanceof ShadowEffectable) {
            ((ShadowEffectable) card).triggerHalfEffect();
        }
    }

    private static void triggerCardShadowplayEffect(AbstractCard card) {
        if (card instanceof ShadowEffectable) {
            ((ShadowEffectable) card).triggerShadowplayEffect();

            // Handle Veil power multiplication (only once per turn)
            if (!firstShadowplayTriggered) {
                handleVeilMultiplication(card);
                firstShadowplayTriggered = true;
            }
        }
    }

    private static void handleVeilMultiplication(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;
        if (!p.hasPower(VeilPower.POWER_ID)) return;

        int veilStacks = p.getPower(VeilPower.POWER_ID).amount;
        for (int i = 0; i < veilStacks; i++) {
            if (card instanceof ShadowEffectable) {
                ((ShadowEffectable) card).triggerShadowplayEffect();
            }
        }
    }

    private static void addShadowVFX(AbstractCard card) {
        float x = card.hb.cX;
        float y = card.hb.cY;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShadowEffect(x, y)));
    }

    private static void scheduleCardDiscard(AbstractCard card) {
        AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));

        if (!card.cardID.equals(Quench.ID)) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractPlayer p = AbstractDungeon.player;
                    if (p.hand.contains(card)) {
                        p.hand.moveToDiscardPile(card);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    private static void applySecondaryEffects(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;

        // Torn Cloth (Penumbra effect)
        if (card.tags.contains(deathbringer.character.Deathbringer.Enums.PENUMBRA)) {
            if (p.hasRelic(TornCloth.ID) && !clothTriggered) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1)
                );
                activateRelic(TornCloth.ID);
                clothTriggered = true;
            }
        }

        // Ominous Note
        if (p.hasRelic(OminousNote.ID)) {
            applyOminousNoteEffect();
        }

        // Trade Power
        if (p.hasPower(TradePower.POWER_ID)) {
            int amount = p.getPower(TradePower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, amount));
        }

        // Livelihood Power
        if (p.hasPower(LivelihoodPower.POWER_ID)) {
            applyLivelihoodEffect(card);
        }
    }

    private static void activateRelic(String relicID) {
        AbstractPlayer p = AbstractDungeon.player;
        com.megacrit.cardcrawl.relics.AbstractRelic relic = p.getRelic(relicID);
        if (relic != null) {
            relic.flash();
            relic.grayscale = true;
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(p, relic)
            );
        }
    }

    private static void applyLivelihoodEffect(AbstractCard excludeCard) {
        AbstractPlayer p = AbstractDungeon.player;
        int livelihoodAmount = p.getPower(LivelihoodPower.POWER_ID).amount;

        for (int i = 0; i < livelihoodAmount; i++) {
            List<AbstractCard> upgradableCards = p.hand.group.stream()
                    .filter(card -> card.canUpgrade() && card != excludeCard)
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

    // External trigger method
    public static void triggerShadowplayFromExternal(AbstractCard card) {
        triggerCardShadowplayEffect(card);
    }


    // Helper method for VFX with custom duration
    private static void addShadowVFX(AbstractCard card, float duration) {
        float x = card.hb.cX;
        float y = card.hb.cY;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShadowEffect(x, y), duration));
    }

    private static void applyOminousNoteEffect() {
        ArrayList<AbstractMonster> targets = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
        targets.removeIf(m -> m.isDying || m.isDead || m.halfDead);

        if (!targets.isEmpty()) {
            AbstractMonster target = targets.get(AbstractDungeon.cardRandomRng.random(0, targets.size() - 1));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, 2), 2)
            );
            OminousNote relic = (OminousNote) AbstractDungeon.player.getRelic(OminousNote.ID);
            if (relic != null) {
                relic.flash();
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            }
        }
    }
}