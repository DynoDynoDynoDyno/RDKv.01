import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Collections;
import java.util.List;


@ScriptManifest(category = Category.COMBAT, name = "RDK", author = "Dyno", version = 0.1)
public class Main extends AbstractScript {

    private final Area COMBAT_AREA = new Area(1573, 5087, 1598, 5061);
    private final Area S1 = new Area(3540, 10484, 3559, 10468);
    private final Area S2 = new Area(3541, 10462, 3558, 10449);
    private final Area S3 = new Area(1563, 5079, 1572, 5058);
    private final Area GE = new Area(3158, 3497, 3171, 3483);
    private final Area CASTLE_WARS = new Area(2435, 3099, 2446, 3080);
    private final Area RIMMINGTON_PORTAL = new Area(2949, 3226, 2958, 3220);
    private final Area BROKEN_DOORS = new Area(3545, 10486, 3553, 10482);
    private final Area BARRIER = new Area(1571, 5079, 1577, 5070);
    private final Area STAIRS = new Area(3546, 10472, 3553, 10463);
    private Area destination = null;
    Player me = Players.getLocal();
    Tile targTile = null;
    boolean hasLooted = false;

    @Override
    public int onLoop() {
        // My destination walker
        if (destination != null){
            Tile randomTile = destination.getRandomTile();
            if (Walking.shouldWalk(5) && Walking.walk(randomTile)) {
            }
            if (destination.contains(me)) {
                destination = null;
            }
        }
        // If outside rimmington portal
        if (RIMMINGTON_PORTAL.contains(me)) {
            Inventory.interact("Teleport to house", "Break");
        }
        // If inside house
        if (GameObjects.closest("Ornate pool of Rejuvenation") != null) {
            if (Prayers.isQuickPrayerActive()) {
                Prayers.toggleQuickPrayer(false);
            }
            if (Combat.getHealthPercent() < 100 ||
                    Combat.isEnvenomed() ||
                    Combat.isPoisoned() ||
                    Skills.getBoostedLevel(Skill.PRAYER) < Skills.getRealLevel(Skill.PRAYER)) {
                GameObjects.closest("Ornate pool of Rejuvenation").interact("Drink");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(500, 1500));
                Sleep.sleep(1000, 1500);
            }
            if (Inventory.contains(
                    "Divine super combat potion(4)",
                    "Extended antifire(4)",
                    "Teleport to house",
                    "Prayer potion(4)",
                    "Shark"
            )) {
                GameObjects.closest("Digsite Pendant").interact("Lithkren");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(500, 1500));
                Sleep.sleep(1000, 1500);
            } else {
                GameObjects.closest("Ornate Jewellery Box").interact("Castle Wars");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(500, 1500));
                Sleep.sleep(1000, 1500);
            }
        }
        // If at castle wars
        if (CASTLE_WARS.contains(me)) {
            if (Inventory.contains(
                    "Divine super combat potion(4)",
                    "Extended antifire(4)",
                    "Teleport to house",
                    "Prayer potion(4)",
                    "Shark"
            )) {
                if (Bank.isOpen()) {
                    Bank.close();
                } else {
                    Inventory.interact("Teleport to house", "Break");
                }
            } else {
                if (!Bank.isOpen()) {
                    Bank.open();
                    Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(500, 1500));
                    Sleep.sleep(1000, 1500);
                } else {
                    Bank.depositAllItems();
                    Sleep.sleepUntil(() -> Inventory.isEmpty(), Calculations.random(500, 1500));
                    // Withdrawing combat pots
                    Bank.withdraw("Divine super combat potion(4)", 1);
                    Sleep.sleepUntil(() -> Inventory.contains("Divine super combat potion(4)"), Calculations.random(1000, 1500));
                    // Withdrawing extended antifires
                    Bank.withdraw("Extended antifire(4)", 1);
                    Sleep.sleepUntil(() -> Inventory.contains("Extended antifire(4)"), Calculations.random(1000, 1500));
                    // Withdrawing house teleports
                    Bank.withdraw("Teleport to house", 20);
                    Sleep.sleepUntil(() -> Inventory.contains("Teleport to house"), Calculations.random(1000, 1500));
                    // Withdrawing prayer potion
                    Bank.withdraw("Prayer potion(4)", 5);
                    Sleep.sleepUntil(() -> Inventory.contains("Prayer potion(4)"), Calculations.random(1000, 1500));
                    // Withdrawing sharks
                    Bank.withdraw("Shark", 20);
                    Sleep.sleepUntil(() -> Inventory.contains("Shark"), Calculations.random(1000, 1500));
                }
            }
        }
        // S1
        if (S1.contains(me)) {
            if (GameObjects.closest("Broken Doors") != null
                    && BROKEN_DOORS.contains(GameObjects.closest("Broken Doors"))) {
                GameObjects.closest("Broken Doors").interact("Enter");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(1500, 2500));
                Sleep.sleep(2000, 3500);
            }
        }
        // S2
        if (S2.contains(me)) {
            if (GameObjects.closest("Staircase") != null
                    && STAIRS.contains(GameObjects.closest("Staircase"))) {
                GameObjects.closest("Staircase").interact("Climb");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(1500, 2500));
                Sleep.sleep(2000, 3500);
            } else {
                Tile randomTile = STAIRS.getRandomTile();
                if (Walking.shouldWalk(5) && Walking.walk(randomTile)) {
                }
            }
        }
        // S3
        if (S3.contains(me)) {
            if (GameObjects.closest("Barrier") != null
                    && BARRIER.contains(GameObjects.closest("Barrier"))) {
                GameObjects.closest("Barrier").interact("Pass");
                Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(1500, 2500));
                Sleep.sleep(2000, 3500);
            } else {
                Tile randomTile = BARRIER.getRandomTile();
                if (Walking.shouldWalk(5) && Walking.walk(randomTile)) {
                }
            }
        }
        // Combat area
        if (COMBAT_AREA.contains(me)) {
            if (Skills.getBoostedLevel(Skill.PRAYER) < 20) {
                if (Inventory.interact("Prayer potion(1)", "Drink")){
                } else if (Inventory.interact("Prayer potion(2)", "Drink")) {
                } else if (Inventory.interact("Prayer potion(3)", "Drink")) {
                } else if (Inventory.interact("Prayer potion(4)", "Drink")) {
                }
            }
            if (Combat.getHealthPercent() < 65) {
                Inventory.interact("Shark", "Eat");
            }
            if (Skills.getBoostedLevel(Skill.STRENGTH) < 100) {
                if (Inventory.interact("Divine super combat potion(1)", "Drink")){
                } else if (Inventory.interact("Divine super combat potion(2)", "Drink")) {
                } else if (Inventory.interact("Divine super combat potion(3)", "Drink")) {
                } else if (Inventory.interact("Divine super combat potion(4)", "Drink")) {
                }
            }
            if (!Prayers.isQuickPrayerActive()) {
                Prayers.toggleQuickPrayer(true);
            }
            if (!Combat.isAntiFireEnabled()) {
                if (Inventory.interact("Extended antifire(1)", "Drink")){
                } else if (Inventory.interact("Extended antifire(2)", "Drink")) {
                } else if (Inventory.interact("Extended antifire(3)", "Drink")) {
                } else if (Inventory.interact("Extended antifire(4)", "Drink")) {
                }
            }
            if (!Inventory.contains("Shark")) {
                Inventory.interact("Teleport to house", "Break");
            }
            if (!Inventory.contains("Prayer potion(4)", "Prayer potion(3)", "Prayer potion(2)", "Prayer potion(1)")) {
                Inventory.interact("Teleport to house", "Break");
            }
            Filter<NPC> dragFilter = npc -> npc != null && npc.getName().equals("Rune dragon") && npc.canAttack() && !npc.isInCombat();
            NPC drag = NPCs.closest(dragFilter);
           //Filter<NPC> targFilter = tnpc -> tnpc != null && tnpc.isInCombat() && tnpc.getInteractingCharacter().equals(me);
            Filter<NPC> targFilter = tnpc -> tnpc != null && tnpc.isInCombat() && tnpc.getInteractingCharacter() != null && tnpc.getInteractingCharacter().equals(me);
            NPC targ = NPCs.closest(targFilter);
            if (!me.isInCombat()) {
                if (drag != null) {
                    drag.interact("Attack");
                    Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(1500, 2500));
                    Sleep.sleep(2000, 3500);
                } else {
                    log("Drag unavailable");
                }
            }
            if (targ != null && !hasLooted) {
                targTile = targ.getTile();
            }
            if (targTile != null && !hasLooted) {
                log("Attempting to loot items from target tile: " + targTile);
                Filter<GroundItem> lootFilter = item -> item != null && item.getTile().equals(targTile);
                List<GroundItem> lootList = GroundItems.all(lootFilter);
                log("Found " + lootList.size() + " loot items on target tile");
                while (!lootList.isEmpty()) {
                    GroundItem loot = lootList.get(0);
                    if (loot != null) {
                        log("Interacting with loot item: " + loot.getName());
                        loot.interact("Take");
                        Sleep.sleepUntil(() -> !me.isMoving(), Calculations.random(1500, 2500));
                        lootList = GroundItems.all(lootFilter);
                    }
                    if (Inventory.isFull()) {
                        if (Inventory.contains("Shark")) {
                            Inventory.interact("Shark", "Eat");
                        } else {
                            Inventory.interact("Teleport to house", "Break");
                            break;
                        }
                    }
                }
                hasLooted = true;
            }
        }
        // Script doesn't get new inventory at castlewars unless all items are out - need to add if exact amount of sharks aren't in inventory
        return 600;
    }
}
