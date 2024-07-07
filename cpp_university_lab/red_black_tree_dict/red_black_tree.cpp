#include <iostream>
#include "red_black_tree.h"
#include "compare_operators.h"

std::ostream& operator <<(std::ostream& out, RedBlackTree::Node* node)
{
	out << node->key_ << " - ";
	std::list<std::string>::iterator it = node->translation_.begin();
	while (it != node->translation_.end())
	{
		out << *it;
		it++;
		if (it != node->translation_.end())
		{
			out << ", ";
		}
	}
	out << "\n";
	return out;
}

RedBlackTree::RedBlackTree(RedBlackTree&& other) noexcept : root_(other.root_)
{
	other.root_ = nullptr;
}

RedBlackTree& RedBlackTree::operator=(RedBlackTree&& other) noexcept
{
	if (this != &other)
	{
		std::swap(*this, other);
	}
	return *this;
}

RedBlackTree::~RedBlackTree()
{
	cutDownBranch(root_);
}

void RedBlackTree::insert(std::string key, std::list<std::string>& translation)
{
	if (key != "")
	{
		if (key.back() == ' ')
		{
			key.erase(key.end()-1);
		}
	}
	if (checkSpelling(key, translation))
	{
		translation.sort();
		if (checkExistence(key) == false) {
			Node* word = new Node(key, translation, red, nullptr, nullptr, nullptr);
			insertNode(word);
		}
		else
		{
			std::cout << "Слово \"" << key << "\" уже имеется в словаре\n";
		}
	}
	else
	{
		std::cout << "Элемент с ключом \"" << key << "\" содержит некорректные для словаря символы\n";
	}
}

void RedBlackTree::insertNode(Node* word)
{
	Node* previous = nullptr;
	Node* current = root_;
	while (current != nullptr)
	{
		previous = current;
		if (word->key_ < current->key_)
		{
			current = current->left_;
		}
		else
		{
			current = current->right_;
		}
	}
	word->parent_ = previous;
	if (previous != nullptr)
	{
		if (word->key_ < previous->key_)
		{
			previous->left_ = word;
		}
		else
		{
			previous->right_ = word;
		}
	}
	else
	{
		root_ = word;
	}
	word->colour_ = red;
	keepRedBlackFeatures(word);
}

void RedBlackTree::keepRedBlackFeatures(Node* word)
{
	Node* parent;
	parent = word->parent_;
	while (word != root_ && parent->colour_ == red)
	{
		Node* grandfather = parent->parent_;
		if (grandfather->left_ == parent)
		{
			Node* uncle = grandfather->right_;
			if (uncle != nullptr && uncle->colour_ == red)
			{
				parent->colour_ = black;
				uncle->colour_ = black;
				grandfather->colour_ = red;
				word = grandfather;
				parent = word->parent_;
			}
			else
			{
				if (parent->right_ == word)
				{
					rotateLeft(parent);
					std::swap(word, parent);
				}
				rotateRight(grandfather);
				grandfather->colour_ = red;
				parent->colour_ = black;
				break;
			}
		}
		else
		{
			Node* uncle = grandfather->left_;
			if (uncle != nullptr && uncle->colour_ == red)
			{
				grandfather->colour_ = red;
				parent->colour_ = black;
				uncle->colour_ = black;
				word = grandfather;
				parent = word->parent_;
			}
			else
			{
				if (parent->left_ == word)
				{
					rotateRight(parent);
					std::swap(parent, word);
				}
				rotateLeft(grandfather);
				parent->colour_ = black;
				grandfather->colour_ = red;
				break;
			}
		}
	}
	root_->colour_ = black;
}

void RedBlackTree::rotateLeft(Node* older)
{
	Node* young = older->right_;
	older->right_ = young->left_;
	if (young->left_ != nullptr)
		young->left_->parent_ = older;
	young->parent_ = older->parent_;
	if (older->parent_ == nullptr)
		root_ = young;
	else {
		if (older == older->parent_->left_)
			older->parent_->left_ = young;
		else
			older->parent_->right_ = young;
	}
	young->left_ = older;
	older->parent_ = young;
}

void RedBlackTree::rotateRight(Node* older)
{
	Node* young = older->left_;
	older->left_ = young->right_;
	if (young->right_ != nullptr)
		young->right_->parent_ = older;
	young->parent_ = older->parent_;
	if (older->parent_ == nullptr)
		root_ = young;
	else {
		if (older == older->parent_->right_)
			older->parent_->right_ = young;
		else
			older->parent_->left_ = young;
	}
	young->right_ = older;
	older->parent_ = young;
}

void RedBlackTree::cutDownBranch(Node*& root)
{
	if (root != nullptr)
	{
		cutDownBranch(root->left_);
		cutDownBranch(root->right_);
		delete root;
		root = nullptr;
	}
}

