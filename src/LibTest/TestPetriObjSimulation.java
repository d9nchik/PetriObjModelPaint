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
 *
 * @author Inna V. Stetsenko
 */
public class TestPetriObjSimulation {  //Результати співпадають з аналітичними обрахунками
      public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
                   
     // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling  
          PetriObjModel model = getModel();
          model.setIsProtokol(false);
          double timeModeling = 1000000;
          model.go(timeModeling);
          
         //Цей фрагмент для виведення результатів моделювання на консоль
          System.out.println("Mean value of queue");
          for (int j = 1; j < 6; j++) {
              System.out.println(model.getListObj().get(j).getNet().getListP()[0].getMean());
          }
          System.out.println("Mean value of channel worked");
          for (int j = 1; j < 5; j++) {
              System.out.println(1.0 - model.getListObj().get(j).getNet().getListP()[1].getMean());
          }
          System.out.println(2.0 - model.getListObj().get(4).getNet().getListP()[1].getMean());
          

       /*   for(PetriSim e: model.getListObj()){
              e.printMark();
                           
         }
         for(PetriSim e: model.getListObj()){
              e.printBuffer();
             
         }*/
           
             
      } 
      
     // метод для конструювання моделі масового обслуговування з 4 СМО 
      
      public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure{
          ArrayList<PetriSim> list = new ArrayList<>();
          list.add(new PetriSim(NetLibrary.CreateNetGenerator(40)));
          list.add(new PetriSim(NetLibrary.CreateRobot(28, 2,"ToWorktable1")));
          list.add(new PetriSim(NetLibrary.CreateWorkTable(60,  "Worktable1")));
          list.add(new PetriSim(NetLibrary.CreateRobot(30, 2,"ToWorkTable2")));
          list.add(new PetriSim(NetLibrary.CreateWorkTable(100, "WorkTable2")));
          list.add(new PetriSim(NetLibrary.CreateRobot(26, 2, "ToStore")));
      //перевірка зв'язків
     //     System.out.println(list.get(0).getNet().getListP()[1].getName() + " == " + list.get(1).getNet().getListP()[0].getName());
     //     System.out.println(list.get(1).getNet().getListP()[2].getName() + " == " + list.get(5).getNet().getListP()[0].getName());

          PetriT petriT =  list.get(2).getNet().getListT()[0]; // Worktable1 = 60+-10
          petriT.setDistribution("norm", 60);
          petriT.setParamDeviation(10);

          list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0]; //gen => Robot1

          list.get(1).getNet().getListP()[2] = list.get(2).getNet().getListP()[0]; // Robot1 => Worktable1
          list.get(2).getNet().getListP()[2] = list.get(3).getNet().getListP()[0]; // Worktable1 => Robot2
          list.get(3).getNet().getListP()[2] = list.get(4).getNet().getListP()[0]; // Robot2 => Worktable2
          list.get(4).getNet().getListP()[2] = list.get(5).getNet().getListP()[0]; // Worktable2 => Robot3

          return new PetriObjModel(list);
      }
           
}
