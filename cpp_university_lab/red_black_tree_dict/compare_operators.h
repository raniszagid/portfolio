#ifndef COMPARE_OPERATORS_H
#define COMPARE_OPERATORS_H
#include <string>
#include <list>

bool operator <(const std::string& first, const std::string& second);
bool operator ==(const std::string& first, const std::string& second);
bool checkSpelling(std::string& key, std::list<std::string>& translation);

#endif