void RedBlackTree::printDictionary(std::ostream& out) const {
	if (root_ == nullptr || root_->key_ == "")
		std::cout << "Cловарь пуст\n" << std::endl;
	else {
		inorder_Walk(root_, out);
		out << "\n";
	}
}

void RedBlackTree::inorder_Walk(Node* node, std::ostream& out) const {
	if (node)
	{
		inorder_Walk(node->left_, out);
		out << node;
		inorder_Walk(node->right_, out);
	}
}

void RedBlackTree::search(std::string word, std::ostream& out)
{
	if (root_ == nullptr)
	{
		std::cout << "Cловарь пуст" << "\n";
	}
	else
	{
		Node* current = root_;
		bool similarity = false;
		while (current != nullptr && similarity != true) {
			if (word == current->key_)
			{
				out << current << "\n";
				similarity = true;
			}
			else
			{
				if (word < current->key_)
				{
					current = current->left_;
				}
				else 
				{
					current = current->right_;
				}
			}
		}
		if (similarity == false)
		{
			std::cout << "Слово не найдено\n";
		}
	}
}

bool RedBlackTree::checkExistence(std::string word)
{
	if (root_ == nullptr)
	{
		return false;
	}
	else
	{
		Node* current = root_;
		while (current != nullptr)
		{
			if (word == current->key_)
			{
				return true;
			}
			else
			{
				if (word < current->key_)
				{
					current = current->left_;
				}
				else
				{
					current = current->right_;
				}
			}
		}
		return false;
	}
}

RedBlackTree::Node* RedBlackTree::getNode(std::string word)
{
	if (root_ == nullptr)
	{
		return nullptr;
	}
	else
	{
		Node* current = root_;
		while (current != nullptr)
		{
			if (word == current->key_)
			{
				return current;
			}
			else
			{
				if (word < current->key_)
				{
					current = current->left_;
				}
				else
				{
					current = current->right_;
				}
			}
		}
		return nullptr;
	}
}

void RedBlackTree::deleteWord(std::string word)
{
	if (checkExistence(word) == true)
	{
		Node* waste = getNode(word);
		deleteNode(waste);
	}
	else
	{
		std::cout << "Вы пытаетесь удалить данные, которых нет в словаре\n";
	}
}

void RedBlackTree::deleteNode(Node* waste)
{
	if (waste == root_)
	{
		delete waste;
	}
	else
	{
		if (waste->left_ == nullptr && waste->right_ == nullptr)
		{
			if (waste->colour_ == black)
			{
				fixBlackLeafDeletion(waste);
			}
			if (waste->parent_->left_ == waste)
			{
				waste->parent_->left_ = nullptr;
			}
			else if (waste->parent_->right_ == waste)
			{
				waste->parent_->right_ = nullptr;
			}
			delete waste;
		}
		else if (waste->right_ != nullptr && waste->left_ == nullptr)
		{
			waste->right_->colour_ = black;
			if (waste->parent_->left_ == waste)
			{
				waste->parent_->left_ = waste->right_;
			}
			else if (waste->parent_->right_ == waste)
			{
				waste->parent_->right_ = waste->right_;
			}
			waste->right_->parent_ = waste->parent_;
			delete waste;
		}
		else if (waste->right_ == nullptr && waste->left_ != nullptr)
		{
			waste->left_->colour_ = black;
			if (waste->parent_->left_ == waste)
			{
				waste->parent_->left_ = waste->left_;
			}
			else if (waste->parent_->right_ == waste)
			{
				waste->parent_->right_ = waste->left_;
			}
			waste->left_->parent_ = waste->parent_;
			delete waste;
		}
		else if (waste->right_ != nullptr && waste->left_ != nullptr)
		{
			Node* minimum = getMinimumRightBranch(waste);
			std::swap(minimum->key_, waste->key_);
			std::swap(minimum->translation_, waste->translation_);
			deleteNode(minimum);
		}
	}
}

RedBlackTree::Node* RedBlackTree::getMinimumRightBranch(Node* master)
{
	Node* minimum = master->right_;
	if (minimum->left_ == nullptr)
	{
		return minimum;
	}
	else
	{
		while (minimum->left_ != nullptr)
		{
			minimum = minimum->left_;
		}
		return minimum;
	}
}

