import os, os.path
import sys
import time

lastTime = 0
lastNumFiles = 0

while True:
	texturesChanged = False
	
	numFiles = 0
	for root, dirs, files in os.walk("textures"):
		for f in files:
			numFiles = numFiles + 1

			fullPath = os.path.join(root, f)
			newTime = os.path.getmtime(fullPath)
			if newTime > lastTime:
				lastTime = newTime
				texturesChanged = True
	
	if texturesChanged or numFiles != lastNumFiles:
		lastNumFiles = numFiles
		print("packing textures")
		if sys.platform == 'win32':
			os.system("java -cp libs/gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 textures assets/")
		else:
			os.system("java -cp libs/gdx.jar:gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 textures assets/")
	
	time.sleep(1)