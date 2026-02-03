# Thaumcraft - Notes & TODO

---

## Bugs

- Rendering isn't working (BERs not showing)
- Pedestals run pickup logic when their slot is empty
- Arcane workbench menu needs a rewrite

## Cleanup

- **Simple(Block)MetaItem system** - Feels redundant now, either rewrite or remove
- **Translations** - Standardize and clean up lang keys

## Notes

- Forge OBJ loader quirk: Blockbench exports have UVs flipped vertically and offset by 0.5 on X/Z

---

## TODO

### Core Systems

- [X] Aura Management (Chunk-based Vis)
- [X] Flux & Rift Mechanics (Rifts, Flux events)
- [ ] **Taint System**
    - [ ] Spread Logic
    - [ ] Biome Conversion
    - [ ] Flux Goo
    - [ ] Taint Fibres
    - [ ] Taint Logs
    - [ ] Taint Blocks (Crust, Soil, etc.)
    - [ ] Taint Features (Geysers, Tendrils)
- [ ] Multiblock Structure Logic
- [-] **Alchemy**
    - [X] Crucible Block & Block Entity &Crucible Recipe System
    - [X] Hedge Alchemy Recipes (tallow, leather, gunpowder x2, slime x2, glowstone x2, dye x2, clay, string, cobweb,
      lava all work)
    - [X] Metallurgy Recipes (brass ingot, thaumium ingot)
    - [X] Vis Crystal Recipes
    - [X] Alumentum Recipe
    - [ ] Essentia Distillation (Smelter, Alembic)
    - [ ] Void Ingot Recipe (needs void seed item)
    - [ ] Nitor Recipe (needs nitor block)
    - [ ] Metal Purification (cluster items needed)
    - [ ] Advanced Alchemy (Bath Salts, Sanity Soap, Liquid Death, Bottled Taint)
- [ ] **Research System**
    - [ ] Theorycrafting minigame
    - [ ] Knowledge types (Observations, Theories)
    - [ ] Scanning mechanics
- [ ] **Infusion Crafting**
    - [ ] Stability mechanics
    - [ ] Instability events
- [ ] **Effects & Potions**
    - [ ] Warp (Temporary, Permanent, Sticky)
    - [ ] Warp Ward
    - [ ] Sun Scorned
    - [ ] Blurred Vision
    - [ ] Thaumarhia
    - [ ] Unnatural Hunger
    - [ ] Death Gaze
    - [ ] Infectious Vis Exhaustion

### World Generation & Environment

- [-] **Ores**
    - [X] Add Cinnabar, Amber, and Quarts Textures & Raw Variants of Cinn/Amber
    - [X] All 3 Ores Generate
    - [ ] Elemental Crystals (Air, Fire, Water, Earth, Order, Entropy) in caves
- [X] **Trees**
    - [X] Greatwood (Logs, Leaves, Saplings, Planks, Stairs, Slabs, Fences, etc.)
    - [X] Silverwood (Logs, Leaves, Saplings, Planks, Stairs, Slabs, Fences, etc.)
    - [ ] **NOTE: Door and Wood Type textures need redesign**
- [X] **Plants**
    - [X] Cinderpearl
    - [X] Shimmerleaf
    - [X] Vishroom
- [ ] **Fluids**
    - [ ] Liquid Death
    - [ ] Purifying Fluid
- [ ] **Structures**
    - [ ] Hilltop Mounds
    - [ ] Obelisks (Eldritch/Crimson)
    - [ ] Wisp Nests
- [~] **Biomes**
    - [~] Magic Forest
        - [X] Biome registration & colors (grass, foliage, water)
        - [X] Tree generation (Greatwood 1/12, Silverwood 1/18, Big Magic Trees)
        - [X] Ambient grass blocks - REPLACE THIS WITH AMBIENT PARTICLES
        - [X] Vishroom placement - Looks bad
        - [X] Cinderpearl placement
        - [X] Shimmerleaf placement
        - [ ] Mob spawns (Pech, Wisp, Witch, Vex)
        - [X] Big mushroom generation
        - [X] Config options (weight, enable/disable)