void RedBlackTree::fixBlackLeafDeletion(Node* waste)
{
	if (waste->colour_ == black && waste != root_)
	{
		Node* father = waste->parent_;
		if (waste->parent_->right_ == waste)
		{
			Node* brother = waste->parent_->left_;
			if (father->colour_ == red)
			{
				if (brother->left_ == nullptr && brother->right_ == nullptr)
				{
					brother->colour_ = red;
					father->colour_ = black;
				}
				else if (brother->left_ != nullptr)
				{
					if (brother->left_->colour_ == red)
					{
						rotateRight(father);
						brother->colour_ = red;
						father->colour_ = black;
						brother->left_->colour_ = black;
					}
				}
				else if (brother->left_ == nullptr && brother->right_->colour_ == red)
				{
					rotateLeft(brother);
					rotateRight(father);
					father->colour_ = black;
				}
			}
			else if (father->colour_ == black)
			{
				if (brother->colour_ == red)
				{
					if (brother->right_->right_ == nullptr && brother->right_->left_ == nullptr)
					{
						brother->right_->colour_ = red;
						brother->colour_ = black;
						rotateRight(father);
					}
					else if (brother->right_->left_ != nullptr)
					{
						if (brother->right_->left_->colour_ == red)
						{
							brother->right_->left_->colour_ = black;
							rotateLeft(brother);
							rotateRight(father);
						}
					}
					else if (brother->right_->left_ == nullptr && brother->right_->right_->colour_ == red)
					{
						brother->colour_ = black;
						brother->left_->colour_ = red;
						rotateLeft(brother);
						rotateRight(father);
					}
				}
				else if (brother->colour_ == black)
				{
					if (brother->right_ != nullptr && brother->right_->colour_ == red)
					{
						brother->right_->colour_ = black;
						rotateLeft(brother);
						rotateRight(father);
					}
					else if (brother->right_ == nullptr && brother->left_ != nullptr)
					{
						if (brother->left_->colour_ == red)
						{
							brother->left_->colour_ = black;
							rotateRight(father);
						}
					}
					else if (brother->right_ == nullptr && brother->left_ == nullptr)
					{
						brother->colour_ = red;
						fixBlackLeafDeletion(father);
					}
				}
			}
		}
		else if (waste->parent_->left_ == waste)
		{
			Node* brother = waste->parent_->right_;
			if (father->colour_ == red)
			{
				if (brother->left_ == nullptr && brother->right_ == nullptr)
				{
					brother->colour_ = red;
					father->colour_ = black;
				}
				else if (brother->right_ != nullptr)
				{
					if (brother->right_->colour_ == red)
					{
						rotateLeft(father);
						brother->colour_ = red;
						father->colour_ = black;
						brother->right_->colour_ = black;
					}
				}
				else if (brother->right_ == nullptr && brother->left_->colour_ == red)
				{
					rotateRight(brother);
					rotateLeft(father);
					father->colour_ = black;
				}
			}
			else if (father->colour_ == black)
			{
				if (brother->colour_ == red)
				{
					if (brother->left_->right_ == nullptr && brother->left_->left_ == nullptr)
					{
						brother->left_->colour_ = red;
						brother->colour_ = black;
						rotateLeft(father);
					}
					else if (brother->left_->right_ != nullptr)
					{
						if (brother->left_->right_->colour_ == red)
						{
							brother->left_->right_->colour_ = black;
							rotateRight(brother);
							rotateLeft(father);
						}
					}
					else if (brother->left_->right_ == nullptr && brother->left_->left_->colour_ == red)
					{
						brother->colour_ = black;
						brother->right_->colour_ = red;
						rotateRight(brother);
						rotateLeft(father);
					}
				}
				else if (brother->colour_ == black)
				{
					if (brother->left_ != nullptr && brother->left_->colour_ == red)
					{
						brother->left_->colour_ = black;
						rotateRight(brother);
						rotateLeft(father);
					}
					else if (brother->left_ == nullptr && brother->right_ != nullptr)
					{
						if (brother->right_->colour_ == red)
						{
							brother->right_->colour_ = black;
							rotateLeft(father);
						}
					}
					else if (brother->right_ == nullptr && brother->left_ == nullptr)
					{
						brother->colour_ = red;
						fixBlackLeafDeletion(father);
					}
				}
			}
		}
	}
}

void RedBlackTree::walkByLevels() const
{
	if (!root_)
		std::cout << "The tree is empty" << std::endl;
	else {
		std::queue<Node*> q;
		q.push(root_);
		while (!q.empty()) {
			Node* node = q.front();
			q.pop();
			std::cout << node->key_ << " " << node->colour_ << '\n';
			if (node->left_)
				q.push(node->left_);
			if (node->right_)
				q.push(node->right_);
		}
		std::cout << std::endl;
	}
}

RedBlackTree::Node* RedBlackTree::copyBranch(const Node* other)
{
	Node* new_root = nullptr;
	if (other != nullptr)
	{
		new_root = new Node(other->key_, other->translation_, other->colour_, nullptr, nullptr, nullptr);
		new_root->parent_ = copyBranch(other->parent_);
		new_root->left_ = copyBranch(other->left_);
		new_root->right_ = copyBranch(other->right_);
	}
	return new_root;
}
