package com.tsc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//        Написать программу, которая читает из файла информацию о сотрудниках и их
//        принадлежности к отделам,рассчитывает среднюю зарплату сотрудников в отделе,
//        cтроит и выводит в файл все варианты возможных переводов сотрудников из одного
//        отдела в другой, при которых средняя зарплата увеличивается в обоих отделах.
public class Task1 {

    private void printAllEmployers(HashMap<String, Department> departments) {
        for (Department department : departments.values()) {
            System.out.println(String.format("%-20s %20.2f", department.getName(), department.getAvgSalary()));
            for (Employer employer : department.getEmployersList()) {
                System.out.println(String.format("%-20s %20.2f", employer.getSurname(), employer.getSalary()));
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
        StringBuilder outString = new StringBuilder(new Date().toString() + "\n");
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
                            outString.append(String.format("Перевод сотрудника: %s\nИз %-20s%20.2f\nВ %-20s%21.2f\nСредняя зп обоих отделов увеличилась\n",
                                    employerDep1.getSurname(), department1.getName(), baseSalary1,
                                    department2.getName(), baseSalary2));
                        }
                    }
                }
            }
        }
        return outString.toString();
    }

    //3
    private String makeAllTransfers(HashMap<String, Department> departments) {
        StringBuilder outString = new StringBuilder(new Date().toString() + "\n");
        for (Department department1 : departments.values())//Идем по разделам
        {
            for (Department department2 : departments.values())//Берем другой департамент для сравнения
            {
                if (!department1.equals(department2)) {
                    outString.append(findTrueCombinations(new ArrayList<Employer>(department1.getEmployersList()), department1, department2, new ArrayList<Employer>()));
                }
            }
        }
        return outString.toString();
    }

    private String findTrueCombinations(List<Employer> source, Department department1, Department department2, List<Employer> partial) {
        StringBuilder outString = new StringBuilder();
        List<Employer> tmpEmployers1 = new ArrayList<>(department1.getEmployersList());//копии списков
        List<Employer> tmpEmployers2 = new ArrayList<>(department2.getEmployersList());
        //Пересчитываем среднее зн. зп для отделов и подмножества
        BigDecimal avgSalary = BigDecimal.ZERO;
        if (partial.size() != 0) {//после первого перевода

            BigDecimal divider = BigDecimal.valueOf(partial.size());//делитель
            for (Employer x : partial) {
                if (source.contains(x) && !source.isEmpty()) {
                    //удаляем из первого списка и добавляем во второй
                    tmpEmployers1.remove(x);
                    tmpEmployers2.add(x);
                }
                avgSalary = avgSalary.add(x.getSalary());
            }
            avgSalary = avgSalary.divide(divider, RoundingMode.HALF_UP);//ср.зн. в подмножестве
        }
        BigDecimal transAvgSalary1 = calcAvgSalary(tmpEmployers1);//ср. зн. 1 отд после перевода
        BigDecimal transAvgSalary2 = calcAvgSalary(tmpEmployers2);//ср. зн. 2 отд после перевода

        //сравниваем ср. зн. зп и выводим нужные
        if (avgSalary.compareTo(transAvgSalary1) == -1 && avgSalary.compareTo(transAvgSalary2) == 1) {
            outString.append(String.format("Из %s в %s: ", department1.getName(), department2.getName()));
            for (Employer employer : partial) {
                outString.append(employer.getSurname() + " ");
            }
            outString.append("\n");
        }
        for (int i = 0; i < source.size(); i++) {
            ArrayList<Employer> remaining = new ArrayList<Employer>();
            Employer n = source.get(i);
            for (int j = i + 1; j < source.size(); j++)
                remaining.add(source.get(j));
            ArrayList<Employer> partial_rec = new ArrayList<Employer>(partial);
            partial_rec.add(n);
            outString.append(findTrueCombinations(remaining, department1, department2, partial_rec));
        }
        return outString.toString();
    }

    //Вспомогательные методы

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
        try {
            if (args.length > 2 || args.length < 0)
                throw new ArrayIndexOutOfBoundsException();
            if (args == null) throw new NullPointerException();
            Reader reader = new Reader(args[0]);
            Writer writer = new Writer(args[1]);
            //Заполняем из файла
            HashMap<String, Department> departments = reader.readDepartments();
            //Выводим в консоль всех сотрудников
            printAllEmployers(departments);
            StringBuilder strOut = new StringBuilder();
            strOut.append("Метод прямомого перевода сорудников:\n");
            strOut.append(objectsTransfers(departments) + "\n");
            strOut.append("Арифметический метод перевода сорудников:\n");
            strOut.append(ariphmeticTransfers(departments) + "\n");
            strOut.append("Рекурсионный метод перевода сорудников:\n");
            strOut.append(makeAllTransfers(departments) + "\n");
            //Записываем в файл
            writer.writeToFile(strOut.toString());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Количество аргументов неверно: " + args.length + " вместо 2");
        } catch (NullPointerException e) {
            System.out.println("Нет входные параметров");
        }
    }
}
//
// Reader reader = new Reader("src/main/resources/Employees.txt");
// Writer writer = new Writer("src/main/resources/Results.txt");