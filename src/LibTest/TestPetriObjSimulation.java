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
        double repeatModeling = 20;
        double timeModeling = 10_000;

        double[] shipIncomeTimes = new double[]{1.5, 1};
        double[] serviceTimes = new double[]{1.2, 1};

        for (double serviceTime : serviceTimes) {
            for (double shipIncomeTime : shipIncomeTimes) {
                System.out.print(shipIncomeTime + "\t" + serviceTime + "\t");
                for (int i = 0; i < repeatModeling; i++) {
                    // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
                    PetriObjModel model = getModel(shipIncomeTime, serviceTime, 0.5);
                    model.setIsProtokol(false);

                    model.goWithStep(5_000);

                    // Цей фрагмент для виведення результатів моделювання на консоль
                    PetriP[] petriP = model.getListObj().get(0).getNet().getListP();
                    // час обслуговування кораблів
                    PetriPPortAvailability port1 = (PetriPPortAvailability) petriP[2];
                    PetriPPortAvailability port2 = (PetriPPortAvailability) petriP[7];
                    port1.resetMetrics();
                    port2.resetMetrics();

                    model.goWithStep(timeModeling);


                    // середнє
                    System.out.print((port1.getSumTime() + port2.getSumTime()) / (port1.getN() + port2.getN()) + "\t");
                }
                System.out.println();
            }
        }

    }

    public static PetriObjModel getModel(double shipIncomeTime, double serviceMeanTime, double serviceDeviationTime) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetCoursework(1000, shipIncomeTime, serviceMeanTime, serviceDeviationTime)));
        return new PetriObjModel(list);
    }

}
