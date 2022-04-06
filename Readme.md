# Description

This tool allows to find duplicates files in a specific folder. It is recursive. It then displays the results in a tree.
When selecting a tree node it allows you to compare the different files' path and size. Also, if the file is an image, it displays the image.

# Workflow

1. Select folder to index
2. Index folder
3. Show indexes

# Files

## Required folders

* config
* logs
* data

## config/config.properties

Entries:

* fm.config.index=data/indexes.json
* fm.config.data=*PATH_TO_FOLDER_TO_INDEX*
* fm.config.img.size=128