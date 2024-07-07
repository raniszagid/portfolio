#include "compare_operators.h"

bool operator ==(const std::string& first, const std::string& second)
{
	if (first.size() != second.size())
	{
		return false;
	}
	else
	{
		size_t letter = 0;
		while (letter < first.size())
		{
			if (first[letter] != second[letter])
			{
				return false;
			}
			letter++;
		}
		return true;
	}
}

bool operator <(const std::string& first, const std::string& second)
{
	size_t minimum = second.size();
	if (first.size() < second.size())
	{
		minimum = first.size();
	}
	size_t letter = 0;
	bool differnce = false;
	while (letter < minimum && !differnce)
	{
		if (int(first[letter]) < int(second[letter]))
		{
			return true;
		}
		else if (int(first[letter]) > int(second[letter]))
		{
			return false;
		}
		letter++;
	}
	if (!differnce)
	{
		if (first.size() < second.size())
		{
			return true;
		}
		else if (first.size() > second.size())
		{
			return false;
		}
	}
}

bool checkSpelling(std::string& key, std::list<std::string>& translation)
{
	size_t letter = 0;
	int i = 0;
	while (letter < key.size())
	{
		i = int(key[letter]);
		if ((i >= 97 && i <= 122) || (i >= 65 && i <= 90) || i == 32 || i == 45 || i == 39) {
			letter++;
		}
		else
		{
			return false;
		}
	}
	return true;
}
