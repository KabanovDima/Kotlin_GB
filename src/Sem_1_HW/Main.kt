package Sem_1_HW

import java.util.Scanner

//Написать программу, которая обрабатывает введённые пользователем в консоль команды:
//• exit
//• help
//• add <Имя> phone <Номер телефона>
//• add <Имя> email <Адрес электронной почты>
//После выполнения команды, кроме команды exit, программа ждёт следующую команду.

//Имя – любое слово.
//Если введена команда с номером телефона, нужно проверить, что указанный телефон может
//начинаться с +, затем идут только цифры. При соответствии введённого номера этому условию –
//выводим его на экран вместе с именем, используя строковый шаблон. В противном случае - выводим
//сообщение об ошибке. Для команды с электронной почтой делаем то же
//самое, но с другим шаблоном – для простоты, адрес должен содержать три последовательности
//букв, разделённых символами @ и точкой.

fun main(){

    do {
        print("Введите команду: ")
        var input = readlnOrNull()
        val splitInputToParts = input?.split(" ")

        if(input == "help"){
            help()
        }
        if(splitInputToParts?.get(0) == "add"){
            if (splitInputToParts.size != 4) {
                println("Данные введены не корректно, повторите ввод, пожалуйста,\n" +
                        "или введите help для вызова подсказки.")
            } else {

                val name = splitInputToParts[1]
                val phoneOrEmail = splitInputToParts[2]
                val data = splitInputToParts[3]


                if (phoneOrEmail == "phone") {
                    addPhoneContact(name, data)
                }
                if (phoneOrEmail == "email") {
                    addEmailContact(name, data)
                }
            }
        }

    }while (input!="exit")
    println("finish")

}
fun help(){
    println("Доступные команды:")
    println("exit - выход из программы")
    println("help - показать доступные команды")
    println("add <Имя> phone <Номер телефона> - добавить контакт с номером телефона")
    println("add <Имя> email <Адрес электронной почты> - добавить контакт с email")
}

fun addPhoneContact(name: String, value: String){
    if(value.matches(Regex("""[0-9]+"""))){
        println("Контакт $name c номером телефона $value добавлен")
    } else{
        println("Ошибка: Неверный формат номера телефона.")
    }
}

fun addEmailContact(name: String, value: String){
    if(value.matches(Regex("""[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+"""))){
        println("Контакт $name c электронной почтой $value добавлен")
    } else{
        println("Ошибка: Неверный формат электронной почты.")
    }
}

