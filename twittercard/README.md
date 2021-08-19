This folder contains an [AppleScript](https://developer.apple.com/library/archive/documentation/AppleScript/Conceptual/AppleScriptLangGuide/introduction/ASLR_intro.html) script. You can use it to generate an image. You may use the image as a [Twitter Card](https://developer.twitter.com/en/docs/twitter-for-websites/cards/overview/abouts-cards) large image. It uses a template built with [Pixelmator Pro](https://www.pixelmator.com/pro/). [Pixelmator supports automation via AppleScript](https://www.pixelmator.com/tutorials/resources/advanced-automation-and-scripting-with-applescript/).

To execute the script, you first need to compile it: 

```bash
osacompile -l AppleScript -d -o micronautwittercard.scpt micronautwittercard.applescript
```

The script takes 4 parameters:

- A path to a PXD file with a text layer which contains the word "TODO". 
- The second parameter script is the folder where the generated image is saved.
- The third parameter is the guide's slug. The script uses it as part of the filename of the output file.
- The scripts opens the Pixelmator Pro file and it replaces it with the value of the fourth parameter.  

Script execution example: 

```
osascript micronautwittercard.scpt ~/github/micronaut-projects/micronaut-guides/twittercard/MicronautTwitterCard.pxd ~/github/micronaut-projects/micronaut-guides/src/docs/images/cards micronaut-security-jwt-cookie "Micronaut JWT authentication via Cookies"
```


