# scalavators [![Build Status](https://travis-ci.org/abazylewicz/scalavators.svg?branch=master)](https://travis-ci.org/abazylewicz/scalavators)
Elevators controller in scala

To run tests: 

`./gradlew check`

Modelled elevator system has following characteristics:

- doesn't use predefined floors set - will handle dispatch to any floor
- every request has its pickup floor and destination - but destination is used only after dispatch. Elevator will add destination after it visits pickup floor
- pickups are 'instant'
- elevators does not have defined capacity

Possible improvements to model:
- introduce cost to picking up/leaving passengers 
- introduce elevator size 

# Dispatchers:

- Naive dispatcher
- shortest path dispatcher
