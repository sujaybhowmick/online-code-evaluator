// should give an ncss of 35

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


//should give an ncss of 22
public class JavaNCSSCheckTestInput {
    
    private Object mObject;
    
    //should count as 2
    private void testMethod1() {
        
        //should count as 1
        int x = 1, y = 2;
    }
    
    //should count as 4
    private void testMethod2() {
        
        int abc = 0;
        
        //should count as 2
        testLabel: abc = 1;  
    }    
     
    //should give an ncss of 12
    private void testMethod3() {
        
        int a = 0;
        switch (a) {
            case 1: //falls through
            case 2: System.out.println("Hello"); break;
            default: break;
        }
    }
    
    //should give an ncss of 2
    private class TestInnerClass {
        
        private Object test;
    } 
}