- [ ] Loot Tables (Dungeon chests, Pech trades)

### Entities & Mobs

- [ ] **Ambient**
    - [ ] Wisps
    - [ ] Firebats
- [ ] **Common**
    - [ ] Brainy Zombies (Normal & Giant)
    - [ ] Pech (Forager, Hunter, Mage)
    - [ ] Thaumic Slimes
- [ ] **Cultists**
    - [ ] Crimson Knights
    - [ ] Crimson Clerics
    - [ ] Crimson Praetors
- [ ] **Eldritch**
    - [ ] Eldritch Guardians
    - [ ] Eldritch Crabs
    - [ ] Eldritch Watchers
    - [ ] Mind Spiders
- [ ] **Tained**
    - [ ] Taint Crawlers
    - [ ] Taint Swarms
    - [ ] Taintacles (Normal & Giant)
- [ ] **Bosses**
    - [ ] The Taintaculus?
    - [ ] Crimson Portal logic

### Golems

- [ ] **Entity & AI**
    - [ ] Pathfinding & Task handling
    - [ ] Interaction properties
- [ ] **Crafting**
    - [ ] Golem Press
    - [ ] Golem Bell
- [ ] **Parts**
    - [ ] Materials (Wood, Iron, Brass, Thaumium, Void, etc.)
    - [ ] Heads (Smart, Perceptive, etc.)
    - [ ] Arms (Claws, Grippers, Breakers, Darts)
    - [ ] Legs (Walker, Roller, Levitation, Climber)
    - [ ] Add-ons
- [ ] **Seals (Task definitions)**
    - [ ] Pickup & Pickup Advanced
    - [ ] Store & Stock
    - [ ] Empty & Empty Advanced
    - [ ] Fill & Fill Advanced
    - [ ] Provide
    - [ ] Harvest
    - [ ] Guard & Guard Advanced
    - [ ] Butcher
    - [ ] Breaker & Breaker Advanced
    - [ ] Lumber
    - [ ] Use
    - [ ] Heart (Seal connection logic)

### Equipment: Armor

- [ ] Thaumium Armor
- [ ] Void Armor & Robes
- [x] Thaumaturge's Armor
- [x] Cultist Armor (Robes, Plate, Leader)
    - [ ] Better System for 3D Armor
    - [ ] Cloth Flaps
- [ ] Fortress Armor (Helm upgrades: Masks)
- [ ] Goggles of Revealing (HUD overlay)
- [ ] Boots of the Traveller (Jump/Speed boost)

### Equipment: Tools & Weapons

- [X] Elemental Tools * Need to improve VFX
- [X] Thaumium Tools (Axe, Hoe, Pick, Shovel, Sword)
- [X] Void Tools (Regeneration logic)
- [X] Primal Crusher
- [X] Crimson Blade
- [ ] Thaumometer (Scanning logic)
- [X] Sanity Checker
- [ ] Grapple Gun
- [X] Scribing Tools (Research table usage)

### Equipment: Casting (Auromancy)

- [ ] **Caster Gauntlet**
- [ ] **Focus Pouch**
- [ ] **Foci System**
    - [ ] **Mediums**
        - [ ] Touch
        - [ ] Bolt
        - [ ] Projectile
        - [ ] Cloud
        - [ ] Mine
        - [ ] Spellbat
        - [ ] Plan
    - [ ] **Effects**
        - [ ] Fire
        - [ ] Frost
        - [ ] Air
        - [ ] Earth
        - [ ] Break
        - [ ] Exchange
        - [ ] Heal
        - [ ] Curse
        - [ ] Rift
        - [ ] Flux
    - [ ] **Modifiers**
        - [ ] Scatter
        - [ ] Split Target
        - [ ] Split Trajectory

### Baubles???

