package com.company;

import java.io.*;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        // write your code here

   /*    Class class1 = new Class();
        class1.setName("9A");
        ArrayList<Student> stAr1 = new ArrayList<Student>();
        Student st1 = new Student("Аникеева", "Юлия", "Александровна", null, "", "");
        Student st2 = new Student("Беляев", "Игорь", "Валерьевич", null, "", "");
        Student st3 = new Student("Воронина", "Ольга", "Владимировна", null, "", "");
        Student st4 = new Student("Грязнов", "Юрий", "Александрович", null, "", "");
        Student st5 = new Student("Дегтярева", "Оксана", "Павловна", null, "", "");
        Student st6 = new Student("Ерилкин", "Максим", "Юрьевич", null, "", "");
        Student st7 = new Student("Железнова", "Анна", "Игоревна", null, "", "");
        Student st8 = new Student("Зайкин", "Максим", "Сергеевич", null, "", "");
        Student st9 = new Student("Иванова", "Инна", "Ивановна", null, "", "");
        Student st10 = new Student("Каменев", "Сергей", "Олегович", null, "", "");
        stAr1.add(st1);
        stAr1.add(st2);
        stAr1.add(st3);
        stAr1.add(st4);
        stAr1.add(st5);
        stAr1.add(st6);
        stAr1.add(st7);
        stAr1.add(st8);
        stAr1.add(st9);
        stAr1.add(st10);
        class1.setStudents(stAr1);

        Class class2 = new Class();
        class2.setName("9Б");
        ArrayList<Student> stAr2 = new ArrayList<Student>();
        Student st12 = new Student("Акименко", "Оксана", "Александровна", null, "", "");
        Student st22 = new Student("Боровицкий", "Антон", "Сергеевич", null, "", "");
        Student st32 = new Student("Виляева", "Светлана", "Владимировна", null, "", "");
        Student st42 = new Student("Горелов", "Михаил", "Александрович", null, "", "");
        Student st52 = new Student("Данилова", "Кристина", "Михайловна", null, "", "");
        Student st62 = new Student("Елизаров", "Валерий", "Юрьевич", null, "", "");
        Student st72 = new Student("Жирова", "Екатерина", "Максимовна", null, "", "");
        Student st82 = new Student("Зябликов", "Святослав", "Сергеевич", null, "", "");
        Student st92 = new Student("Инюшкина", "Анастасия", "Дмитриевна", null, "", "");
        Student st102 = new Student("Козлов", "Сергей", "Николаевич", null, "", "");
        stAr2.add(st12);
        stAr2.add(st22);
        stAr2.add(st32);
        stAr2.add(st42);
        stAr2.add(st52);
        stAr2.add(st62);
        stAr2.add(st72);
        stAr2.add(st82);
        stAr2.add(st92);
        stAr2.add(st102);
        class2.setStudents(stAr2);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("objects.dat"));
            out.writeInt(2);
            out.writeObject(class1);
            out.writeObject(class2);
            out.close();
        }
        catch (IOException ioe){

        }
*/

