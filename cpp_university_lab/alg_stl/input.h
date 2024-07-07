#ifndef INPUT_H
#define INPUT_H
#include <iomanip>

struct DelimeterIO
{
  char symbol;
};

std::istream& operator >>(std::istream& in, DelimeterIO&& value);

#endif
