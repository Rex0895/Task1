package com.tsc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//        Написать программу, которая читает из файла информацию о сотрудниках и их
//        принадлежности к отделам,рассчитывает среднюю зарплату сотрудников в отделе,
//        cтроит и выводит в файл все варианты возможных переводов сотрудников из одного
//        отдела в другой, при которых средняя зарплата увеличивается в обоих отделах.
public class Task1 {
    public static void printAll(List<Department> departments) {
        for (Department department : departments) {
            System.out.println("Средняя зарплата отдела " + department.getName() + ": " + department.getAvgSalary().toString());
            for (Employer employer : department.getEmployersList()) {
                System.out.println("Сотрудник: " + employer.getSurname()
                        + " ЗП:" + employer.getSalary().toString());
            }
            System.out.println();
        }
    }

    public static String objectsTransfers(HashMap<String, Department> departmentList){
        //Перемещение сотрудников между отделами
        String outString = new Date().toString() + "\n";
        HashMap<String, Department> departments = new HashMap<>(departmentList);//работаем с копией
        for (Department department1 : departments.values())//Идем по разделам
        {
            Department tempDepartment1 = department1.copy();
            for (Department department2 : departments.values())//Берем другой департамент для сравнения
            {
                Department tempDepartment2 = department2.copy();//копия, паттерн
                if (!department2.equals(department1)) {
                    //Расчет средних зарплат ДО ПЕРЕВОДА
                    BigDecimal baseSalary1 = tempDepartment1.getAvgSalary();
                    BigDecimal baseSalary2 = tempDepartment2.getAvgSalary();
                    //Для Средней ЗП отдела при перемещении каждого сотрудника
                    for (Employer employerFromDepartment1 : tempDepartment1.getEmployersList()) {
                        //Перевод
                        department1.transferEmployer(department2, employerFromDepartment1);
                        //Средняя ЗП после перевода
                        BigDecimal transferSalary1 = department1.getAvgSalary();
                        BigDecimal transferSalary2 = department2.getAvgSalary();
                        //Сравниваем среднюю зп в обоих отделах при переводе сотрудника в другой отдел
                        //Если обе срЗП выросли
                        if (transferSalary1.compareTo(baseSalary1) == 1 && transferSalary2.compareTo(baseSalary2) == 1) {
                            outString += "Перевели сотрудника " + employerFromDepartment1.getSurname()
                                    + " из отдела " + department1.getName() + " в отдел " + department2.getName() + "\n"
                                    + "Средняя зп "+ department1.getName()+" деп (старая/новая): " + baseSalary1 + "/"
                                    + transferSalary1 + "\n" + "Средняя зп "+ department2.getName()+" деп (старая/новая): "
                                    + baseSalary2 + "/" + transferSalary2 + "\n";
                        }
                        //Перевод обратно
                        department2.transferEmployer(department1,employerFromDepartment1);
                    }
                }
            }
        }
        return outString;
    }

    public static String ariphmeticTransfers(HashMap<String, Department> departmentList){
        //Перемещение сотрудников между отделами
        String outString = new Date().toString() + "\n";
        HashMap<String, Department> departments = new HashMap<>(departmentList);//работаем с копией
        for (Department department1 : departments.values())//Идем по разделам
        {
            for (Department department2 : departments.values())//Берем другой департамент для сравнения
            {
                if (!department2.equals(department1)) {
                    BigDecimal baseSalary1 = department1.getAvgSalary();
                    BigDecimal baseSalary2 = department2.getAvgSalary();
                    List<Employer>employeesFromDepartment1= department1.getEmployersList();
                    for (Employer employerFromDepartment1 :employeesFromDepartment1)
                    {
                       if(employerFromDepartment1.getSalary().compareTo(baseSalary1)==-1 &&employerFromDepartment1.getSalary().compareTo(baseSalary2)==1)
                       {
                           outString += "При переводе сотрудника " + employerFromDepartment1.getSurname()
                                        + " из отдела " + department1.getName() + " в отдел " + department2.getName()
                                        +" Средняя зп обоих отделов увеличится \n";
                       }
                    }
                }
            }
        }
        return outString;
    }
    public static void main(String args[]){
//        try {
//            if (args.length > 2 || args.length < 0) throw new ArrayIndexOutOfBoundsException();
//            if (args == null) throw new NullPointerException();
//        Reader reader = new Reader(args[0]);
//      Writer writer = new Writer(args[1]);

        Reader reader = new Reader("src/main/resources/Employees.txt");
        Writer writer = new Writer("src/main/resources/Results.txt");
        //Создаем список департаментов и заполняем из файла
        HashMap<String, Department> departments = new HashMap<>();
        departments = reader.readDepartments();
        //Выводим в консоль
        //PrintAll(departments);
        //Делаем переводы сотрудников
        //System.out.println(new Date());
        System.out.println(objectsTransfers(departments));
        System.out.println("*************************************");
        System.out.println(ariphmeticTransfers(departments));
        //Записываем в файл
        //writer.writeToFile(CalculateTransfers(departments));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Неверное число входных параметров");
//        } catch (NullPointerException e) {
//            System.out.println("Не заданы входные параметры");
//        }
    }
}

