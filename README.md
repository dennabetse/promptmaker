# PromptMaker

Tool to make contributing to popsauce easy.

Outil permettant de faciliter la création de questions pour popsauce.

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
![image](https://github.com/esteb4nned/PromptMaker/assets/31979112/02e953a8-ae49-4462-a74c-fc349566f221)
| Parameter       | Description                                                                                |
| :-------------- | :----------------------------------------------------------------------------------------- |
| Filename        | By default file(s) are named from the first answer you typed in the answer area.           |
| Custom filename | In most cases custom filename is unneeded. Example of use case is for prompts asking to complete lyrics or characters from movies/series where you have to set the source as filename. |
| Image selection | Copy and set the image name the same as the generated json.                                |
| Resize          | Resize the image between 1024x1024 and 512x512.                                            |
| Text content    | For text type prompt (i.e quotes, lyrics)                                                  |
| Answer(s)       | Answer(s) must be typed each on a **separate line**.                                       |
| Short(s)        | Shorthand(s) if there are multiple should be **separated by a comma** "**,**"              |
| Tag editor      | If tags are missing for automated input you can edit them or choose to use manual input.   |

If no image has been selected, the program only generate a json file.

## Example output

Image prompt

```json
{
  "prompt": "What movie is this image from?",
  "text": null,
  "source": [
    "answer",
    "answer",
    "answer"
  ],
  "shorthand": [],
  "details": "",
  "submitter": "test",
  "tags": [
    "Mainstream",
    "Easy",
    "Images",
    "Movies"
  ]
}
```

Text prompt
```json
{
  "prompt": "What book is this quote from?",
  "text": "submit random quote",
  "source": [
    "answer"
  ],
  "shorthand": [],
  "details": "",
  "submitter": "test",
  "tags": [
    "Mainstream",
    "Easy",
    "Literature",
    "Texts"
  ]
}
```

## Compiling

To compile the project by yourself you need the dependencies used to be modular. I provide the one I made in the modular folder.

```bash
mvn clean javafx:jlink
```
