package deathbringer;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import deathbringer.cards.BaseCard;
import deathbringer.cards.attacks.ChainedHits;
import deathbringer.cards.skills.LightsOut;
import deathbringer.potions.BasePotion;
import deathbringer.powers.ExtinctionMarkPower;
import deathbringer.relics.BaseRelic;
import deathbringer.util.GeneralUtils;
import deathbringer.util.KeywordInfo;
import deathbringer.util.ShadowUtility;
import deathbringer.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static deathbringer.cards.skills.Nightlight.resetNightlightVFX;
import static deathbringer.powers.OnlyADreamPower.globalCardsToReplay;

import static deathbringer.character.Deathbringer.enemiesMarkedForExtinction;

@SpireInitializer
public class Deathbringer implements
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditRelicsSubscriber,
        PostInitializeSubscriber,
        OnPlayerTurnStartSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber,
        AddAudioSubscriber,
        PostDungeonInitializeSubscriber,
        CustomSavable<HashMap<String, Boolean>> {

    public static ModInfo info;
    public static String modID;
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID);

    private static final String resourcesFolder = "deathbringer";
    private static final String BG_ATTACK = characterPath("cardback/bg_attack.png");
    private static final String BG_ATTACK_P = characterPath("cardback/bg_attack_p.png");
    private static final String BG_SKILL = characterPath("cardback/bg_skill.png");
    private static final String BG_SKILL_P = characterPath("cardback/bg_skill_p.png");
    private static final String BG_POWER = characterPath("cardback/bg_power.png");
    private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
    private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
    private static final String ENERGY_ORB_P = characterPath("cardback/energy_orb_p.png");
    private static final String SMALL_ORB = characterPath("cardback/small_orb.png");
    private static final Color cardColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    private static final String CHAR_SELECT_BUTTON = characterPath("select/button.png");
    private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait.png");

    public static final Color RED_BORDER_GLOW_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);
    public static final String UNSHEATHE_KEY = "Unsheathe";
    public static final String UNSHEATHE_OGG = "deathbringer/audio/unsheathe.ogg";
    public static final String CLEANKILL_KEY = "CleanKill";
    public static final String CLEANKILL_OGG = "deathbringer/audio/CleanKill.ogg";
    public static final String FLASHLIGHTON_KEY = "FlashlightOn";
    public static final String FLASHLIGHTON_OGG = "deathbringer/audio/FlashlightOn.ogg";
    public static final String FLASHLIGHTOFF_KEY = "FlashlightOff";
    public static final String FLASHLIGHTOFF_OGG = "deathbringer/audio/FlashlightOff.ogg";

    private static int permanentStrengthGain = 0;
    public static int chainedHitsCounter = 2;
    public static int shadowplaysThisCombat = 0;

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new Deathbringer();
        BaseMod.addColor(deathbringer.character.Deathbringer.Enums.CARD_COLOR, cardColor,
                BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                SMALL_ORB);
    }

    public Deathbringer() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField(makeID("enemiesMarkedForExtinction"), this);
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        // Handle Extinction Mark power
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.hasPower(ExtinctionMarkPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new InstantKillAction(m));
            }
        }

        // Reset shadow system state for the new turn
        ShadowUtility.resetFirstShadowplayTrigger();
        LightsOut.resetRecursionGuard();
    }

    @Override
    public void receivePostDungeonInitialize() {
        shadowplaysThisCombat = 0;
        permanentStrengthGain = 0;
        chainedHitsCounter = 2;

        // Reset shadow system for new run
        ShadowUtility.resetCombatState();
        LightsOut.resetRecursionGuard();
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(UNSHEATHE_KEY, UNSHEATHE_OGG);
        BaseMod.addAudio(CLEANKILL_KEY, CLEANKILL_OGG);
        BaseMod.addAudio(FLASHLIGHTON_KEY, FLASHLIGHTON_OGG);
        BaseMod.addAudio(FLASHLIGHTOFF_KEY, FLASHLIGHTOFF_OGG);
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(BaseRelic.class, (info, relic) -> {
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool);
                    else
                        BaseMod.addRelic(relic, relic.relicType);
                });
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        // Reset combat counters
        shadowplaysThisCombat = 0;
        chainedHitsCounter = 2;

        // Reset shadow system state for new combat
        ShadowUtility.resetCombatState();
        LightsOut.resetRecursionGuard();

        // Clear any existing global card lists
        if (globalCardsToReplay != null) {
            globalCardsToReplay.clear();
        }

        // Apply permanent strength if any
        if (permanentStrengthGain > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, permanentStrengthGain), permanentStrengthGain)
            );
        }

        // Reset other systems
        resetNightlightVFX();

        // Reset ChainedHits magic numbers in all card piles
        resetChainedHitsInPile(AbstractDungeon.player.hand.group);
        resetChainedHitsInPile(AbstractDungeon.player.drawPile.group);
        resetChainedHitsInPile(AbstractDungeon.player.discardPile.group);
    }

    private void resetChainedHitsInPile(ArrayList<AbstractCard> pile) {
        for (AbstractCard c : pile) {
            if (c instanceof ChainedHits) {
                ((ChainedHits) c).resetChainedHitsMagicNumber();
            }
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        chainedHitsCounter = 2;
        shadowplaysThisCombat = 0;

        // Reset shadow system after battle
        ShadowUtility.resetCombatState();
        LightsOut.resetRecursionGuard();
    }

    // Implement CustomSavable
    @Override
    public HashMap<String, Boolean> onSave() {
        savePermanentStrengthGain();
        return enemiesMarkedForExtinction;
    }

    public static void addPermanentStrength(int amount) {
        permanentStrengthGain += amount;
    }

    // Permanent strength save/load system
    private Preferences prefs;

    private Preferences getPrefs() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences("DeathbringerData");
        }
        return prefs;
    }

    public void savePermanentStrengthGain() {
        getPrefs().putInteger("permanentStrengthGain", permanentStrengthGain);
        getPrefs().flush();
    }

    public void loadPermanentStrengthGain() {
        if (getPrefs().contains("permanentStrengthGain")) {
            permanentStrengthGain = getPrefs().getInteger("permanentStrengthGain");
        }
    }

    @Override
    public void onLoad(HashMap<String, Boolean> loadedMap) {
        if (loadedMap != null) {
            enemiesMarkedForExtinction = loadedMap;
            loadPermanentStrengthGain();
        }
    }

    @Override
    public void receivePostInitialize() {
        registerPotions();
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    /*----------Localization----------*/
    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    private static final String defaultLanguage = "eng";
    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            registerKeyword(keyword);
        }
        if (!defaultLanguage.equals(getLangString())) {
            try {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty()) {
            keywords.put(info.ID, info);
        }
    }

    // Path helper methods
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }

    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }

    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }

    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(Deathbringer.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new deathbringer.character.Deathbringer(),
                CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT, deathbringer.character.Deathbringer.Enums.DEATHBRINGER);
    }

    public static void registerPotions() {
        new AutoAdd(modID)
                .packageFilter(BasePotion.class)
                .any(BasePotion.class, (info, potion) -> {
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(BaseCard.class)
                .setDefaultSeen(true)
                .cards();
    }
}