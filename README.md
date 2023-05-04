# PromptMaker

Tool to make contributing to popsauce easy.

Outil qui devrait permettre de faciliter la création de questions pour popsauce.

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

## Usage
![example](https://user-images.githubusercontent.com/31979112/236230974-21cb9267-2520-412f-93e9-9a9e7bcdf518.png)
| Parameter       | Description                                                                                |
| :-------------- | :----------------------------------------------------------------------------------------- |
| Filename        | By default file(s) are named from the first answer you typed in the answer area.           |
| Custom filename | In most cases custom filename is unneeded. Example of use case is for prompts asking to complete lyrics or characters from movies/series where you have to set the source as filename. |
| Image selection | Copy and set the image name the same as the generated json.                                |
| Resize          | **\*\*experimental\*\*** it might be better to use other way to resize your image.         |
| Text content    | For text type prompt (i.e quotes, lyrics)                                                  |
| Answer(s)       | Answer(s) should be typed each on a **separate line**.                                     |
| Short(s)        | Shorthand(s) if added should be **separated by a comma** "**,**"                           |
| Automated tags  | Self explanatory. If new tags are ever added or some are missing for a specific language you can also add them to the text files located in the config folder. |
| Manual tags     | If there are missing tags, know that you can manually enter them. |

If no image has been selected, the program only generate a json file.

## Example output

Image prompt

```json
{
  "prompt": "What movie is this image from?",
  "text": null,
  "source": [
    "movie name"
  ],
  "shorthand": [],
  "details": "w/e fit your description (can let this field empty)",
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
  "prompt": "What movie is this quote from?",
  "text": "i.e random quote or song lyrics.",
  "source": [
    "w/e movie name"
  ],
  "shorthand": [],
  "details": "",
  "submitter": "test",
  "tags": [
    "Mainstream",
    "Medium",
    "Movies"
  ]
}
```

## Todo

- resizer (still a work in progress)
- add localization (maybe)

## Compiling

To compile the project yourself you will need to use modular jar for the used libraries. They have been added in the modular folder of the repo.

```bash
mvn clean javafx:jlink
```
