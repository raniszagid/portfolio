#ifndef RED_BLACK_TREE_H
#define RED_BLACK_TREE_H
#include <string>
#include <list>
#include <queue>
#include <iterator>
#include <algorithm>

enum colour {black, red};

class RedBlackTree
{
public:
	RedBlackTree() : root_(nullptr) {};
	RedBlackTree(const RedBlackTree& other) = delete;
	RedBlackTree& operator=(const RedBlackTree& other) = delete;
	RedBlackTree(RedBlackTree&& other) noexcept;
	RedBlackTree& operator=(RedBlackTree&& other) noexcept;
	~RedBlackTree();
	void printDictionary(std::ostream& out) const;
	void walkByLevels() const;
	void insert(std::string key, std::list<std::string>& translation);
	void search(std::string word, std::ostream& out);
	void deleteWord(std::string word);
	bool checkExistence(std::string word);
private:
	struct Node {
		std::string key_;
		std::list<std::string> translation_;
		colour colour_;
		Node* parent_;
		Node* left_;
		Node* right_;
		Node(std::string key, std::list<std::string> translation, colour c, Node* parent, Node* left, Node* right) :
			key_(key), translation_(translation), colour_(c), parent_(parent), left_(left), right_(right) {}
	};
	friend std::ostream& operator <<(std::ostream& out, Node* node);
	Node* root_;
	void insertNode(Node* word);
	void keepRedBlackFeatures(Node* word);
	void rotateLeft(Node* older);
	void rotateRight(Node* older);
	void cutDownBranch(Node*& root);
	void inorder_Walk(Node* node, std::ostream& out) const;
	Node* getNode(std::string word);
	void deleteNode(Node* waste);
	Node* getMinimumRightBranch(Node* master);
	void fixBlackLeafDeletion(Node* waste);
	Node* copyBranch(const Node* other);
};

#endif
