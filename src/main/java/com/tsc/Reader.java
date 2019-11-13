package com.tsc;

import java.io.*;
import java.math.BigDecimal;
import java.util.IllegalFormatException;
import java.util.List;

public class Reader {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String p) {
        path = p;
    }

    public Reader(String p) {
        path = p;
    }

    public boolean preCheck(String line){
        /*Проверка на правильность входной строки*/
        //Все лишние символы и пробелы, кроме точки и запятой (для вещ. чисел)
        String[] tmp = line.split("(?U)[^\\w|.]+");
        //Проверка количество входных параметров
        int len = tmp.length;
        //Иванов IT 45000
        if (len > 3 || len < 0)
            return false;
        String surN = tmp[0];
        String depN = tmp[1];
        String sal = tmp[2];
        /*Проверка правильности входных параметров*/
        //Проверка фамилии и отдела
        String con = "([А-Я])([а-я]+)|([A-Z])([A-Za-z]+)";
        if (!surN.matches(con) || !depN.matches(con)) return false;
            //System.out.println("Неправильный формат фамилии или названия отдела");

        //Проверка значений зарплаты
        BigDecimal bd = new BigDecimal(sal);
        //Отрицательные / 0 / больше 1кк
        if (bd.compareTo(BigDecimal.ZERO) == -1 || bd.equals(BigDecimal.ZERO)
                || bd.compareTo(BigDecimal.valueOf(1000000)) == 1) return false;
            //System.out.println("Неправильный формат ЗП");
        return true;
    }
    public void readDepartments(List<Department> deps) {
        try (BufferedReader bufRead = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            //Выделяем названия департаментов из файла
            while ((line = bufRead.readLine()) != null) {
                if(preCheck(line)) {
                    String[] tmp = line.split(" ");//делим на подстроки
                    //Создаем сотрудника
                    String surname = tmp[0];//фамилия
                    String departName = tmp[1];//отдел
                    BigDecimal salary = new BigDecimal(tmp[2]);//зп
                    Employer emp = new Employer(surname, salary);
                    Department tmpDep = new Department(departName);
                    if (deps.isEmpty() || !deps.contains(tmpDep))//сравнение по имени
                    {
                        deps.add(tmpDep);
                    }
                    for (Department dep : deps) {
                        if (dep.getName().equals(departName))
                            dep.getListEmp().add(emp);
                    }
                }
            }
            bufRead.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ix) {
            System.out.println(ix.toString());
        }
    }
}
