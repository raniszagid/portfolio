#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <iterator>
#include <functional>
#include <map>
#include "commands.h"

int main(int argument, char* argument_value[])
{
  if (argument != 2)
  {
    std::cerr << "Invalid argument\n";
    return 1;
  }
  std::ifstream in(argument_value[1]);
  if (!in)
  {
    std::cerr << "Wrong filename\n";
    return 1;
  }
  std::vector< Polygon > box;
  while (!in.eof())
  {
    in.clear();
    std::copy(
      std::istream_iterator< Polygon >(in),
      std::istream_iterator< Polygon >(),
      std::back_inserter(box)
    );
  }
  ListOfCommands input_commands(box, std::cin, std::cout);
  std::map< std::string, std::function< void() > > map_commands = {
    {"AREA", std::bind(&ListOfCommands::getArea, std::ref(input_commands))},
    {"MAX", std::bind(&ListOfCommands::getMax, std::ref(input_commands))},
    {"MIN", std::bind(&ListOfCommands::getMin, std::ref(input_commands))},
    {"COUNT", std::bind(&ListOfCommands::getCount, std::ref(input_commands))},
    {"MAXSEQ", std::bind(&ListOfCommands::getMaxSeq, std::ref(input_commands))},
    {"RIGHTSHAPES", std::bind(&ListOfCommands::getRightShapes, std::ref(input_commands))}
  };
  std::string method = "";
  std::cin >> method;
  while (!std::cin.eof())
  {
    std::map< std::string, std::function< void() > >::iterator it = map_commands.find(method);
    if (it != map_commands.end())
    {
      it->second();
    }
    else
    {
      writeInvalid(std::cout);
      std::cin.ignore(std::numeric_limits< std::streamsize >::max(), '\n');
    }
    std::cin.clear();
    std::cin >> method;
  }
  in.close();
  return 0;
}
