# BeanBoyBot

-------------------------------------------------
Copyright 2018 Benjamin Massey
-------------------------------------------------

## Summary

This is a bot for streams on Twitch.tv that runs a game integrated with the streamer's speeedrun.

Here's a rundown of the basics of the game:

All throughout a given run, that run will have a certain price associated with it.

This price is gathered through a calculation on the quality of the run -
if the run is going well, the price will go up, and vice versa.

At any point in the run, you can either buy or sell the run at the given price.

The best way to think of this is like a stock - you can either invest in the run or not.

Just like a stock, you're gonna want to buy low and sell high as best you can.

However, and here is where the major "game" portion comes in,
there are two conditions where the run will auto sell to every investor:
either a reset or a personal best.

If the runner achieves a personal best, then congrats, you invested into a successful run!

You'll be awarded double the price at the end if it is a PB: should be a hefty sum

Much more likely, an investor will run into a reset (such is life).

On a reset, the run will be sold out from under the investors for only 75% of the current price.

This makes holding onto a run risky, and forces the viewers to pay attention and try to predict futures based on their observations.

There are some other smaller parts of the game,
such as dividends which encourage investors to stay invested,
but that is the basic concept of the game.

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
- And many more random things I've done manually...


## Quotes

While I probably should have separated it a bit, this bot build also includes a pretty standard system for storing quotes.

Anyone can add a quote with !addquote XX, view a random quote with !quote or a specific one with !quote XX.

The quotes are very basically handled - just plain text files. They're backed up every couple of quotes.

Feel free to ignore this part of the bot if you want, and it will probably be separated in the future.

## Code

First thing to make clear: this is very much still a work in progress, and you should not expect it to be easy to use or smooth or even really fully working.
I have not yet pushed any real "release" version, and as far as I know I'm the only one to use it on stream ever.
I do my best to keep everything safe and tidy, but use at your own risk and sanity.

This project is all written in Java, using Pircbot as base:
http://www.jibble.org/pircbot.php

Make sure to check out pircbot, it makes a lot of the silly Twitch interaction a whole lot easier.

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

Shoutouts to my various viewers for helping test the bot a lot, which admittedly has had MANY problems over its lifetime - they've got great ideas too <3.

Shoutouts to Clint Stevens for sparking this idea in my head, even though it was mainly his chat and it was just a barely related meme <3.