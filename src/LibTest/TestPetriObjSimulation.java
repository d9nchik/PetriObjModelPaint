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
        double[] fridgeCreationTimes = new double[]{0.4, 4, 40, 400};
        double[] reorderTimes = new double[]{0.3, 3, 30, 300};
        double[] clientComeTimes = new double[]{0.2, 2, 20, 200};
        double[] possibilitiesOfClientWaiting = new double[]{0.1, 0.2, 0.5, 0.9};

        DecimalFormat format = new DecimalFormat("0.#");

        for (double fridgeCreationTime : fridgeCreationTimes) {
            for (double reorderTime : reorderTimes) {
                for (double clientComeTime : clientComeTimes) {
                    for (double possibilityOfClientWaiting : possibilitiesOfClientWaiting) {

                        System.out.printf("%s\t%s\t%s\t%s\t", format.format(fridgeCreationTime), format.format(reorderTime), format.format(clientComeTime), format.format(possibilityOfClientWaiting));

                        PetriObjModel model = getModel(fridgeCreationTime, reorderTime, clientComeTime, possibilityOfClientWaiting);
                        model.setIsProtokol(false);
                        double timeModeling = 1_000_000;
                        model.go(timeModeling);


                        PetriNet petriNet = model.getListObj().get(0).getNet();
                        // невдоволений попит
                        System.out.printf("%d\t", petriNet.getListP()[13].getObservedMax());
                        // виконані замовлення
                        System.out.printf("%d\t", petriNet.getListP()[10].getObservedMax());
                        // кількість холодильників
                        System.out.printf("%s\t", format.format(petriNet.getListP()[7].getMean()));
                        // потрібно дозамовити
                        System.out.printf("%s\t", format.format(petriNet.getListP()[3].getMean()));
                        // користувачі в очікуванні
                        System.out.printf("%s\n", format.format(petriNet.getListP()[11].getMean()));
                    }
                }
            }
        }

    }


    public static PetriObjModel getModel(double fridgeCreationTime, double reorderTime, double clientComeTime, double possibilityOfClientWaiting) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(NetLibrary.CreateNetTask3(fridgeCreationTime, reorderTime, clientComeTime, possibilityOfClientWaiting)));
        return new PetriObjModel(list);
    }

}
