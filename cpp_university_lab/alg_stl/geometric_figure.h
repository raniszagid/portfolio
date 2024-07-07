#ifndef GEOMETRIC_FIGURE_H
#define GEOMETRIC_FIGURE_H
#include <vector>
#include <iomanip>
#include <string>

struct Point
{
  int x, y;
};

struct Polygon
{
  std::vector< Point > points;
};

std::istream& operator >>(std::istream& in, Point& point);
std::istream& operator >>(std::istream& in, Polygon& polygon);
bool operator ==(Point first, Point second);
bool operator ==(Polygon first, Polygon second);

#endif
