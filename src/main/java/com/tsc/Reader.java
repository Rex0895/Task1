package com.tsc;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;

public class Reader {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String p) {
        path = p;
    }

    /**
     * Проверка количества параметров
     */
    private boolean testCountParams(String[] strArr, int lineCounter) {
        //Проверка количество входных параметров: Иванов IT 45000
        int length = strArr.length;
        if (length > 3 || length < 0) {
            System.out.println("Ошибка: Неверное число входных параметров: " + length + " (Строка " + lineCounter + ")");
            return true;
        } else return false;
    }

    /**
     * Проверка параметра фамилии
     */
    private boolean testSurnameInput(String str, int lineCounter) {

        String con = "([А-Я])([а-я]+)";
        if (!str.matches(con)) {
            System.out.println("Ошибка: Параметр фамилии в неправильном формате. (Строка " + lineCounter + ")");
            return true;
        } else return false;
    }

    /**
     * Проверка параметра название отдела
     */
    private boolean testDepartmentInput(String str, int lineCounter) {

        String con = "([A-Z])([A-Za-z]+)";
        if (!str.matches(con)) {
            System.out.println("Ошибка: Параметр название департамента в неправильном формате. (Строка " + lineCounter + ")");
            return true;
        } else return false;
    }

    /**
     * Проверка входного параметра зп
     */
    private boolean testSalaryInput(String str, int lineCounter) {
        //Проверка на соответствие типа
        String con = "^([0-9]+([.][0-9]*)?)$";//1000.435
        if (!str.matches(con)) {
            System.out.println("Ошибка: Параметр зарплата в неправильном формате. (Строка " + lineCounter + ")");
            return true;
        } else return false;

    }

    /**
     * Проверка граничных значений ЗП
     */
    private boolean testSalaryBoundaries(BigDecimal salary, int lineCounter) {
        if (salary.compareTo(BigDecimal.ZERO) == -1 || salary.compareTo(BigDecimal.ZERO) == 0
                || salary.compareTo(BigDecimal.valueOf(1000000)) == 1) {
            System.out.println("Ошибка: Параметр ЗП не попадает в заданный диапазон. (Строка " + lineCounter + ")");
            return true;
        } else return false;
    }

    /**
     * Создание сотрудника
     */
    private Employer createEmployer(String[] tmp) {
        //Создаем сотрудника
        String surname = tmp[0];//фамилия
        BigDecimal salary = new BigDecimal(tmp[2]);//зп
        Employer employer = new Employer(surname, salary);
        return employer;
    }

    /**
     * Добавление сотрудников департамент без дублирования отделов
     */
    private void addEmployersToDepartments(Employer employer, String departmentName, HashMap<String, Department> departments) {
        if (!departments.containsKey(departmentName)) {
            Department department = new Department(departmentName);
            department.addEmployer(employer);
            departments.put(departmentName, department);
        } else {
            departments.get(departmentName).addEmployer(employer);
        }
    }


    public Reader(String p) {
        path = p;
    }

    public HashMap<String, Department> readDepartments() {
        int lineCounter = 0;
        HashMap<String, Department> departments = new HashMap<>();
        try (BufferedReader bufRead = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            //Выделяем названия департаментов из файла
            while ((line = bufRead.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] tmp = line.split("(?U)[^\\w|.]+");//делим на подстроки, без лишних символов
                /**Блок проверки входных параметров*/
                //Проверка числа входных параметров
                if (testCountParams(tmp, lineCounter)) continue;
                //Проверки на правильность входных параметров
                if (testSurnameInput(tmp[0], lineCounter)) continue;
                if (testDepartmentInput(tmp[1], lineCounter)) continue;
                if (testSalaryInput(tmp[2], lineCounter)) continue;
                //Проверка на граничные значения зп
                if (testSalaryBoundaries(new BigDecimal(tmp[2]), lineCounter)) continue;
                //Создание сотрудника
                Employer employer = createEmployer(tmp);
                String departmentName = tmp[1];
                //Добавление сотрудника в отдел
                addEmployersToDepartments(employer, departmentName, departments);
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return departments;
    }
}
