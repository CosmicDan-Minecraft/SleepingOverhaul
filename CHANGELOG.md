# 0.1.0

## Requirements:

* CosmicLib [0.2.0](https://github.com/CosmicDan-Minecraft/CosmicLib/releases/tag/0.2.0) or later
* Forge 1.12.2-14.23.2.2611

## Changes:

* Adds Rich-versions of ore for all detected ore blocks (scans the ore dictionary for entries beginning with "ore")
* Rich ore will drop multiple versions of their base ore when broken (the multiple amount is configurable, default x10)
* Rich ore hardness is a multiple of the base ore hardness (also configurable)
* Preferred Mod ID's can be configured, so particular-mod ores will be the rich ore base (I.e. what they will look like and drop)
* No world generation. Use COFH or something else yourself.
* All configuration made possible in Forge GUI 

