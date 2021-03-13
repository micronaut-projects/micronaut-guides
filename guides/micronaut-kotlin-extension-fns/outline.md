1. Describe kotlin extension functions
    - What are they?
    - Why are they useful?
    - How do you write them?
        - Note package level functions
    
2. Introduce the kotlin extension functions we have
    - Shout out Alejandro for the contribution
    - Point at the docs TODO fix them (modify gh-action, finalize dokka comments)
    - Call special attn to the ones we'll use: startApp, retrieveObject, scheduleCallable

3. Write our app
    - Consume the `dadjoke` API w/ a client (https://icanhazdadjoke.com/api)
      - Maybe show a test and ext fn for a test? if I can think of one
    - Schedule a "message" (fake, just log it) to "text your kids" the joke
    - Write our own ext fn
      - Maybe basic client just has random joke, we add something to utilize the keyword search
    - Make a controller to put it all together 
    - Show the `startApplication` function to put it all together
    - Enjoy the jokes
    
