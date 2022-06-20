on run argv
	if (count of argv) > 3 then
		set pixelmatorPath to item 1 of argv
		set destinationPath to item 2 of argv
		set slug to item 3 of argv
		set theReplacementString to item 4 of argv
		set strLength to the length of theReplacementString
		set pixelmatorFile to POSIX file pixelmatorPath
		set exportLocation to POSIX file destinationPath
		set theSearchString to "TODO"
		tell application "Pixelmator Pro"
			set currentImage to open pixelmatorFile
			select every layer of the front document
			tell the front document
				select every layer
				if strLength > 45 then
					delete layer 1
					delay 0.2
					delete layer 1
				else
					delete layer 3
					delay 0.2
					delete layer 3
				end if
				replace text theSearchString with theReplacementString
			end tell
			export front document to file ((exportLocation as text) & slug & ".png") as PNG with properties {compression factor:100}
			close front document without saving
		end tell
	else
		display dialog "You need 4 arguments to generate a PNG. pxdPath folder slug title"
	end if
end run
