# BeanBoyBot

-------------------------------------------------
Copyright 2019 Benjamin Massey
-------------------------------------------------

## Summary

This is a bot for streams on Twitch.tv that runs a game integrated with the streamer's speeedrun.

Here's a rundown of the basics of the game:

- All throughout a given run, that run will have a certain price associated with it.
This price is gathered through a calculation on the quality of the run -
if the run is going well, the price will go up, and vice versa.

- At any point in the run, a viewer can either buy or sell the run at the given price.

- There are also two conditions where the run will auto sell to every investor.

- If the runner achieves a personal best, then the run will be auto sold at 2x its ending price.

- If the runner does a reset, then the run will be auto sold for 75% of its current price.

This creates a game of the viewer treating the game like a stock, and paying attention to the runs in order to dodge resets and maximize profit.

## Viewer Interaction

The whole point of this is to increase viewer interaction, and of course you need to reward that interaction with incentives.
Every user starts out with 100 points, and can build up points through buying and selling.
Those points can then be used for certain rewards.

Some rewards I've experimented with, and are currently available using this bot:

- Buying a message to put on screen
- Buying an emote to put on screen
- Leader board of top point holders
- Ability to "flex" with a chat message
- Buying a background image for a green screen
- Buying an image to put on stream

## Code

This project is currently written entirely in Java.
It has been written mostly by me, with some additions as seen in the "Thank Yous" section.

This project uses Pircbot as base:
http://www.jibble.org/pircbot.php
Make sure to check it out, it makes a lot of the silly Twitch interaction a whole lot easier.

You're going to need to have your own account setup to act as the Twitch bot, and figure out how to get an API key and whatnot for it.
Google should make all that fairly easy.

## Images

Image of the bot's interface in non config mode:

![Image of the bot's interface in non config mode](https://i.imgur.com/WCEBmxZ.png)

Image of the bot's interface in config mode:

![Image of the bot's interface in config mode](https://i.imgur.com/liPUP0r.png)

Image of the bot's output for a stream:

![Image of the bot's output for a stream](https://i.imgur.com/cv8FBom.png)

Full screenshot of bot on stream:

![Full screenshot of bot on stream](https://i.imgur.com/yNPHfvO.jpg)

## Contact

Any and all questions should be directed to benjamin.w.massey@gmail.com - I'd love to talk about my silly little bot.

I also stream at https://www.twitch.tv/BeanSSBM and use this bot when I speedrun - feel free to just hop in and ask questions, or send me a whisper.

## Thank Yous

Shoutout to Dark_Tenka for helping out with certain parts of the bot, including the majority of the dividends system - I really appreciate his help <3.

Shoutouts to my various viewers for helping test the bot - they've got some great ideas <3.

Shoutouts to Clint Stevens for sparking this idea in my head, even though it was mainly his chat and it was just a barely related meme <3.