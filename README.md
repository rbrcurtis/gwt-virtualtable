gwt-virtualtable
================

A VTT written in GWT targeting the pathfinder RPG system.

I think I started this project late in 2009 or thereabouts (although I didn't start using git until 2011).  It is a realtime RPG virtual table meaning that it 
is a "table" or map that you can play DND with friends on the internet where you each control a peice. 
All moves of pieces or squares are seen in realtime by all connected players looking at the same map and there is full map editing capability.
The backend was originally in mysql but was transitioned to mongodb at some point.
I implemented it using long polling because at the time websockets were a fantasy in development with no browser support.  
Jetty 6 had pretty awesome longpolling support that doesn't tie up threads using continuations so that was cutting edge at the time.

I worked on the project off and on and in 2011 decided to target the pathfinder system specifically, and brought a 
few friends on to help with creating a setting and doing art.  All the code is mine, however.

In 2012 I discontinued this when I learned that pathfinder was making their own VTT (supposedly; where is it paizo?). 
The last commits were for the beginnings of a pretty involved character editor.

Given that this was started in 2009 before I really had any clue about javascript and client-side rendering, 
please go easy on me when judging the code.

All code here is MIT license.  Please feel free to use it in any way you like, just follow that license and let me know you're
using it because I'd find that pretty cool.
