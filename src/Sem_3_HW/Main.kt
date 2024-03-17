package Sem_3_HW

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
class ShowCommand(val name: String) : Command {
    override fun isValid(): Boolean {
        return true
    }

    override fun toString(): String {
        return "show $name"
    }
}
class FindCommand(val query: String) : Command {
    override fun isValid(): Boolean {
        return true
    }
}

data class Person(
    val name: String,
    val phone: MutableList<String>,
    val email: MutableList<String>)

fun main(){
    val phoneBook = mutableMapOf<String, Person>()
    var lastContact: String? = null
    var command: Command?
    do {
        print("Введите команду: ")
        command = readComand(phoneBook, mutableListOf())
        println("Вы ввели команду: $command")
        if (command != null) {
            when (command) {
                is ExitCommand -> {
                    println("Выход из программы")
                    lastContact
                }
                is HelpCommand -> {
                    help()
                    lastContact
                }
                is AddPhoneContactCommand -> {
                    val person = getOrCreatePerson(command.name, phoneBook)
                    addPhoneContact(person, command.phoneNumber)
                    lastContact = person.name
                }
                is AddEmailContactCommand -> {
                    val person = getOrCreatePerson(command.name, phoneBook)
                    addEmailContact(person, command.email)
                    lastContact = person.name
                }
                is ShowCommand -> {
                    val personName = command.name // Сохраняем имя контакта
                    val person = phoneBook[personName] // Получаем объект Person по имени
                    if (person != null) {
                        showLastContact(person, phoneBook)
                        lastContact = personName // Сохраняем имя контакта, а не объект Person
                    } else {
                        println("Контакт с именем $personName не найден")
                    }
                }
                is FindCommand -> {
                    findContact(command.query, phoneBook)
                    lastContact
                }
            }
        } else {
            println("Данные введены не корректно, повторите ввод, пожалуйста,\n" +
                    "или введите help для вызова подсказки.")
        }
    } while (command !is ExitCommand)
}

fun readComand(phoneBook: MutableMap<String, Person>, commands: MutableList<Command>, input: String? = null): Command? {
    val userInput = input ?: readlnOrNull() ?: ""
    val parts = userInput.split(" ")
    return when (parts[0]) {
        "exit" -> ExitCommand(userInput)
        "help" -> HelpCommand(userInput)
        "add" -> parseAddCommand(parts)
        "show" -> parseShowCommand(parts, phoneBook)
        "find" -> parseFindCommand(parts)
        else -> null
    }
}

fun parseShowCommand(parts: List<String>, phoneBook: MutableMap<String, Person>): Command? {
    if (parts.size != 2) return null // Проверяем, что в команде есть имя для показа
    val name = parts[1]
    return phoneBook[name]?.let { ShowCommand(name) } ?: run {
        println("Контакт с именем $name не найден")
        null
    }
}

fun parseAddCommand(parts: List<String>): Command?{
    if(parts.size !=4) return null
    val name = parts[1]
    val type = parts[2]
    val data = parts[3]

    return when(type){
        "phone" -> AddPhoneContactCommand(name, data)
        "email" -> AddEmailContactCommand(name, data)
        else -> null
    }
}

fun parseFindCommand(parts: List<String>): Command? {
    if (parts.size != 2) return null // Проверяем, что в команде есть запрос для поиска
    return FindCommand(parts[1]) // Возвращаем команду FindCommand с переданным запросом
}

fun getOrCreatePerson(name: String, phoneBook: MutableMap<String, Person>): Person {
    return phoneBook.getOrPut(name) { Person(name, mutableListOf(), mutableListOf()) }
}

fun help(){
    println("Доступные команды:")
    println("exit - выход из программы")
    println("help - показать доступные команды")
    println("add <Имя> phone <Номер телефона> - добавить контакт с номером телефона")
    println("add <Имя> email <Адрес электронной почты> - добавить контакт с email")
    println("show <Имя> - показать контакт по имени")
    println("find <email or phone> - найти контакт по email или номеру телефона")
}

fun addPhoneContact(person: Person, phoneNumber: String) {
    person.phone.add(phoneNumber)
    println("Контакт ${person.name} c номером телефона $phoneNumber добавлен")
}

fun addEmailContact(person: Person, email: String) {
    person.email.add(email)
    println("Контакт ${person.name} c электронной почтой $email добавлен")
}

fun showLastContact(person: Person?, phoneBook: MutableMap<String, Person>) {
    if (person != null) {
        println("Контакт ${person.name}:")
        println("Телефоны: ${person.phone.joinToString()}")
        println("Emails: ${person.email.joinToString()}")
    } else {
        println("Контакт не найден")
    }
}

fun findContact(query: String, phoneBook: MutableMap<String, Person>) {
    val foundPeople = mutableListOf<String>()
    phoneBook.forEach { (_, person) ->
        if (person.phone.contains(query) || person.email.contains(query)) {
            foundPeople.add(person.name)
        }
    }
    if (foundPeople.isNotEmpty()) {
        println("Найдены контакты для запроса '$query': ${foundPeople.joinToString()}")
    } else {
        println("Контакты для запроса '$query' не найдены")
    }
}