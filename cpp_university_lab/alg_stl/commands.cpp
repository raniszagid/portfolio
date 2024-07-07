#include "commands.h"
#include <vector>
#include <algorithm>

double getSquare(Polygon polygon)
{
  polygon.points.push_back(polygon.points[0]);
  std::vector< int > summands;
  double square = 0.0;
  std::transform(
    std::begin(polygon.points),
    std::end(polygon.points) - 1,
    std::begin(polygon.points) + 1,
    std::back_inserter(summands),
    [&square](const Point& first, const Point& second)
    {
      int summand = first.x * second.y - second.x * first.y;
      square += summand;
      return summand;
    }
  );
  return abs(square) / 2;
}

ListOfCommands::ListOfCommands(std::vector< Polygon > box, std::istream& in, std::ostream& out):
  box_(box),
  in_(in),
  out_(out)
{}

void ListOfCommands::getArea()
{
  std::string parameter = "";
  in_ >> parameter;
  if (parameter == "EVEN")
  {
    out_ << std::fixed << std::setprecision(1) << summarizeEvenOrOdd(box_, true) << '\n';
  }
  else if (parameter == "ODD")
  {
    out_ << std::fixed << std::setprecision(1) << summarizeEvenOrOdd(box_, false) << '\n';
  }
  else if (parameter == "MEAN")
  {
    if (box_.empty())
    {
      writeInvalid(out_);
    }
    else
    {
      out_ << std::fixed << std::setprecision(1) << getAverage(box_) << '\n';
    }
  }
  else if (std::all_of(parameter.begin(), parameter.end(), ::isdigit))
  {
    size_t number_param = std::stoi(parameter);
    if (number_param < 3)
    {
      writeInvalid(out_);
      return;
    }
    else
    {
      out_ << std::fixed << std::setprecision(1) << countAreaCertainNum(box_, number_param) << '\n';
    }
  }
  else
  {
    writeInvalid(out_);
  }
}

void ListOfCommands::getMax()
{
  std::string parameter = "";
  in_ >> parameter;
  if (box_.empty())
  {
    writeInvalid(out_);
    return;
  }
  if (parameter == "AREA")
  {
    out_ << std::fixed << std::setprecision(1) << getMaxArea(box_) << '\n';
  }
  else if (parameter == "VERTEXES")
  {
    out_ << getMaxAnglesNumber(box_) << '\n';
  }
  else
  {
    writeInvalid(out_);
    return;
  }
}

void ListOfCommands::getMin()
{
  std::string parameter = "";
  in_ >> parameter;
  if (box_.empty())
  {
    writeInvalid(out_);
    return;
  }
  if (parameter == "AREA")
  {
    out_ << std::fixed << std::setprecision(1) << getMinArea(box_) << '\n';
  }
  else if (parameter == "VERTEXES")
  {
    out_ << getMinAnglesNumber(box_) << '\n';
  }
  else
  {
    writeInvalid(out_);
  }
}

void ListOfCommands::getCount()
{
  std::string parameter = "";
  in_ >> parameter;
  if (parameter == "EVEN")
  {
    out_ << countEvenFigures(box_) << '\n';
  }
  else if (parameter == "ODD")
  {
    out_ << countOddFigures(box_) << '\n';
  }
  else if (std::all_of(parameter.begin(), parameter.end(), ::isdigit))
  {
    size_t number_param = std::stoi(parameter);
    if (number_param < 3)
    {
      writeInvalid(out_);
      return;
    }
    else
    {
      out_ << std::fixed << std::setprecision(1) << countAnglesNum(box_, number_param) << '\n';
    }
  }
  else
  {
    writeInvalid(out_);
  }
}

void ListOfCommands::getRightShapes()
{
  out_ << countRightShapes(box_) << "\n";
}

void ListOfCommands::getMaxSeq()
{
  Polygon value;
  in_ >> value;
  if (in_.fail())
  {
    writeInvalid(out_);
  }
  else
  {
    out_ << countMaxSequence(box_, value) << '\n';
  }
}

void writeInvalid(std::ostream& out)
{
  out << "<INVALID COMMAND>\n";
}

double summarizeEvenOrOdd(std::vector< Polygon > shapes, bool evenness)
{
  std::vector< Polygon > even_or_odd_polygons;
  std::vector< double > squares;
  if (evenness == true)
  {
    std::copy_if(
      std::begin(shapes),
      std::end(shapes),
      std::back_inserter(even_or_odd_polygons),
      [](const Polygon& polygon)
      {
        return polygon.points.size() % 2 == 0;
      }
    );
  }
  else
  {
    std::copy_if(
      std::begin(shapes),
      std::end(shapes),
      std::back_inserter(even_or_odd_polygons),
      [](const Polygon& polygon)
      {
        return polygon.points.size() % 2 == 1;
      }
    );
  }
  double sum = 0.0;
  std::transform(
    std::begin(even_or_odd_polygons),
    std::end(even_or_odd_polygons),
    std::back_inserter(squares),
    [&sum](const Polygon& polygon)
    {
      double square = 0.0;
      square = getSquare(polygon);
      sum += square;
      return square;
    }
  );
  return sum;
}