//десериализация выполнится при нажатии на 0 - выйти. При прерывании программы изменения не сохранятся
        //десериализация
        ArrayList<Class> classes = new ArrayList<>();
        try {
            ObjectInputStream s = new ObjectInputStream(new FileInputStream("objects.dat"));
            int size = s.readInt();
            for (int i = 0; i < size; i++) {
                Class cl = (Class) s.readObject();
                classes.add(cl);
            }
            s.close();
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        } catch (ClassNotFoundException cle) {
            log.error(cle.getMessage());
        }

        classChoice: do {
            //выбор класса
            int classNumber = classChoice(classes);

            //выбор - добавить журнал или просмотреть существующий
            Scanner scanner = new Scanner(System.in);
            int q;
            registerChoice:
            do {
                System.out.println("//*КЛАСС " + classes.get(classNumber).getName() + "*//");
                System.out.println("Введите 1, чтобы добавить новый журнал, 2, чтобы выбрать существующий, 9, чтобы вернуться к выбору класса и 0, чтобы выйти");
                do {
                    q = scanner.nextInt();
                    switch (q) {
                        case 1:
                            addRegister(classes.get(classNumber));
                            break;
                        case 2:
                            break;
                        case 9:
                            break registerChoice;
                        case 0:
                            break classChoice;
                        default:
                            System.out.println("Введен неверный номер, попробуйте еще раз");
                            break;
                    }
                } while (q != 1 && q != 2);

                Register register = registerChoise(classes.get(classNumber));

                //выбор действия над журналом
                actChoice:
                do {
                    System.out.println("Выберите дальнейшее действие: \n" +
                            "1. Просмотреть журнал\n" +
                            "2. Добавить оценку\n" +
                            "3. Редактировать журнал\n" +
                            "4. Удалить журнал\n" +
                            "9. Вернуться к выбору журнала\n" +
                            "0. Выйти");
                    q = scanner.nextInt();
                    switch (q) {
                        //просмотр журнала
                        case 1:
                            viewRegister(register);
                            break;
                        //добавление оценки
                        case 2:
                            addMark(classes.get(classNumber).getStudents(), register);
                            break;
                        //редактрование или удаление оценки
                        case 3:
                            updateMark(classes.get(classNumber).getStudents(), register);
                            break;
                        //удалить журнал
                        case 4:
                            classes.get(classNumber).getRegisters().remove(register.getSubject());
                            System.out.println("Журнал по предмету " + register.getSubject() + " удален для класса " + classes.get(classNumber).getName());
                            break actChoice;
                        case 9:
                            break actChoice;
                        case 0:
                            break classChoice;
                        default:
                            System.out.println("Неверный номер, введите еще раз \n");
                            break;
                    }
                } while (q != 0);
            } while (true);
        }while(true);

            //сериализация
            try {
                ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream("objects.dat"));
                s.writeInt(classes.size());
                for (Class cl : classes) {
                    s.writeObject(cl);
                }
                s.close();
            } catch (IOException ioe) {
                log.error(ioe.getMessage());
            }

    }

    //выбор класса
    public static int classChoice(ArrayList<Class> classes){
        Scanner scanner = new Scanner(System.in);
        int q;
        System.out.println("Выберите класс (введите порядковый номер):");
        for (int i = 0; i < classes.size(); i++)
            System.out.println(i + 1 + ". " + classes.get(i).getName());
        do {
            q = scanner.nextInt();
            if (q > 0 && q < classes.size()+1) break;
            else System.out.println("Неверный номер, введите еще раз: ");
        } while (true);
        return q - 1;
    }

    //выбор журнала
    public static Register registerChoise(Class cl)
    {
        //если журналов нет, создать
        if (cl.getRegisters().size() == 0) {
            System.out.println("Нет журналов для просмотра. Добавьте новый журнал");
            addRegister(cl);
        }

        //выбор журнала
        System.out.println("Выберите предмет (введите порядковый номер):");
        Register.Subjects[] subjects = cl.getRegisters().keySet().toArray(new Register.Subjects[cl.getRegisters().size()]);
        for (int i = 0; i < subjects.length; i++)
            System.out.println(i + 1 + ". " + subjects[i].name());
        int q;
        Scanner scanner = new Scanner(System.in);
        do {
            q = scanner.nextInt();
            if (q > 0 && q < subjects.length + 1) break;
            else System.out.println("Неверный номер, введите еще раз: ");
        } while (true);
        return cl.getRegisters().get(subjects[q-1]);
    }

    //добавление журнала
    public static void addRegister(Class cl){
        System.out.println("//* ДОБАВЛЕНИЕ НОВОГО ЖУРНАЛА *//");
        System.out.println("Выберите предмет (введите порядковый номер): ");
        for(Register.Subjects subject: Register.Subjects.values()){
            System.out.println(subject.ordinal()+1+". "+subject.name());
        }
        int q;
        Scanner scanner = new Scanner(System.in);
        do{
            q = scanner.nextInt();
            if(q>0 && q < Register.Subjects.values().length+1){
                for (Register.Subjects sub: Register.Subjects.values())
                    if(sub.ordinal()+1 == q){
                        cl.getRegisters().put(sub, new Register(sub, cl.getStudents()));
                        System.out.println("Журнал для класса "+cl.getName()+" по предмету "+sub+" создан");
                        break;
                }
            }
            else System.out.println("Неверный номер, попробуйте еще раз");
        } while (q<1 || q>Register.Subjects.values().length);
    }

    //просмотр журнала
    public static void viewRegister(Register register){
        int i=0;
        for (ArrayList m: register) {
            System.out.println(++i + ". " + m.get(0) + ": ");
            for (int j = 0; j < ((ArrayList<Mark>)m.get(1)).size(); j++)
                System.out.println("\t"+((ArrayList<Mark>)m.get(1)).get(j).toString());
        }
    }

    //добавление оценки
    public static void addMark(ArrayList<Student> students, Register register){
        System.out.println("Выберите ученика(порядковый номер)");
        for (int i = 0; i < students.size(); i++)
            System.out.println(i + 1 + ". " + students.get(i).getName());
        Scanner scanner = new Scanner(System.in);
        int q;
        do {
            q = scanner.nextInt();
            if (q > 0 && q < students.size() + 1) {
                System.out.println("Введите оценку ");
                int q1;
                do {
                    q1 = scanner.nextInt();
                    if (q1 > 0 && q1 < 6) {
                        System.out.println("Введите дату в формате ДД.ММ.ГГ (При неверно введенной дате будет выбрана текущая дата)");
                        String q2 = scanner.next();
                        Date date;
                        try {
                            date = new SimpleDateFormat("dd.MM.yy").parse(q2);
                        } catch (ParseException pe) {
                            date = new Date();
                        }
                        register.addMark(students.get(q - 1), new Mark(q1, date));
                        log.info("Add mark: "+q1+" "+ new SimpleDateFormat("dd.MM.yy").format(date) +" to student "+students.get(q-1).getName());
                        break;
                    } else
                        System.out.println("Неверная оценка, введите еще раз");
                } while (true);
                break;
            } else
                System.out.println("Неверный номер, попробуйте еще раз");
        } while (q < 1 || q > students.size());
        System.out.println("Оценка добавлена");
    }

    //редактирование и удаление оценки
    public static void updateMark(ArrayList<Student> students, Register register) {
        System.out.println("Выберите ученика(порядковый номер) для редактирования/удаления оценки");
        for (int i = 0; i < students.size(); i++)
            System.out.println(i + 1 + ". " + students.get(i).getName());
        int studentNumber;
        Scanner scanner = new Scanner(System.in);
        do {
            studentNumber = scanner.nextInt();
            if (studentNumber > 0 && studentNumber < students.size() + 1) {
                Student student = students.get(studentNumber-1);
                System.out.println("Выберите оценку(порядковый номер) для редактирования/удаления оценки");
                for (int i = 0; i < register.getMarksOfStudent(student).size(); i++)
                    System.out.println(i + 1 + ". " + register.getMarksOfStudent(student).get(i));
                int markNumber;
                do {
                    markNumber = scanner.nextInt();
                    if (markNumber > 0 && markNumber < register.getMarksOfStudent(student).size() + 1) {
                        System.out.println("Нажмите 1, чтобы изменить оценку или 2, чтобы удалить ее");
                        int q;
                        do {
                            q = scanner.nextInt();
                            switch (q) {
                                //отредактировать оценку (только значение. Если нужно изменить дату, то удалить оценку и добавить новую)
                                case 1:
                                    System.out.println("Введите новую оценку ");
                                    int q1;
                                    do {
                                        q1 = scanner.nextInt();
                                        if (q1 > 0 && q1 < 6) {
                                            register.getMarksOfStudent(student).set(markNumber - 1, new Mark(q1, register.getMarksOfStudent(student).get(markNumber - 1).getDate()));
                                            System.out.println("Оценка изменена");
                                            log.info("Update mark: "+q1+" "+ new SimpleDateFormat("dd.MM.yy").format(register.getMarksOfStudent(student).get(markNumber - 1).getDate()) +" of student "+student.getName());
                                            break;
                                        } else
                                            System.out.println("Неверная оценка, введите еще раз");
                                    } while (q1 < 1 || q1 > 5);

                                    break;
                                //удалить оценку
                                case 2:
                                    register.getMarksOfStudent(student).remove(markNumber-1);
                                    System.out.println("Оценка удалена");
                                    log.info("Delete mark from student "+student.getName());
                                    break;
                                default:
                                    System.out.println("Неверная команда, введите еще раз ");
                                    break;
                            }
                        } while (q != 1 && q != 2);
                        break;
                    }
                } while (markNumber < 1 || markNumber > 5);
                break;
            } else
                System.out.println("Неверный номер, попробуйте еще раз");
        } while (studentNumber < 1 || studentNumber > students.size());
    }
}
