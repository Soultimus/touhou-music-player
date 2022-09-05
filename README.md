# Touhou Music Player
This application reads PCM and FMT data found in Touhou Project games after Perfect Cherry Blossom and plays their songs

## About
With every Touhou game you install, a `thbgm.dat` file is found in the main directory. 

I was always curious to know how the games play the soundtrack from that file, and it made me find [this article](https://en.touhouwiki.net/wiki/Technical_Information/BGM) on the Touhou Wiki explaining the specifics. And so I attempted to code an application that would read and play from that file. At the time of writing this, Java is the only language I know well enough to achieve this. I'm sure there are better ways I could've done it, or even exisiting apps faster and better than mine which do the same, but whatever

## Some setting up
Before using, please install the [Brightmoon](https://mits203.tistory.com/entry/%EC%B6%94%EC%B6%9C-%EB%8F%99%EB%B0%A9%EC%8B%9C%EB%A6%AC%EC%A6%88-dat-%ED%8C%8C%EC%9D%BC-%EC%B6%94%EC%B6%9C-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-brightmoon) application. This will help my app read which song it needs to read.

Please also make sure you have Java installed on your computer

### Step 1
Go to the main directory of game. If you have some of the games on Steam and don't know where they are located:
1. Go into your game library
2. Right click the game
3. Select `Properties`
4. On the popup, select `Local Files`
5. Click on `Browse...`

### Step 2
Open the Brightmoon tool. Select `File` -> `Open` (or do `CTRL+O`) and go to the game directory. Select the `thXX.dat` file (XX being the game's number, so if you were doing this for Mountain of Faith, you would see `th10.dat`)

### Step 3
Using the tool, locate the `thbgm.fmt` file. Click on it, then do `File` -> `Extract` (or press `F5`). Saving the file is a hassle, so I suggest you save it on your Desktop, then drag it to the game's directory

### Step 4
Open the application. Use the `Set Directory` button to save where the game directory you've been using so far is.

## How to use
Now that you've set up all of that, you can use the application!

The last soundtrack you were listening to is saved to a file, so on startup it will load said last soundtrack.

On the left, you will see all the songs from the game you've selected and setup. Just press one of the song titles, and it will play!

Please note that the songs play **indefinitely**. There's no pausing or playing, or skiping to a next song. All you can do is stop the song by pressing the `■` button, or change to another song by pressing another title.

The game selection box on the right can be also be controlled with the arrow keys. Just press your `↑` or `↓` keys to go up or down a game.

Have fun listening! I'm quite proud that this is my first personal project, so I hope you enjoy it somewhat despite how basic it is.

## Special thanks

- My friend ***NeNikitov***, for helping me code the `MusicFMTData` file and explaining to me how to read bytes and make use of them. Check his awesome stuff out [here](https://github.com/nenikitov)
- ***Michael Beeuwsaert***, for creating a [DataInputStream file with Endian support](https://gist.github.com/MichaelBeeu/6545110). It greatly helped with byte reading
- ***The Touhou Wiki***, for their article on Touhou BGM files
- **ZUN**, for creating Touhou Project and making music so damn good it made me want to code an entire app on my own accord just to play it