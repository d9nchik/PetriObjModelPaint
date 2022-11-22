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

        for(PetriSim petriSim : model.getListObj()){
            for (PetriP P : petriSim.getNet().getListP()) {
                    System.out.println("Place " + P.getName() + ": mean value = " + (P.getMean()) + "\n"
                            + "         max value = " + Double.toString(P.getObservedMax()) + "\n"
                            + "         min value = " + Double.toString(P.getObservedMin()) + "\n");
            }
        }
    }

    // метод для конструювання моделі масового обслуговування з 4 СМО

    public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        int n = 10;

        list.add(new PetriSim(NetLibrary.CreateNetPeopleIncome()));
        list.add(new PetriSim(NetLibrary.CreateNetBus(25, 20, 1, n)));
        list.add(new PetriSim(NetLibrary.CreateNetBus(35, 30, 0, n)));
        //перевірка зв'язків
        //     System.out.println(list.get(0).getNet().getListP()[1].getName() + " == " + list.get(1).getNet().getListP()[0].getName());
        //     System.out.println(list.get(1).getNet().getListP()[2].getName() + " == " + list.get(5).getNet().getListP()[0].getName());


        list.get(1).getNet().getListP()[0] = list.get(0).getNet().getListP()[2]; // People on bus stop
        list.get(2).getNet().getListP()[0] = list.get(0).getNet().getListP()[2]; // People on bus stop
        list.get(1).getNet().getListP()[1] = list.get(2).getNet().getListP()[1]; // Money Earned


        return new PetriObjModel(list);
    }

}
