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
		std::cout << "������� ��� ����� �� �������\n";
		std::string input = "";
		std::getline(std::cin, input);
		input += ".txt";
		in.open(input);
		if (!in.is_open())
		{
			std::cout << "������� �� ������, ��������� �������\n";
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
			std::cout << "������� �����: ";
			std::getline(std::cin, key);
			dictionary.deleteWord(key);
		}
		else if (command == "search")
		{
			std::cout << "������� �����: ";
			std::getline(std::cin, key);
			std::cout << dictionary.checkExistence(key) << "\n";
			if (dictionary.checkExistence(key))
			{
				std::ofstream output;
				std::cout << "������� �������� ����� ��� ������ ���������� �����\n";
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
			std::cout << "������� �������� ����� ��� ������\n";
			std::string filename = "";
			std::getline(std::cin, filename);
			filename += ".txt";
			output.open(filename);
			dictionary.printDictionary(output);
		}
		else if (command == "help")
		{
			std::cout << "������ ��������� ������: \n   help - ����� ������ ��������� ������\n   ";
			std::cout << "delete - �������� �����\n   search - ����� �����\n   ";
			std::cout << "print - ����� �������\n";
		}
		else
		{
			if (!std::cin.eof())
			{
				std::cout << "����������� �������\n";
			}
		}
	}
	return 0;
}
