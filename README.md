
How to run Ants simulation:

    - Navigate to root folder
    
    - Create .class files
    
        - 'javac langtonsAnts/*.java'
        
    - execute command 'java langtonsAnts/Ants {height} {width} {pixel-size} {step-speed (miliseconds)} {pattern} {multiple-ants}'
    
        - ex; 'java langtonsAnts/Ants 120 160 5 3 RL false'
        
    - for multiple ants
    
        - execute command 'java langtonsAnts/Ants {height} {width} {pixel-size} {step-speed (miliseconds)} {pattern} {multiple-ants} {number-ants}'
        
            - ex; 'java langtonsAnts/Ants 120 160 5 3 RL true 3'
            

How to run Life simulation:

    - Navigate to root folder
    
    - Create .class files
    
        - 'javac gameOfLife/Life.java'
        
    - execute command 'java gameOfLife/Life {height} {width} {cell-size} {delay}
    
        - ex; 'java gameOfLife/Life 70 30 10 200'
        
    - How to use:
    
        - Click cells to toggle life (black alive / white dead)
        
        - Keyboard input:
        
            - 's' start simulating generations
            
            - 'g' generate random life
            
            - 'c' clear all cells
            
            - '-' decrease delay speed
            
            - '=' increase delay speed
            