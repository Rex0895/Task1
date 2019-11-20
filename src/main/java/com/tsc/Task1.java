package com.tsc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

//        Написать программу, которая читает из файла информацию о сотрудниках и их
//        принадлежности к отделам,рассчитывает среднюю зарплату сотрудников в отделе,
//        cтроит и выводит в файл все варианты возможных переводов сотрудников из одного
//        отдела в другой, при которых средняя зарплата увеличивается в обоих отделах.
public class Task1 {
    private String outString;//Для вывода переводов

    private void printAll(HashMap<String, Department> departments) {

        for (Department department : departments.values()) {
            System.out.println(new Formatter().format("%-20s %20.2f", department.getName(), department.getAvgSalary()).toString());
            for (Employer employer : department.getEmployersList()) {
                System.out.println(new Formatter().format("%-20s %20.2f", employer.getSurname(), employer.getSalary()).toString());
            }
            System.out.println();
        }
    }

    //1
    private String objectsTransfers(HashMap<String, Department> departmentList) {
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
                    for (Employer employerDep1 : tempDepartment1.getEmployersList()) {
                        //Перевод
                        department1.transferEmployer(department2, employerDep1);
                        //Средняя ЗП после перевода
                        BigDecimal transferSalary1 = department1.getAvgSalary();
                        BigDecimal transferSalary2 = department2.getAvgSalary();
                        //Сравниваем среднюю зп в обоих отделах при переводе сотрудника в другой отдел
                        //Если обе срЗП выросли
                        if (transferSalary1.compareTo(baseSalary1) == 1 && transferSalary2.compareTo(baseSalary2) == 1) {

                            outString += new Formatter().format("Перевод сотрудника: %s\nИз %-20s%20.2f(%.2f)\n"
                                            + "В %-20s%21.2f(%.2f)\n",
                                    employerDep1.getSurname(), department1.getName(), baseSalary1, transferSalary1,
                                    department2.getName(), baseSalary2, transferSalary2).toString();
                        }
                        //Перевод обратно
                        department2.transferEmployer(department1, employerDep1);
                    }
                }
            }
        }
        return outString;
    }

    //2
    private String ariphmeticTransfers(HashMap<String, Department> departmentList) {
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
                    List<Employer> employeesFromDepartment1 = department1.getEmployersList();
                    for (Employer employerDep1 : employeesFromDepartment1) {
                        if (employerDep1.getSalary().compareTo(baseSalary1) == -1 && employerDep1.getSalary().compareTo(baseSalary2) == 1) {
                            outString += new Formatter().format("Перевод сотрудника: %s\nИз %-20s%20.2f\n"
                                            + "В %-20s%21.2f\nСредняя зп обоих отделов увеличилась\n",
                                    employerDep1.getSurname(), department1.getName(), baseSalary1,
                                    department2.getName(), baseSalary2).toString();
                        }
                    }
                }
            }
        }
        return outString;
    }

    //3
    private void allTransfers(HashMap<String, Department> departments) {
        outString = new Date().toString() + "\n";
        for (Department department1 : departments.values())//Идем по разделам
        {
            for (Department department2 : departments.values())//Берем другой департамент для сравнения
            {
                if (!department1.equals(department2)) {
                    String depsName = "Из " + department1.getName() + " в " + department2.getName() + ": ";
                    findTrueCombinations(new ArrayList<Employer>(department1.getEmployersList()), department1.getEmployersList(), department2.getEmployersList(), new ArrayList<Employer>(), depsName);
                }
            }
        }
    }

    private void findTrueCombinations(List<Employer> source, List<Employer> employersDep1, List<Employer> employersDep2, List<Employer> partial, String depsName) {
        List<Employer> tmpEmployers1 = new ArrayList<>(employersDep1);//копии списков
        List<Employer> tmpEmployers2 = new ArrayList<>(employersDep2);
        BigDecimal avgSalary = BigDecimal.ZERO;
        //Считаем средне зн. зп
        BigDecimal divider = BigDecimal.ZERO;
        if (partial.size() != 0) {
            for (Employer x : partial) {
                if (source.contains(x) && !source.isEmpty()) {
                    //удаляем из первого списка и добавляем во второй
                    tmpEmployers1.remove(x);
                    tmpEmployers2.add(x);
                }
                avgSalary = avgSalary.add(x.getSalary());
                divider = divider.add(BigDecimal.ONE);//++
            }
            avgSalary = avgSalary.divide(divider, RoundingMode.HALF_UP);//ср.зн. в подмножестве
        }
        BigDecimal transAvgSalary1 = calcAvgSalary(tmpEmployers1);//ср. зн. 1 отд после перевода
        BigDecimal transAvgSalary2 = calcAvgSalary(tmpEmployers2);//ср. зн. 2 отд после перевода

        //сравниваем ср. зн. зп
        if (avgSalary.compareTo(transAvgSalary1) == -1 && avgSalary.compareTo(transAvgSalary2) == 1) {
            //System.out.println(Arrays.toString(partial.toArray()));
            //outStr+=Arrays.toString(partial.toArray())+"\n";
            outString += depsName;
            for (Employer employer : partial) {
                outString += employer.getSurname() + " ";
            }
            outString += "\n";
        }
        for (int i = 0; i < source.size(); i++) {
            ArrayList<Employer> remaining = new ArrayList<Employer>();
            Employer n = source.get(i);
            for (int j = i + 1; j < source.size(); j++)
                remaining.add(source.get(j));
            ArrayList<Employer> partial_rec = new ArrayList<Employer>(partial);
            partial_rec.add(n);
            findTrueCombinations(remaining, employersDep1, employersDep2, partial_rec, depsName);
        }
    }

    //Вспомогательные методы
    private List<Employer> notContains(List<Employer> list, Employer emp) {
        return list.stream().filter(e -> e != emp).collect(Collectors.toList());
    }

    private BigDecimal calcAvgSalary(List<Employer> list) {
        BigDecimal avg = BigDecimal.ZERO;
        for (Employer emp : list) {
            avg = avg.add(emp.getSalary());
        }
        avg = avg.divide(BigDecimal.valueOf(list.size()), RoundingMode.HALF_UP);
        return avg;
    }

    public Task1() {
    }

    //Точка входа
    public static void main(String args[]) {
        new Task1().run(args);
    }

    private void run(String args[]) {
//        try {
//            if (args.length > 2 || args.length < 0) throw new ArrayIndexOutOfBoundsException();
//            if (args == null) throw new NullPointerException();
//        Reader reader = new Reader(args[0]);
//      Writer writer = new Writer(args[1]);

        Reader reader = new Reader("src/main/resources/Employees.txt");
        Writer writer = new Writer("src/main/resources/Results.txt");
        //Создаем список департаментов и заполняем из файла
        HashMap<String, Department> departments = reader.readDepartments();
        //Выводим в консоль
        //printAll(departments);
        //Делаем переводы сотрудников
        //System.out.println(new Date());
        //System.out.println(objectsTransfers(departments));
        // System.out.println("*************************************");
        // System.out.println(ariphmeticTransfers(departments));
        //  System.out.println("*************************************");
        allTransfers(departments);
        System.out.println(outString);
        //Записываем в файл
        //writer.writeToFile(CalculateTransfers(departments));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Неверное число входных параметров");
//        } catch (NullPointerException e) {
//            System.out.println("Не заданы входные параметры");
//        }
    }
}