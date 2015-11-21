# A Market Data aggregator simulated with rxJava

## Intro
This project simulates two market data price feeds (Reuters and Bloomberg). 

Each market data feed is implemented as a rx.functions.Func1 (see MarketDataProviderFunc)

Some prices can come from both Reuters and Bloomberg; some are exclusive to each.

MarketDataSource aggregates prices from Reuters and Bloomberg.

## Delays
When we receive a price update from Reuters or Bloomberg, we simulate a one second delay to make things interesting

## Pre-Reqs
Java 8 

Gradle (to build from source)

## How to run
gradle run

tail/open prices.txt file 

## Overview
App - the main application

MarketDataProviderFunc 	- models Reuters and Bloomberg by creating random prices based on an instrument list

MarketDataSource 		- listens and stores the latest prices from Reuters/Bloomberg.
The observer model is used to simulate callbacks and is based on RxJava/reactive.io.
https://github.com/ReactiveX/RxJava
http://reactivex.io/

Prices - aggregates the prices coming from both providers and handles file i/o.

