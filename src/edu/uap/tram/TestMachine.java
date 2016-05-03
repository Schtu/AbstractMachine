package edu.uap.tram;


public class TestMachine {

	public static void main(String[] args) {
		
		/***********************************************
		 * Sample Programs
		 ***********************************************/

		/**
		 * Quellkode: y = x*3+5*2 
		 * Annahmen: Variable x durch Kellerzelle 0 und Variable y durch Kellerzelle 1 implementiert, 
		 *           sowie PP=0, FP=0 und TOP=1.
		 */
		Instruction[] program1 = new Instruction[] {
				new Instruction(Instruction.CONST, 6), // value for x
				new Instruction(Instruction.STORE, 0, 0), // store x
				new Instruction(Instruction.LOAD, 0, 0), 
				new Instruction(Instruction.CONST, 3),
				new Instruction(Instruction.MUL), 
				new Instruction(Instruction.CONST, 5),
				new Instruction(Instruction.CONST, 2), 
				new Instruction(Instruction.MUL),
				new Instruction(Instruction.ADD), 
				new Instruction(Instruction.STORE, 1, 0),
				new Instruction(Instruction.HALT) };

		/**
		 * Quellkode: x=10; if(x == 0) 100 else 200; 3 
		 * Annahmen: Variable x durch Kellerzelle 0 implementiert, sowie PP=0, FP=0 und TOP=0.
		 */
		Instruction[] program2 = new Instruction[] { 
				new Instruction(Instruction.CONST, 10),
				new Instruction(Instruction.STORE, 0, 0),
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.IFZERO, 6), // --> iftrue
				new Instruction(Instruction.CONST, 200),
				new Instruction(Instruction.GOTO, 7), // --> goto
				// iftrue
				new Instruction(Instruction.CONST, 100),
				// goto
				new Instruction(Instruction.NOP), 
				new Instruction(Instruction.CONST, 3),
				new Instruction(Instruction.HALT) };

		/**
		 * Quellkode: let square(x) { x*x } 
		 *            in square(10) 
		 * Annahmen: Das Argument von square wird durch Kellerzelle 0 repraesentiert, sowie PP=0, FP=0 
		 *           und TOP=-1
		 */
		Instruction[] program3 = new Instruction[] {
				new Instruction(Instruction.CONST, 10),
				new Instruction(Instruction.INVOKE, 1, 3, 0), // --> square
				// return
				new Instruction(Instruction.HALT),
				// square
				new Instruction(Instruction.LOAD, 0, 0), 
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.MUL), 
				new Instruction(Instruction.RETURN) // --> return
		};

		/**
		 * Quellkode: let wrapper(number, threshold) { 
		 *                  let square(x) { 
		 *                        if (x*x > threshold) x 
		 *                        else x*x 
		 *                      }
		 *                  in square(number) 
		 *                } 
		 *            in wrapper(4, 10) 
		 * Annahmen: Die Argumente von wrapper werden durch die Kellerzellen 0 und 1 repraesentiert, 
		 *           sowie PP=0, FP=0 und TOP=-1
		 */
		Instruction[] program4 = new Instruction[] {
				new Instruction(Instruction.CONST, 4),
				new Instruction(Instruction.CONST, 10),
				new Instruction(Instruction.INVOKE, 2, 4, 0), // --> wrapper
				// return wrapper
				new Instruction(Instruction.HALT),
				// wrapper
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.INVOKE, 1, 7, 0), // --> square
				// return square
				new Instruction(Instruction.RETURN),
				// square
				new Instruction(Instruction.LOAD, 0, 0), 
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.MUL), 
				new Instruction(Instruction.LOAD, 1, 1),
				new Instruction(Instruction.GT), 
				new Instruction(Instruction.IFZERO, 15),
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.RETURN), // --> return square
				new Instruction(Instruction.LOAD, 0, 0), 
				new Instruction(Instruction.LOAD, 0, 0),
				new Instruction(Instruction.MUL), 
				new Instruction(Instruction.RETURN) // --> return square
		};
		
		//Aufgabe 3: FAKULTÄT
		
		int fak_von=3; // Zu berechnende Fakultät
		
		Instruction[] fak = new Instruction[] {
					//INITIALISIERUNG
					new Instruction(Instruction.CONST, 0), // Belege Stack mit 0, dient zum als Speicherzelle
					new Instruction(Instruction.CONST, fak_von), // Belege Stack mit Fakultätswert
					new Instruction(Instruction.IFZERO,16), // Wenn fak_von = 0 springe zum Halt-Block
					new Instruction(Instruction.CONST, fak_von), //IFZERO entfernt den obersten Wert, fak neu laden
					new Instruction(Instruction.STORE,0,0), //Speichere fak auf der untersten Stelle
					new Instruction(Instruction.LOAD,0,0), // Lade Kopie an oberste Stelle
					new Instruction(Instruction.STORE,1,0), // Speichere Kopie an 2ter Stelle
					//LOOP
					new Instruction(Instruction.LOAD,0,0), // Lade aktuellen fak_Wert an oberste Stelle
					new Instruction(Instruction.CONST,1), // Lade 1 an oberste Stelle für Subtraktion
					new Instruction(Instruction.SUB), // fak_Wert - 1
					new Instruction(Instruction.LOAD,2,0), // Lade neuen fak_Wert an oberste Stelle für die Speicherung
					new Instruction(Instruction.STORE,0,0), // Speichere
					new Instruction(Instruction.IFZERO,16), // Prüfe auf 0, wenn ja, springe zu HALT
					new Instruction(Instruction.LOAD,0,0), // Neu laden wegen IFZERO
					new Instruction(Instruction.MUL), // Multipliziere "alten" fak_Wert mit fak_Wert-1
					new Instruction(Instruction.GOTO,7), // WDH bis fak_Wert = 0
					//HALT
					new Instruction(Instruction.STORE,0,0), // Speichere Ergebnis an erster Stackzelle
					new Instruction(Instruction.HALT) // Beende Instruktion
			};
		
		
		
		
		//AUFRUF
		
		
		AbstractMachine m = new AbstractMachine();
		
		//Werte sind je nach Instruktion anzupassen
		
		m.TOP=0;
		m.PP=0;
		m.FP=0;
		m.PC=0;
		m.stack.push(0); //Vorinitialisierung des Stacks
		
		//Instruktionsname/Debugmode
		
		m.run(fak, true);
		
	}
	
}