double getAverage(std::vector< Polygon > shapes)
{
  std::vector< double > squares;
  double sum = 0.0;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(squares),
    [&sum](const Polygon& polygon)
    {
      double square = 0.0;
      square = getSquare(polygon);
      sum += square;
      return square;
    }
  );
  return sum / shapes.size();
}

double countAreaCertainNum(std::vector< Polygon > shapes, size_t quantity)
{
  std::vector< Polygon > certain_polygons;
  std::copy_if(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(certain_polygons),
    [quantity](const Polygon& polygon)
    {
      return polygon.points.size() == quantity;
    }
  );
  std::vector< double > squares;
  double sum = 0.0;
  std::transform(
    std::begin(certain_polygons),
    std::end(certain_polygons),
    std::back_inserter(squares),
    [&sum](const Polygon& polygon)
    {
      double square = 0.0;
      square = getSquare(polygon);
      sum += square;
      return square;
    }
  );
  return sum;
}

double getMaxArea(std::vector< Polygon > shapes)
{
  std::vector< double > squares;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(squares),
    getSquare
  );
  std::sort(squares.begin(), squares.end());
  return *(squares.end() - 1);
}

double getMinArea(std::vector< Polygon > shapes)
{
  std::vector< double > squares;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(squares),
    getSquare
  );
  std::sort(squares.begin(), squares.end());
  return *squares.begin();
}

size_t getMaxAnglesNumber(std::vector< Polygon > shapes)
{
  std::vector< size_t > angles_quantities;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(angles_quantities),
    [](const Polygon& polygon)
    {
      return polygon.points.size();
    }
  );
  std::sort(angles_quantities.begin(), angles_quantities.end());
  return *(angles_quantities.end() - 1);
}

size_t getMinAnglesNumber(std::vector< Polygon > shapes)
{
  std::vector< size_t > angles_quantities;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(angles_quantities),
    [](const Polygon& polygon)
    {
      return polygon.points.size();
    }
  );
  std::sort(angles_quantities.begin(), angles_quantities.end());
  return *angles_quantities.begin();
}

size_t countEvenFigures(std::vector< Polygon > shapes)
{
  size_t number = 0;
  number = std::count_if(
    std::begin(shapes),
    std::end(shapes),
    [](const Polygon& polygon)
    {
      return polygon.points.size() % 2 == 0;
    }
  );
  return number;
}

size_t countOddFigures(std::vector< Polygon > shapes)
{
  size_t number = 0;
  number = std::count_if(
    std::begin(shapes),
    std::end(shapes),
    [](const Polygon& polygon)
    {
      return polygon.points.size() % 2 == 1;
    }
  );
  return number;
}

size_t countAnglesNum(std::vector< Polygon > shapes, size_t quantity)
{
  size_t number = 0;
  number = std::count_if(
    std::begin(shapes),
    std::end(shapes),
    [quantity](const Polygon& polygon)
    {
      return polygon.points.size() == quantity;
    }
  );
  return number;
}

size_t countRightShapes(std::vector< Polygon > shapes)
{
  size_t quantity = 0;
  quantity = std::count_if(
    std::begin(shapes),
    std::end(shapes),
    isRightShape
  );
  return quantity;
}

bool isRightShape(Polygon polygon)
{
  polygon.points.push_back(polygon.points[0]);
  std::vector< Point > geom_vectors;
  std::transform(
    std::begin(polygon.points),
    std::end(polygon.points) - 1,
    std::begin(polygon.points) + 1,
    std::back_inserter(geom_vectors),
    [](const Point& first, const Point& second)
    {
      Point geom_vector = {0, 0};
      geom_vector.x = second.x - first.x;
      geom_vector.y = second.y - first.y;
      return geom_vector;
    }
  );
  geom_vectors.push_back(geom_vectors[0]);
  std::vector< int > scalar_multiplication;
  std::transform(
    std::begin(geom_vectors),
    std::end(geom_vectors) - 1,
    std::begin(geom_vectors) + 1,
    std::back_inserter(scalar_multiplication),
    [](const Point& first, const Point& second)
    {
      return first.x * second.x + first.y * second.y;
    }
  );
  std::vector< int >::iterator position = std::find(
    std::begin(scalar_multiplication),
    std::end(scalar_multiplication),
    0
  );
  return position != std::end(scalar_multiplication);
}

size_t countMaxSequence(std::vector< Polygon > shapes, Polygon value)
{
  size_t max = 0;
  size_t count = 0;
  std::vector< bool > sequence;
  std::transform(
    std::begin(shapes),
    std::end(shapes),
    std::back_inserter(sequence),
    [&max, &count, &value](Polygon figure)
    {
      if (figure == value)
      {
        count++;
        return true;
      }
      else
      {
        if (max < count)
        {
          max = count;
        }
        count = 0;
        return false;
      }
    }
  );
  return max;
}
