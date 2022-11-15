/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LibTest;

//import PetriObj.PetriObjModel;

import LibNet.NetLibrary;
import PetriObj.*;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * @author Inna V. Stetsenko
 */
public class TestPetriObjSimulation {  //Результати співпадають з аналітичними обрахунками
    public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {

        // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
        int processors = 6;


        double[] incomeTimes = new double[]{0.25, 2.5, 25, 250};
        double[] processTimes = new double[]{0.1, 1, 10, 100};
        double[] moveTimes = new double[]{ 1, 10, 100};

        System.out.printf("%s\t%s\t%s\t", "income", "process", "move");

        for (int i = 0; i < processors; i++) {
            System.out.printf("%s\t", "P" + i);
        }
        for (int i = 0; i < processors; i++) {
            System.out.printf("%s\t", "C" + i);
        }
        System.out.printf("%s\n", "D");
        DecimalFormat format = new DecimalFormat("0.#");

        for (double incomeTime: incomeTimes) {
            for(double processTime : processTimes){
                for (double moveTime: moveTimes){
                    System.out.printf("%s\t%s\t%s\t", format.format(incomeTime), format.format(processTime), format.format(moveTime));
                    PetriObjModel model = getModel(processors, incomeTime, processTime, moveTime);
                    model.setIsProtokol(false);
                    double timeModeling = 1_000;
                    model.go(timeModeling);


                    for (int i = 0; i < processors; i++) {
                        PetriNet petriNet = model.getListObj().get(i + 1).getNet();
                        // Processor load
                        System.out.printf("%s\t", format.format(petriNet.getListT()[0].getMean()));
                    }
                    for (int i = 0; i < processors; i++) {
                        PetriNet petriNet = model.getListObj().get(i + 1).getNet();
                        // Convejer
                        System.out.printf("%s\t", format.format(petriNet.getListT()[1].getMean()));
                    }
                    System.out.printf("%s\n", format.format(model.getListObj().get(1).getNet().getListP()[0].getObservedMax()));
                }
            }
        }
    }

    // метод для конструювання моделі масового обслуговування з 4 СМО

    public static PetriObjModel getModel(int processors, double incomeTime, double processTime, double moveTime) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();

        list.add(new PetriSim(NetLibrary.CreateNetTask2DetailsIncome(incomeTime)));


        for (int i = 0; i < processors; i++) {
            PetriNet petriNet = NetLibrary.CreateNetTask2Device(i + 1, moveTime, processTime);
            list.add(new PetriSim(petriNet));
        }

        PetriP income = list.get(0).getNet().getListP()[1];
        PetriP processedDetails = list.get(1).getNet().getListP()[1];

        for (int i = 0; i < processors; i++) {
            list.get(i + 1).getNet().getListP()[0] = income;
            list.get(i + 1).getNet().getListP()[1] = processedDetails;
            income = list.get(i + 1).getNet().getListP()[3];
        }

        list.get(processors).getNet().getListP()[3] = list.get(0).getNet().getListP()[1];
        list.get(processors).getNet().getListT()[1].changeMean(5 * moveTime);

        return new PetriObjModel(list);
    }

}
