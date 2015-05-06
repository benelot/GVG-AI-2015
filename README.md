# GVG-AI-2015
Watch out other contestants! INI participates at this year's GVG-AI competition and we are going to win! With the least amount of effort, because that is what we mainly optimize for.

If you are new to this group and you are wondering what we did up to now, here is our schedule:
* [Past Meetings and Future Schedule](https://github.com/benelot/GVG-AI-2015/wiki/Past-Meetings-and-Future-Schedule)


--------

## Setup:

To set up a GVG-AI Development environment on your machine, refer to the following article in our wiki:

* [Setup GVG-AI Development Environment](https://github.com/underworldguardian/GVG-AI-2015/wiki/Setup-GVG-AI-Development-Environment)


## Tutorials:

Once you have everything installed and working according to the setup above, let's get started.  Head over to the wiki and proceed with our tutorials:
* [Tutorial 1 - Choosing a Movement](https://github.com/benelot/GVG-AI-2015/wiki/Tutorial-1%3A-Choosing-a-Movement)
* [Tutorial 2 - Reinforcement learning] (https://github.com/benelot/GVG-AI-2015/wiki/Tutorial-2%3A-Reinforcement-Learning)


You are always welcome to improve the article and add new ones (that is the idea of a wiki compared to a non-editable documentation). 



## Projects

#### Project 1: Implementing Deepmind
We implement the controller written by Deepmind in Java for the GVG-AI Task. Checkout the awesome presentation by Daniel Neil about the paper ["Human-level control through deep reinforcement learning"](https://github.com/benelot/GVG-AI-2015/wiki/Past-Meetings-and-Future-Schedule#20150416-deepminds-human-level-control-through-deep-reinforcement-learning).


* Input and Preprocessing
    * Interface to the game simulator
    * Represent game state as simplified image
* DQN Network without the deep part
    * Setup epsilon-greedy framework, replay memory, and Q-
target copying
    * Build generalized DQN without the q-estimator part
* ConvNet Function Approximation
    * Set up a simple interface to use the convnet to estimate the
rewards

We work in teams of the following members:

| Team 1 Input:  | Team 2 DQN:  | Team 3 DCN:  |
|---|---|---|
Daniel Neil | Diana Ponce | Diana Ponce |
Thanuja Ambegoda | Benjamin Ellenberger | Benjamin Ellenberger |
Alpha Renner | Jakob Buhmann | Phillip Kainz |
| | Benjamin Ehret | Gagan Narula |
| | Nikola Nikolov | Sepp Kollmorgen |
| | Daniel Renz | Daniel Renz |
| | Sepp Kollmorgen |
| |Joachim Ott |
| |Peter Diehl |
