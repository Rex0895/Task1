package com.tsc;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    public HashMap<String, Department> readDepartments() {
        int lineCounter = 0;
        HashMap<String, Department> departments = new HashMap<>();
        try (BufferedReader bufRead = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            //Выделяем названия департаментов из файла
            while ((line = bufRead.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] tmp = line.split("(?U)[^\\w|.]+");//делим на подстроки, без лишних символов
                int len = tmp.length;
                /**Проверка правильности входных параметров*/
                //Проверка количество входных параметров: Иванов IT 45000
                if (len > 3 || len < 0) {
                    System.out.println("Ошибка: Неверное число входных параметров: " + len + " (Строка " + lineCounter + ")");
                    continue;
                }
                //Проверка параметров на null
                if (tmp[0] == null || tmp[1] == null || tmp[2] == null) {
                    System.out.println("Ошибка: Значение одного из параметров null" + " (Строка " + lineCounter + ")");
                    continue;
                }
                //Проверка параметров фамилии и отдела
                String con = "([А-Я])([а-я]+)|([A-Z])([A-Za-z]+)";
                if (!tmp[0].matches(con) || !tmp[1].matches(con)) {
                    System.out.println("Ошибка: Параметры фамилии или департамента в неправильном формате. (Строка " + lineCounter + ")");
                    continue;
                }
                //Создаем сотрудника
                String surname = tmp[0];//фамилия
                String departName = tmp[1];//отдел
                BigDecimal salary = new BigDecimal(tmp[2]);//зп
                //Проверка грпничных значений ЗП
                if (salary.compareTo(BigDecimal.ZERO) == -1 || salary.compareTo(BigDecimal.ZERO) == 0
                        || salary.compareTo(BigDecimal.valueOf(1000000)) == 1) {
                    System.out.println("Ошибка: Параметр ЗП не попадает в заданный диапазон. (Строка " + lineCounter + ")");
                    continue;
                }
                Employer employer = new Employer(surname, salary);
                //Добавление в список без дублирования отделов
                if (!departments.containsKey(departName)) {
                    Department tmpDep = new Department(departName);
                    List<Employer> employerList = new ArrayList<>();
                    employerList.add(employer);
                    tmpDep.setEmployersList(employerList);
                    departments.put(departName, tmpDep);
                } else {
                    departments.get(departName).getEmployersList().add(employer);
                }
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат для параметра ЗП (Строка " + lineCounter + ")");
        }
        return departments;
    }
}
