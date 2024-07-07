#include "input.h"

std::istream& operator >>(std::istream& in, DelimeterIO&& value)
{
  std::istream::sentry sentry(in);
  if (!sentry)
  {
    return in;
  }
  char sign = '0';
  in >> sign;
  if (in && (sign != value.symbol))
  {
    in.setstate(std::ios::failbit);
  }
  return in;
}
