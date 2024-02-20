# Clean Code Documentation

## Dependency Inversion
The dependency Inversion principle has been used to ensure, that the controllers do not depend upon the models. Each Class inside the models package implements one or multiple abstractions defined at the controllers package

## Interface Seggregation
The many interfaces used by the classes inside the controllers package are seperated in such a way that each controller is able to use client specific interfaces and is not able to see any methods which it does not need. 

## Low Coupeling/Single Responsibility 
The Classes inside the models package e.g. the Factories are not interconnected in any way, therefore reducing coupeling and enabeling change and/or modification of any class. The object produced by a factory is always an inner class of said factory.

The controllers are completely independet from each other and each controller strives to serve a specific task (e.g.Log In procedure,Profile Page...), for specific views, although some coupeling was deemed necessary in order to reduce duplication of code. For example the Inventory Controller is responsible of adding to and removing from an Users inventory (jokers and coins in our case) which is a functionality needed for buying jokers on the shop page as well as spending them on the quiz page.