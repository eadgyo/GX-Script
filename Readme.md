# GX-SCRIPT
![GX-Script](https://raw.githubusercontent.com/eadgyo/GX-Script-Editor/master/img/example.PNG)

## Information
GX-Script is a visual language made to create algorithm. This type of language is suitable for non developer, desiring to make simple algorithm. This language is designed to be used with [GX-Script-Editor](https://github.com/eadgyo/GX-Script-Editor).

## Creating a GX-Script
[GX-Script-Editor](https://github.com/eadgyo/GX-Script-Editor) was made to create, edit, save and launch a Visual GX-Script.


## Creating a visual entity
You can create and add your own Visual entity (box).
Two methods are available
#### 1. Create a JAVA entity
Create a new java project, with a new class for our entity:
```java
public class MyEntity extends DefaultVariableGXEntity {
    
    // You can add input and output dynamically
    public MyEntity() {
        super("MyEntity");
        
        this.addInputEntry("MandatoryInput", Number.class);
        this.addInputEntryNotNeeded("Next", Void.class);
        
        this.addOutputEntry("ResultOutput", Number.class);
        this.addOutputEntry("Continue", Void.class);
    }
    
    // Create the algorithm
    public Func getFunc() {
       return new Func() {
           public void run(Program program) {
               Object[] objects = program.loadCurrentParametersObjects();
               
               // Get the first input
               Number mandatoryInput = (Number)objects[0];
               
               // Get output addresses
               FuncImbricationDataAddresses funcParameters = program.getCurrentFuncImbricationParameters();
               FuncAddress successAddress = funcParameters.getImbricationAddress(0);
               FuncAddress continueAddress = funcParameters.getImbricationAddress(1);
               
               // Run our algorithm
               if (mandatoryInput > 10) {
                   program.pushInMemory(0);
               } else {
                   program.pushInMemory(mandatoryInput + 1);
               }
                
               // Set the program to the next entity output address
               program.setNextFuncAddress(continueAddress);
           }
       }; 
    }
}
```
To add this entity you must create a EGX file:
```java
EGX egx = new EGX();
EGXGroup booleanGroup = new EGXGroup("MyGroup");
IOGXManager.getInstance().saveEGX("MyEntity.egx", egx);
```
This file can then be imported from the [GX-Script-Editor](https://github.com/eadgyo/GX-Script-Editor),
by clicking on ```File/Import entity```.

#### 2. Create a GX-Entity embedding GX-Script
You can use GX-Script editor to create a GX-Entity, steps are described here:
[GX-Script-Editor](https://github.com/eadgyo/GX-Script-Editor)


