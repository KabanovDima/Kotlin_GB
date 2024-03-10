package Sem_2_HW

sealed interface Command{
    fun isValid(): Boolean

}
class ExitCommand(val input: String) : Command{
    override fun isValid(): Boolean {
        return input == "exit"
    }

    override fun toString(): String {
        return input
    }
}
class HelpCommand(val input: String) : Command{
    override fun isValid(): Boolean {
        return input == "help"
    }

    override fun toString(): String {
        return input
    }
}
class AddPhoneContactCommand(val name: String, val phoneNumber: String): Command{
    override fun isValid(): Boolean {
        return phoneNumber.matches(Regex("""[0-9]+"""))
    }

    override fun toString(): String {
        return "Add phone number"
    }
}
class AddEmailContactCommand(val name: String, val email: String) : Command{
    override fun isValid(): Boolean {
        return email.matches(Regex("""[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+"""))
    }
    override fun toString(): String {
        return "Add email"
    }
}
class ShowCommand: Command{
    override fun isValid(): Boolean {
        return true
    }
    override fun toString(): String {
        return "show"
    }
}
data class Person(
    val name: String,
    val phone: String,
    val email: String)

fun main(){
    val commands = mutableListOf<Command>()
    var lastContact: Person? = null
    var command: Command?
    do {
        print("Введите команду: ")
        command = readComand(commands)
        println("Вы ввели команду: $command")
        if(command!=null){
            if(!command.isValid()) {
                help()
            } else{
                when (command){
                    is ExitCommand -> {
                        println("Выход из программы")
                        lastContact
                    }

                    is HelpCommand -> {
                        help()
                        lastContact
                    }
                    is AddPhoneContactCommand -> {
                        addPhoneContact(command.name, command.phoneNumber)
                        Person(command.name, command.phoneNumber, "")
                    }
                    is AddEmailContactCommand -> {
                        addEmailContact(command.name, command.email)
                        Person(command.name,"", command.email)
                    }
                    is ShowCommand -> {
                        showLastContact(commands, lastContact)
                        lastContact
                    }
                }
            }
        } else {
            println("Данные введены не корректно, повторите ввод, пожалуйста,\n" +
                    "или введите help для вызова подсказки.")
        }
    }while (command !is ExitCommand)
}

fun readComand(commands: MutableList<Command>, input: String? = null): Command?{
    val userInput = input ?: readlnOrNull() ?: ""
    val parts = userInput.split(" ")
    return when(parts[0]){
        "exit" -> ExitCommand(userInput)
        "help" -> HelpCommand(userInput)
        "add" -> parseAddCommand(parts, commands)
        "show" -> ShowCommand()
        else -> null
    }
}

fun parseAddCommand(parts: List<String>, commands: MutableList<Command>): Command?{
    if(parts.size !=4) return null
    val name = parts[1]
    val type = parts[2]
    val data = parts[3]

    return when(type){
        "phone" -> {
            val command = AddPhoneContactCommand(name, data)
            commands.add(command)
            command
        }
        "email" -> {
            val command = AddEmailContactCommand(name, data)
            commands.add(command)
            command
        }
        else -> null
    }
}

fun help(){
    println("Доступные команды:")
    println("exit - выход из программы")
    println("help - показать доступные команды")
    println("add <Имя> phone <Номер телефона> - добавить контакт с номером телефона")
    println("add <Имя> email <Адрес электронной почты> - добавить контакт с email")
    println("show - показать последнее добавленное значение")
}

fun addPhoneContact(name: String, value: String){
    println("Контакт $name c номером телефона $value добавлен")
}

fun addEmailContact(name: String, value: String){
    println("Контакт $name c электронной почтой $value добавлен")
}

fun showLastContact(contacts: List<Command>, lastContact: Person?){
    if (lastContact != null) {
        print("Последнее добавленное значение: ")
        println("Имя: ${lastContact.name}, Телефон: ${lastContact.phone}, Email: ${lastContact.email}")
    } else {
        println("Not initialized")
    }
}