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
        double repeatModeling = 4;
        double timeModeling = 10_000;

        double[] shipIncomeTimes = new double[]{0.01, 1, 100};
        double[][] serviceTimes = new double[][]{{0.005, 0.015}, {0.5, 1.5}, {50, 150}};

        for (double shipIncomeTime : shipIncomeTimes) {
            for (double[] serviceTime : serviceTimes) {
                double serviceMeanTime = (serviceTime[1] + serviceTime[0]) / 2;
                double serviceDeviationTime = (serviceTime[1] - serviceTime[0]) / 2;
            }
        }

        for (int i = 0; i < repeatModeling; i++) {
            // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
            PetriObjModel model = getModel(1.25, 1, 0.5);
            model.setIsProtokol(false);
            for (int j = 100; j < timeModeling; j += 100) {
                model.goWithStep(j);

                // Цей фрагмент для виведення результатів моделювання на консоль
                PetriP[] petriP = model.getListObj().get(0).getNet().getListP();

                // час обслуговування кораблів
                PetriPPortAvailability port1 = (PetriPPortAvailability) petriP[2];
                PetriPPortAvailability port2 = (PetriPPortAvailability) petriP[7];
                // середнє
                System.out.print((port1.getSumTime() + port2.getSumTime()) / (port1.getN() + port2.getN()) + "\t");
            }
            System.out.println();
        }
    }

    public static PetriObjModel getModel(double shipIncomeTime, double serviceMeanTime, double serviceDeviationTime) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetCoursework(1000, shipIncomeTime, serviceMeanTime, serviceDeviationTime)));
        return new PetriObjModel(list);
    }

}
