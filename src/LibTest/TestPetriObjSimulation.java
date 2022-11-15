/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LibTest;

//import PetriObj.PetriObjModel;

import LibNet.NetLibrary;
import PetriObj.*;

import java.util.ArrayList;


/**
 * @author Inna V. Stetsenko
 */
public class TestPetriObjSimulation {  //Результати співпадають з аналітичними обрахунками
    public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {

        for (int i = 0; i < 1; i++) {
            // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
            PetriObjModel model = getModel();
            model.setIsProtokol(false);
            double timeModeling = 1_000;
            model.go(timeModeling);

            // Цей фрагмент для виведення результатів моделювання на консоль
            for (PetriP P : model.getListObj().get(0).getNet().getListP()) {
                if (P.getName().startsWith("FreePorts") || P.getName().startsWith("Crane")) {
                    System.out.println("Place " + P.getName() + ": mean value = " + (1 - P.getMean()) + "\n"
                            + "         max value = " + Double.toString(1 - P.getObservedMin()) + "\n"
                            + "         min value = " + Double.toString(1 - P.getObservedMax()) + "\n");
                }
                if (P instanceof PetriPPortAvailability Port) {
                    System.out.println("Port processing time " + Port.getName());
                    System.out.println("time max value = " + Port.getMaxTime() + "\ntime average value = " + Port.getAverageTime() + "\ntime min value = " + Port.getMinTime() + "\n");
                }
            }
        }
    }

    public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetCoursework(100)));
        return new PetriObjModel(list);
    }

}
