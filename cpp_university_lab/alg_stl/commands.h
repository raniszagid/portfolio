#ifndef COMMANDS_H
#define COMMANDS_H
#include "geometric_figure.h"

struct ListOfCommands
{
public:
  ListOfCommands(std::vector< Polygon > box, std::istream& in, std::ostream& out);
  void getArea();
  void getMax();
  void getMin();
  void getCount();
  void getMaxSeq();
  void getRightShapes();
private:
  std::vector< Polygon > box_;
  std::istream& in_;
  std::ostream& out_;
};

double getSquare(Polygon polygon);
double summarizeEvenOrOdd(std::vector< Polygon > shapes, bool evenness);
double getAverage(std::vector< Polygon > shapes);
double countAreaCertainNum(std::vector< Polygon > shapes, size_t quantity);
double getMaxArea(std::vector< Polygon > shapes);
size_t getMaxAnglesNumber(std::vector< Polygon > shapes);
double getMinArea(std::vector< Polygon > shapes);
size_t getMinAnglesNumber(std::vector< Polygon > shapes);
size_t countEvenFigures(std::vector< Polygon > shapes);
size_t countOddFigures(std::vector< Polygon > shapes);
size_t countAnglesNum(std::vector< Polygon > shapes, size_t quantity);
size_t countRightShapes(std::vector< Polygon > shapes);
bool isRightShape(Polygon polygon);
size_t countMaxSequence(std::vector< Polygon > shapes, Polygon value);
void writeInvalid(std::ostream& out);

#endif
