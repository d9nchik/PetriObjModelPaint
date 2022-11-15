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

        for (PetriSim petriSim : model.getListObj()) {
            for (PetriP P : petriSim.getNet().getListP()) {
                System.out.println("Place " + P.getName() + ": mean value = " + P.getMean() + "\n"
                        + "         max value = " + P.getObservedMax() + "\n"
                        + "         min value = " + P.getObservedMin() + "\n");
            }
            for (PetriT T : petriSim.getNet().getListT()) {
                System.out.println("Transition " + T.getName() + ": mean value = " + T.getMean() + "\n"
                        + "         max value = " + T.getObservedMax() + "\n"
                        + "         min value = " + T.getObservedMin() + "\n");
            }
        }

    }

    // метод для конструювання моделі масового обслуговування з 4 СМО

    public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();

        int processors = 6;

        list.add(new PetriSim(NetLibrary.CreateNetTask2DetailsIncome()));


        for (int i = 0; i < processors; i++) {
            PetriNet petriNet = NetLibrary.CreateNetTask2Device(i + 1, 1);
            list.add(new PetriSim(petriNet));
        }

        PetriP income = list.get(0).getNet().getListP()[1];
        PetriP processedDetails = list.get(1).getNet().getListP()[1];

        for (int i = 0; i < processors; i++) {
            list.get(i+1).getNet().getListP()[0] = income;
            list.get(i+1).getNet().getListP()[1] = processedDetails;
            income = list.get(i+1).getNet().getListP()[3];
        }

        list.get(processors).getNet().getListP()[3] = list.get(0).getNet().getListP()[1];

        return new PetriObjModel(list);
    }

}
