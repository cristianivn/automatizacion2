#include <Ultrasonic.h>

Ultrasonic ultrasonic(12,11); // (Trig PIN,Echo PIN)

void setup() {
  Serial.begin(9600); 
}

void loop()
{
  Serial.println(ultrasonic.Ranging(CM)); // CM or INC
 
  delay(100);
}
