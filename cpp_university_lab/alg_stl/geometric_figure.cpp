#include "geometric_figure.h"
#include <iterator>
#include <algorithm>
#include "input.h"

std::istream& operator >>(std::istream& in, Point& point)
{
  std::istream::sentry sentry(in);
  if (!sentry)
  {
    return in;
  }
  in >> DelimeterIO{'('} >> point.x >> DelimeterIO{';'} >> point.y >> DelimeterIO{')'};
  return in;
}

std::istream& operator >>(std::istream& in, Polygon& polygon)
{
  std::istream::sentry sentry(in);
  if (!sentry)
  {
    return in;
  }
  Polygon input;
  size_t angles = 0;
  if (!(in >> angles) || angles < 3)
  {
    in.ignore(std::numeric_limits< std::streamsize >::max(), '\n');
    in.setstate(std::ios::failbit);
    return in;
  }
  Point point{ 0, 0 };
  std::string line;
  std::string coordinate;
  std::getline(in, line);
  line.erase(0, 1);
  while (!line.empty())
  {
    if (line[0] != '(' || line[line.length() - 1] != ')')
    {
      in.setstate(std::ios::failbit);
      return in;
    }
    coordinate = line.substr(1, line.find(';') - 1);
    if (coordinate.empty() || std::find_if(
        coordinate.begin(),
        coordinate.end(),
        [](char symbol)
        {
          return !std::isdigit(symbol) && symbol != '-';
        }
      ) != coordinate.end())
    {
      in.setstate(std::ios::failbit);
      return in;
    }
    point.x = std::stoi(coordinate);
    coordinate = line.substr(line.find(';') + 1, line.find(')') - line.find(';') - 1);
    if (coordinate.empty() || std::find_if(
      coordinate.begin(),
      coordinate.end(),
      [](char c)
      {
        return !std::isdigit(c) && c != '-';
      }
    ) != coordinate.end())
    {
      in.setstate(std::ios::failbit);
      return in;
    }
    point.y = std::stoi(coordinate);
    input.points.push_back(point);
    line.erase(0, line.find(')') + 2);
  }
  if (input.points.size() != angles)
  {
    in.setstate(std::ios::failbit);
  }
  if (in)
  {
    polygon = input;
  }
  return in;
}

bool operator ==(Point first, Point second)
{
  return (first.x == second.x) && (first.y == second.y);
}

bool operator ==(Polygon first, Polygon second)
{
  bool equality = std::equal(
    std::begin(first.points),
    std::end(first.points),
    std::begin(second.points),
    std::end(second.points)
  );
  return equality;
}