- [ ] Vis Amulet (Vis recharge)
- [ ] Cloud Ring (Flight/Fall protect)
- [ ] Charm of Undying
- [ ] Verdant Charm (lifegiver/sustainer)
- [ ] Voidseer Charm
- [ ] Curiosity Band

### Alchemy & Consumables

- [-] **Blocks**
    - [-] Crucible (Crafting logic)
    - [ ] Arcane Spa
    - [ ] Essentia Smelter (Basic & Thaumium)
    - [ ] Arcane Alembics
    - [ ] Centrifuge
- [ ] **Essentia Transport**
    - [X] Tubes
    - [ ] Tube Variants (Valve, Filter, Buffer, Restricted, Oneway)
    - [ ] Labels
    - [ ] Brain Jar
- [-] **Items**
    - [X] Phials
    - [X] Vis Crystals
    - [X] Quartz Sliver - for making the crystals and all that
    - [X] Alumentum
    - [X] Tallow
    - [ ] Bath Salts
    - [ ] Sanity Soap
    - [ ] Bottle of Taint
    - [ ] Causality Collapser
    - [ ] Meat Nuggets & Triple Meat Treat
    - [ ] Zombie Brains (Consumable)

### Blocks: Devices & Machines

- [ ] **Research**
    - [ ] Research Table
    - [ ] Brain in a Jar
- [ ] **Crafting**
    - [X] Arcane Workbench
    - [ ] Focal Manipulator (Focus crafting)
    - [ ] Infusion Altar (Pillars, Pedestals, Matrix)
    - [ ] Stabilizers
- [ ] **Utility**
    - [X] Dioptra * NEEDS VATUU POLISH FOR RENDERER & BLOCK MODEL
    - [X] Arcane Levitator
    - [ ] Arcane Ear & Toggle
    - [ ] Arcane Lamp / Lamp of Growth / Lamp of Fertility
    - [ ] Infernal Furnace
    - [ ] Mirrors (Magic Hand Mirror link, Essentia Mirrors)
    - [ ] Redstone Relay
    - [ ] Hungry Chest
    - [ ] Everfull Urn
    - [ ] Arcane Bellows
    - [ ] Vis Battery
    - [ ] Vis Generator
    - [ ] Flux Condenser / Lattice

### Blocks: Cosmetic & Building

- [ ] Candles (16 colors)
- [ ] Banners (TC specific)
- [ ] Nitor (8 colors, floating light)
- [ ] Metal Storage Blocks (Thaumium, Brass, Void)
- [ ] Translucent Glass (Beacon base, decorative)
- [ ] Paving Stones (Arcane, Barrier, Travel)
- [ ] Pillar Block (Directional decorative)
- [X] Planks & Logs (Greatwood, Silverwood) - **Doors/Wood types need redesign**
- [X] Arcane Stone Blocks (Bricks, Smooth, Pillars)
- [X] Eldritch Stone (+ Stairs, Slabs)
- [X] Ancient Stone (+ Tile, Stairs, Slabs)
- [X] Pedestals (Ancient, Eldritch, Arcane)
    - [ ] Stabilization
    - [ ] Viewmodels

### Items & Resources

- [ ] Thaumonomicon
- [X] Salis Mundus
- [X] Loot Bags (Common, Uncommon, Rare)
- [X] Vis Crystals (All 6 primals + Taint, tintable item)
- [X] Quartz Sliver (Catalyst for vis crystal crafting)
- [X] Metal Ingots (Brass, Thaumium, Void)
- [X] Metal Nuggets (Brass, Thaumium, Void, Quicksilver, Quartz)
- [X] Metal Plates (Brass, Iron, Thaumium, Void)
- [X] Raw Ores (Amber, Cinnabar, Quicksilver)
- [ ] Metal Clusters (Iron, Gold, Copper, Cinnabar - for purification)
- [ ] Void Seed
- [ ] Knowledge Fragments
- [ ] Celestial Notes
- [ ] Primordial Pearl
- [ ] Magic Dust
- [ ] Crystal Essence
