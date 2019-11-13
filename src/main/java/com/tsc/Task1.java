package com.tsc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//        Написать программу, которая читает из файла информацию о сотрудниках и их
//        принадлежности к отделам,рассчитывает среднюю зарплату сотрудников в отделе,
//        cтроит и выводит в файл все варианты возможных переводов сотрудников из одного
//        отдела в другой, при которых средняя зарплата увеличивается в обоих отделах.
public class Task1{
    public static void PrintAll(List<Department> deps) {
        for (Department dep : deps) {
            System.out.println("Средняя зарплата отдела " + dep.getName() + ": " + dep.getAvgSalary().toString());
            for (Employer emp : dep.getListEmp()) {
                System.out.println("Сотрудник: " + emp.getSurname()
                        + " ЗП:" + emp.getSalary().toString());
            }
            System.out.println();
        }
    }
    public static String CalculateTransfers(List<Department> departments) {
        //Перемещение сотрудников между отделами
        String outStr = new Date().toString()+"\n";
        List<Department> deps = new ArrayList<>(departments);//работаем с копией
        for (Department dep1 : deps)//Идем по разделам
        {
            Department tmpDep1 = new Department(dep1);//копия
            for (Department dep2 : deps)//Берем другой департамент для сравнения
            {
                Department tmpDep2 = new Department(dep2);//копия
                if (!tmpDep2.equals(tmpDep1)) {
                    //Расчет средних зарплат ДО ПЕРЕВОДА
                    BigDecimal baseSal1 = tmpDep1.getAvgSalary();
                    BigDecimal baseSal2 = tmpDep2.getAvgSalary();
                    //Для Средней ЗП отдела при перемещении каждого сотрудника
                    for (Employer empDep1 : tmpDep1.getListEmp()) {
                        //Перевод
                        dep1.transferEmployer(dep2, empDep1);
                        //Средняя ЗП после перевода
                        BigDecimal tranSal1 = dep1.getAvgSalary();
                        BigDecimal tranSal2 = dep2.getAvgSalary();
                        //Сравниваем среднюю зп в обоих отделах при переводе сотрудника в другой отдел
                        //Если обе срЗП выросли
                        if (tranSal1.compareTo(baseSal1) == 1 && tranSal2.compareTo(baseSal2) == 1) {
                            outStr += "Перевели сотрудника " + empDep1.getSurname()
                                    + " из отдела " + dep1.getName() + " в отдел " + dep2.getName() + "\n"
                                    + "Средняя зп 1 деп (старая/новая): " + baseSal1 + "/"
                                    + tranSal1 + "\n" + "Средняя зп 2 деп (старая/новая): "
                                    + baseSal2 + "/" + tranSal2 + "\n";
                        }

                        //Перевод обратно
                        dep1.returnEmployer(dep2, empDep1);
                    }
                }
            }
        }
        return outStr;
    }

    public static void main(String args[]) {
//        Reader reader = new Reader(args[0]);
//      Writer writer = new Writer(args[1]);
        Reader reader = new Reader("src/main/resources/Employees.txt");
        Writer writer = new Writer("src/main/resources/Results.txt");
        //Создаем список департаментов и заполняем из файла
        List<Department> departments = new ArrayList<>();
        reader.readDepartments(departments);
        //Выводим в консоль
        //PrintAll(departments);
        //Делаем переводы сотрудников
        //System.out.println(new Date());
        System.out.println(CalculateTransfers(departments));
        //Записываем в файл
        //writer.writeToFile(CalculateTransfers(departments));
    }
}

