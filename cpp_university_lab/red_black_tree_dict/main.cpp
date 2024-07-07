#include <iostream>
#include <string>
#include <list>
#include <fstream>
#include <sstream>
#include <Windows.h>
#include "red_black_tree.h"
#include "compare_operators.h"

int main()
{
	setlocale(LC_ALL, "Russian");
	SetConsoleCP(1251);
	SetConsoleOutputCP(1251);
	RedBlackTree dictionary;
	bool file_existence = false;
	std::ifstream in;
	while (file_existence == false && !std::cin.eof())
	{
		std::cout << "Введите имя файла со словарём\n";
		std::string input = "";
		std::getline(std::cin, input);
		input += ".txt";
		in.open(input);
		if (!in.is_open())
		{
			std::cout << "Словарь не открыт, повторите попытку\n";
		}
		else
		{
			file_existence = true;
		}
	}
	while (in.is_open() && !in.eof())
	{
		std::string key = "";
		std::getline(in, key, '-');
		std::list< std::string > translation;
		std::string russian_words = "";
		std::getline(in, russian_words);
		std::stringstream line(russian_words);
		while (!line.eof())
		{
			std::string word = "";
			std::getline(line >> std::ws, word, ',');
			translation.push_back(word);
		}
		dictionary.insert(key, translation);
	}
	while (!std::cin.eof())
	{
		std::string command = "";
		std::getline(std::cin, command);
		std::string key = "";
		if (command == "delete")
		{
			std::cout << "Введите слово: ";
			std::getline(std::cin, key);
			dictionary.deleteWord(key);
		}
		else if (command == "search")
		{
			std::cout << "Введите слово: ";
			std::getline(std::cin, key);
			std::cout << dictionary.checkExistence(key) << "\n";
			if (dictionary.checkExistence(key))
			{
				std::ofstream output;
				std::cout << "Введите название файла для вывода найденного слова\n";
				std::string filename = "";
				std::getline(std::cin, filename);
				filename += ".txt";
				output.open(filename);
				dictionary.search(key, output);
			}
		}
		else if (command == "print")
		{
			std::ofstream output;
			std::cout << "Введите название файла для вывода\n";
			std::string filename = "";
			std::getline(std::cin, filename);
			filename += ".txt";
			output.open(filename);
			dictionary.printDictionary(output);
		}
		else if (command == "help")
		{
			std::cout << "Список доступных команд: \n   help - вывод списка доступных команд\n   ";
			std::cout << "delete - удаление слова\n   search - поиск слова\n   ";
			std::cout << "print - вывод словаря\n";
		}
		else
		{
			if (!std::cin.eof())
			{
				std::cout << "Неизвестная команда\n";
			}
		}
	}
	return 0;
}
