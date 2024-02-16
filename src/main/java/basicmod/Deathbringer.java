package basicmod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import basicmod.cards.BaseCard;
import basicmod.cards.attacks.ChainedHits;
import basicmod.potions.BasePotion;
import basicmod.potions.HemlockSip;
import basicmod.potions.LiquidCongregation;
import basicmod.potions.TwistedPair;
import basicmod.powers.ExtinctionMarkPower;
import basicmod.relics.BaseRelic;
import basicmod.util.GeneralUtils;
import basicmod.util.KeywordInfo;
import basicmod.util.ShadowUtility;
import basicmod.util.TextureLoader;
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

import static basicmod.cards.skills.Nightlight.resetNightlightVFX;
import static basicmod.powers.OnlyADreamPower.globalCardsToReplay;

import static basicmod.character.Deathbringer.enemiesMarkedForExtinction;

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
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "basicmod";
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
    public static final Color RED_BORDER_GLOW_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f); // Red color

    public static final String UNSHEATHE_KEY = "Unsheathe";
    public static final String UNSHEATHE_OGG = "basicmod/audio/unsheathe.ogg";
    public static final String CLEANKILL_KEY = "CleanKill";
    public static final String CLEANKILL_OGG = "basicmod/audio/CleanKill.ogg";
    public static final String FLASHLIGHTON_KEY = "FlashlightOn";
    public static final String FLASHLIGHTON_OGG = "basicmod/audio/FlashlightOn.ogg";
    public static final String FLASHLIGHTOFF_KEY = "FlashlightOff";
    public static final String FLASHLIGHTOFF_OGG = "basicmod/audio/FlashlightOff.ogg";
    private static int permanentStrengthGain = 0;
    public static int chainedHitsCounter = 2;
    public static int shadowplaysThisCombat = 0;


    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {

        new Deathbringer();

        BaseMod.addColor(basicmod.character.Deathbringer.Enums.CARD_COLOR, cardColor,
                BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                SMALL_ORB);
    }

    public Deathbringer() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField(makeID("enemiesMarkedForExtinction"), this); // Register the save field
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.hasPower(ExtinctionMarkPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new InstantKillAction(m));
            }
            ShadowUtility.resetFirstShadowplayTrigger();
        }
    }


    @Override
    public void receivePostDungeonInitialize() {
        shadowplaysThisCombat = 0;
        permanentStrengthGain = 0;  // Reset the permanent Strength gain
        chainedHitsCounter = 2;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(UNSHEATHE_KEY, UNSHEATHE_OGG);
        BaseMod.addAudio(CLEANKILL_KEY, CLEANKILL_OGG);
        BaseMod.addAudio(FLASHLIGHTON_KEY, FLASHLIGHTON_OGG);
        BaseMod.addAudio(FLASHLIGHTOFF_KEY, FLASHLIGHTOFF_OGG);
    }

    // Reset at the start of each combat if necessary

    @Override
    public void receiveEditRelics() { //somewhere in the class
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseRelic.class) //In the same package as this class
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
                    //If you want all your relics to be visible by default, just remove this if statement.
                    if (info.seen)
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        shadowplaysThisCombat = 0;
        globalCardsToReplay.clear();
        if (permanentStrengthGain > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, permanentStrengthGain), permanentStrengthGain)
            );
        }
        ShadowUtility.shatteredTriggered = false;
        ShadowUtility.clothTriggered = false;
        resetNightlightVFX();
        chainedHitsCounter = 2;

        // Reset the magic numbers for all instances of ChainedHits in all relevant piles
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof ChainedHits) {
                ((ChainedHits) c).resetChainedHitsMagicNumber();
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof ChainedHits) {
                ((ChainedHits) c).resetChainedHitsMagicNumber();
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof ChainedHits) {
                ((ChainedHits) c).resetChainedHitsMagicNumber();
            }
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        chainedHitsCounter = 2;
        shadowplaysThisCombat = 0;
    }

    // Implement CustomSavable
    @Override
    public HashMap<String, Boolean> onSave() {
        savePermanentStrengthGain();
        return enemiesMarkedForExtinction; // Replace with your actual HashMap
    }

    public static void addPermanentStrength(int amount) {
        permanentStrengthGain += amount;
    }

    // To save permanentStrengthGain when needed
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
            enemiesMarkedForExtinction = loadedMap; // Replace with your actual HashMap
            loadPermanentStrengthGain();
        }
    }

    @Override
    public void receivePostInitialize() {
        registerPotions();
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
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
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
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


    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(Deathbringer.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new basicmod.character.Deathbringer(),
                CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT, basicmod.character.Deathbringer.Enums.DEATHBRINGER);
    }

    public static void registerPotions() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BasePotion.class) //In the same package as this class
                .any(BasePotion.class, (info, potion) -> { //Run this code for any classes that extend this class
                    //These three null parameters are colors.
                    //If they're not null, they'll overwrite whatever color is set in the potions themselves.
                    //This is an old feature added before having potions determine their own color was possible.
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                    //playerClass will make a potion character-specific. By default, it's null and will do nothing.
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseCard.class) //In the same package as this class
                .setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }
}
