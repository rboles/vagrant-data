VagrantData

This project provides a tool that generates SQL from text data files.

The tool is named after Agent Callo Merlose, a member of the VKP's
Information Analysis Unit, a team specializing in the collection of
information, espionage, and analysis. Seemed fitting.

See ./bin for callo.jar

The /txt/*/*-combo.txt files are the data sources for all SQL

Script order

 - material-types.sql
 - armor-types.sql
 - weapon-types.sql

 - armor-items.sql
 - weapon-items.sql

 - armor-combos.sql
 - weapon-combos.sql

 - material-combos-armor.sql
 - material-combos-weapon.sql

ABOUT

I originally played Vagrant Story when it was released for the PS2 and
remembered really liking it. At the time, everything about the game
was a little unusual. During the summer of 2011 I downloaded it from
PSN to the PS3 and got right back into the game. If you are not
familiar with Vagrant Story, a major and novel game mechanic is that
you level your weapons and armor instead of your character. Items
become more powerful when dedicating them to specific monster
classes. Addionally you can deconstruct items and recombine the parts
to create more powerful items.

I had been playing with Scala since Spring. My biggest hurdle when
learning a new language is findig a project engaging enough to keep me
workign and learning. I thought it would be fun and instructive to
write a little tool to find game object combinations.

The project has 2 parts. First there is a tool called "Callo",
compiled into callo.jar. This tool parses text data and generates
SQL. Second there is a Lift application that provides the combination
discovery tool.
