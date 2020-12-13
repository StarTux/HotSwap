# HotSwap

Replace parts of block materials.

The `/hotswap` command replaces parts of the material names of the
selected blocks. The idea is to replace only the color or the wood
type or the stone type or shape of a block while retaining the other
attributes which are encoded in the name or data tag.

## Command

The hotswap command takes one pattern and a replacement. The pattern
can specify whether to look at the beginning (`^` prefix) or the end
(`$` suffix) of the material name. The pattern is referred to as
`from`, the replacement as `to`.

- `/hotswap <from> <to>` Replace beginning of material name
- `/hotswap ^<from> <to>` Replace beginning of material name
- `/hotswap <from>$ <to>` Replace end of material name

## Dependencies

WorldEdit is required to select the region where the command should
operate. Furthermore, all actions will be on the WorldEdit `//undo`
stack.

## Examples

- `/hotswap ^green red`
  `green_wool => red_wool`
  `green_terracotta => red_terracotta`

- `/hotswab ^oak spruce`
  `oak_stairs[facing=south,half=bottom] => spruce_stairs[facing=south,half=bottom]`
  `oak_slab[type=bottom] => spruce_slab[type=bottom]`

- `/hotswab terracotta$ concrete`
  `red_terracotta => red_concrete`
  `white_terracotta => whtie_concrete`