# Versus
I designed Versus to work as a 1v1 dueling plugin for _everyone_. To do so, I designed most features to be opt-in, minimizing complexity at the outset by enabling server owners to add features at their leisure. Likewise, the plugin avoids loud, intrusive GUI's whenever possible, supplementing that with simple, intuitive command design.

## Tutorials
### Arena Setup:
**[Video Tutorial](https://www.youtube.com/watch?v=GMRqdYudboM)**

First type /arena create <your_arena>. Clickable text will appear instructing you how to proceed. Follow each prompt to setup the arena. When you complete setup, you'll be forced to pick a kit that already exists. If you wish to add another, type /arena savekit <your_kit>. Then you can bind example_kit to your_arena with /arena set kit <your_arena> and the simple GUI that follows. 


## Commands:
### /arena
* **/arena create <arenaname>** Initiates the arena creation process.
* **/arena edit <arenaname>** Opens a menu to editing an existing arena, revealing all /arena set prompts available.
* **/arena visit <arenaname>** Teleport to an existing arena.
* **/arena savekit <name>** Saves a kit to kits.yml with a given name.
* **/arena deletekit <name>** Deletes kit of a given name.
* **/arena set <arenaname> <setting_name>** Sets an arena property to your current location
* **/arena loadkit <name>** Loads a kit into the player's inventory.
* **/arena list** Sends a list of all arenas and their locations.

### /duel
* **/duel <player>** Sends a duel request to <player> or accepts a player's request to duel if they've requested you already.
* **/duel deny <player>** Denies a request to duel from <player>. 
* **/duel accept** Accepts the most recent duel request.

### /spectate
* **/spectate <player>** Spectate <player>'s current duel.

## Permissions:
* **versus.duel** Allow access to /duel.
* **versus.spectate** Allow access to /spectate.
* **versus.arena** Allow access to /arena.
* **versus.arena.create** Allow access to /arena create.
* **versus.arena.delete** Allow access to /arena delete.
* **versus.arena.edit** Allow access to /arena edit.
* **versus.arena.list** Allow access to /arena list.
* **versus.bypass** Allow players to use any command while dueling.
