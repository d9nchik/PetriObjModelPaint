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

        // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
        PetriObjModel model = getModel();
        model.setIsProtokol(false);
        double timeModeling = 1000;
        model.go(timeModeling);

//        //Цей фрагмент для виведення результатів моделювання на консоль
//        System.out.println("Mean value of queue");
//        for (int j = 1; j < model.getListObj().size(); j++) {
//            System.out.println(model.getListObj().get(0).getNet().getListP()[0].getMean());
//        }
//        System.out.println("Mean value of channel worked");
//        for (int j = 1; j < model.getListObj().size(); j++) {
//            System.out.println(1.0 - model.getListObj().get(0).getNet().getListP()[1].getMean());
//        }
//        System.out.println(2.0 - model.getListObj().get(0).getNet().getListP()[1].getMean());
//
//        System.out.println("Estimation precision");
//        double[] valuesQueue = {1.786, 0.003, 0.004, 0.00001};
//
//        System.out.println(" Mean value of queue  precision: ");
//        for (int j = 1; j < 5; j++) {
//            double inaccuracy = (model.getListObj().get(j).getNet().getListP()[0].getMean() - valuesQueue[j - 1]) / valuesQueue[j - 1] * 100;
//            inaccuracy = Math.abs(inaccuracy);
//            System.out.println(inaccuracy + " %");
//        }
//
//        double[] valuesChannel = {0.714, 0.054, 0.062, 0.036};
//
//        System.out.println(" Mean value of channel worked  precision: ");
//
//        for (int j = 1; j < 4; j++) {
//            double inaccuracy = (1.0 - model.getListObj().get(j).getNet().getListP()[1].getMean() - valuesChannel[j - 1]) / valuesChannel[j - 1] * 100;
//            inaccuracy = Math.abs(inaccuracy);
//
//            System.out.println(inaccuracy + " %");
//        }
//        double inaccuracy = (2.0 - model.getListObj().get(4).getNet().getListP()[1].getMean() - valuesChannel[3]) / valuesChannel[3] * 100;
//        inaccuracy = Math.abs(inaccuracy);
//
//        System.out.println(inaccuracy + " %");

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


//       for(PetriSim e: model.getListObj()){
//              e.printMark();
//
//
//         }
//         for(PetriSim e: model.getListObj()){
//              e.printBuffer();
//         }


    }

    // метод для конструювання моделі масового обслуговування з 4 СМО

    public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetCoursework6()));
        return new PetriObjModel(list);
    }

}
