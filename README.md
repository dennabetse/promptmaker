# PromptMaker

Tool to help on making contributing to popsauce easy and fast.

## Installation

Extract the zip

To start the program, in the bin folder

On Windows:

```
launcher.bat
```
On Mac/Linux:

In terminal
```
launcher
```

## How to use
![example](https://github.com/dennabetse/promptmaker/blob/main/example/example.png)

| Parameter       | Description                                                                                |
| :-------------- | :----------------------------------------------------------------------------------------- |
| filename        | By default file(s) are named from the first answer you typed in the answer area.              |
| custom filename | In most cases custom filename is unneeded but is a special case i.e for prompts asking to complete lyrics or characters from movies/series where you have to set the filename from their sources.                                                               |
| Image selection | Copy and set the image name the same as the generated json.                                |
| Resize          | **\*\*experimental\*\*** it might be better to use other way to resize your image.         |
| Text content    | For text type prompt (i.e quotes, lyrics)                                                  |
| Answers         | Answer(s) should be typed each on a **separate line**.                                     |
| Shorts          | shorthand if added should be **separated by a comma** "**,**"                              |
| Manual tags     | If new tags are ever added or some are missing, you can manually enter them.               |

## Sample output

Image prompt

```json
{
  "prompt": "What movie is this image from?",
  "text": null,
  "source": [
    "The King",
    "Le Roi"
  ],
  "shorthand": [],
  "details": "w/e fit your description",
  "submitter": "test",
  "tags": [
    "Mainstream",
    "Easy",
    "Movies"
  ]
}
```

Text prompt
```json
{
  "prompt": "What movie is this taken from?",
  "text": "i.e random quote or song lyrics",
  "source": [
    "w/e movie name"
  ],
  "shorthand": [],
  "details": "w/e fit your description",
  "submitter": "test",
  "tags": [
    "Mainstream",
    "Medium",
    "Movies"
  ]
}
```

## Compiling

To compile the project yourself you will need to use modular jar for the used libraries. They have been added in the modular folder of the repo.

```bash
mvn clean javafx:jlink
```
