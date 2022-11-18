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
        double repeatModeling = 10;
        double timeModeling = 1_000;

        double[] shipIncomeTimes = new double[]{0.01, 1, 100};
        double[][] serviceTimes = new double[][]{{0.005, 0.015}, {0.5, 1.5}, {50, 150}};

        for (double shipIncomeTime : shipIncomeTimes) {
            for (double[] serviceTime : serviceTimes) {
                double serviceMeanTime = (serviceTime[1] + serviceTime[0]) / 2;
                double serviceDeviationTime = (serviceTime[1] - serviceTime[0]) / 2;

                for (int i = 0; i < repeatModeling; i++) {
                    System.out.print(shipIncomeTime + "\t" + serviceTime[0] + "\t" + serviceTime[1] + "\t");
                    // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling
                    PetriObjModel model = getModel(shipIncomeTime, serviceMeanTime, serviceDeviationTime);
                    model.setIsProtokol(false);
                    model.go(timeModeling);

                    // Цей фрагмент для виведення результатів моделювання на консоль
                    PetriP[] petriP = model.getListObj().get(0).getNet().getListP();
                    // завантаження 1-го крану
                    System.out.print(1 - petriP[6].getMean() + "\t");
                    // завантаження 2-го крану
                    System.out.print(1 - petriP[9].getMean() + "\t");
                    // завантаження 1-ї якірної стоянки
                    System.out.print(1 - petriP[2].getMean() + "\t");
                    // завантаження 2-ї якірної стоянки
                    System.out.print(1 - petriP[7].getMean() + "\t");

                    // час обслуговування кораблів
                    PetriPPortAvailability port1 = (PetriPPortAvailability) petriP[2];
                    PetriPPortAvailability port2 = (PetriPPortAvailability) petriP[7];
                    // максимальне
                    System.out.print(Double.max(port1.getMaxTime(), port2.getMaxTime()) + "\t");
                    // мінімальне
                    System.out.print(Double.min(port1.getMinTime(), port2.getMinTime()) + "\t");
                    // середнє
                    System.out.print((port1.getSumTime() + port2.getSumTime()) / (port1.getN() + port2.getN()) + "\t");

                    // к-ть обслуговуваних кораблів
                    System.out.print(petriP[5].getObservedMax() + "\n");

                }
            }
        }
    }

    public static PetriObjModel getModel(double shipIncomeTime, double serviceMeanTime, double serviceDeviationTime) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetCoursework(1000, shipIncomeTime, serviceMeanTime, serviceDeviationTime)));
        return new PetriObjModel(list);
    }

}
